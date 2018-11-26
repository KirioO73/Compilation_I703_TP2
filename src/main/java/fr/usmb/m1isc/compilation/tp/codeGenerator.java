package fr.usmb.m1isc.compilation.tp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

class codeGenerator {
    private Arbre A;
    private List<String> variables;
    private PrintWriter PW;

    codeGenerator( Arbre A, String Path) throws FileNotFoundException {
        this.A = A;
        this.variables = new ArrayList<>();
        PW = new PrintWriter(new FileOutputStream(Path));
        genereData();
        genereCode();
        PW.close();
    }

    private void genereData(){
        PW.println("DATA SEGMENT");
        findLet(A);
        for (String variable : this.variables) PW.println("    " + variable + " DD");
        PW.println("DATA ENDS");
    }

    private void findLet(Arbre a) {
        if (a == null) return;
        else {
            if (a.getType() == NoeudType.LET) {
                this.variables.add(a.getFg().getVal());
                findLet(a.getFg());
            } else {
                findLet(a.getFd());
                findLet(a.getFg());
            }
        }
    }

    private void genereCode(){
        PW.println("CODE SEGMENT");
        parcourArbre(A);
        PW.println("CODE ENDS");
    }

    int n_if = 0;
    int n_while = 0;
    private void parcourArbre( Arbre a) {
        if(a==null);
        else{
            switch (a.getType()){
                case SEMI:
                    parcourArbre(a.getFg());
                    parcourArbre(a.getFd());
                    break;
                case LET:
                    ecrireLet(a);
                    break;
                case ENTIER:
                    ecrireEntier(a);
                    break;
                case IDENT:
                    ecrireIdent(a);
                    break;

                case INPUT:
                    ecrireInput(a);
                    break;
                case OUTPUT:
                    ecrireOutput(a);
                    break;

                case PLUS:
                    ecrirePlus(a);
                    break;
                case MOINS:
                    ecrireMoins(a);
                    break;
                case DIV:
                    ecrireDiv(a);
                    break;
                case MUL:
                    ecrireMul(a);
                    break;

                case IF:
                    ecrireIf(a);
                    break;
                case WHILE:

                default:
                    parcourArbre(a.getFg());
                    parcourArbre(a.getFd());
                    PW.println("Default");
            }
        }
    }


    // Input/Output
    private void ecrireOutput(Arbre a) {
        parcourArbre(a.getFg());
        PW.println("    out eax");
    }

    private void ecrireInput(Arbre a) {
        PW.println("    in eax");
    }

    // LET
    private void ecrireLet(Arbre a) {
        parcourArbre(a.getFd());
        PW.println("    mov " + a.getFg().getVal() + ", eax" );
    }


    //Arithmétiques
    private void ecrirePlus(Arbre a) {
        parcourArbre(a.getFg());
        PW.println("    push eax");
        parcourArbre(a.getFd());
        PW.println("    pop ebx");
        PW.println("    add eax, ebx");
    }

    private void ecrireMoins(Arbre a) {
        parcourArbre(a.getFg());
        PW.println("    push eax");
        parcourArbre(a.getFd());
        PW.println("    pop ebx");
        PW.println("    sub eax, ebx");
    }

    private void ecrireMul(Arbre a) {
        parcourArbre(a.getFg());
        PW.println("    push eax");
        parcourArbre(a.getFd());
        PW.println("    pop ebx");
        PW.println("    mul eax, ebx");
    }

    private void ecrireDiv(Arbre a) {
        parcourArbre(a.getFg());
        PW.println("    push eax");
        parcourArbre(a.getFd());
        PW.println("    pop ebx");
        PW.println("    div ebx, eax");
        PW.println("    mov eax, ebx");
    }

    //Operators Boolean
    private void ecrireEqual(Arbre a) {
        parcourArbre(a.getFg());
        PW.println("    push eax");
        parcourArbre(a.getFd());
        PW.println("    pop ebx");
        PW.println("    sub eax, ebx");
    }

    private void ecrireGT(Arbre a) {
        parcourArbre(a.getFg());
        PW.println("    push eax");
        parcourArbre(a.getFd());
        PW.println("    pop ebx");
        PW.println("    sub eax, ebx");
    }

    private void ecrireGTE(Arbre a) {
        parcourArbre(a.getFg());
        PW.println("    push eax");
        parcourArbre(a.getFd());
        PW.println("    pop ebx");
        PW.println("    sub eax, ebx");
    }

    // Conditionals
    private void ecrireIf(Arbre a) {
        n_if++;
        switch(a.getFg().getType()){
            case EGAL:
                ecrireEqual(a.getFg());
                PW.println("    jz vrai_if_" + n_if );
                PW.println("    jmp faux_if_" + n_if);
                break;
            case GT:
                ecrireGT(a.getFg());
                PW.println("    jl vrai_if_" + n_if );
                PW.println("    jmp faux_if_" + n_if);
                break;
            case GTE:
                ecrireGTE(a.getFg());
                PW.println("    jle vrai_if_" + n_if );
                PW.println("    jmp faux_if_" + n_if);
                break;
        }
        PW.println("vrai_if_" + n_if + " :");
        parcourArbre(a.getFd().getFg());
        PW.println("faux_if_" + n_if + " :");
        parcourArbre(a.getFd().getFd());
    }

    //Finals
    private void ecrireIdent(Arbre a) {
        PW.println("    mov eax, " + a.getVal());
    }
    private void ecrireEntier(Arbre a) {
        PW.println("    mov eax, " + a.getVal());
    }


}
