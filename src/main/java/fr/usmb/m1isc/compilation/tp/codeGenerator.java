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
                if(!this.variables.contains(a.getFg().getVal())){
                    this.variables.add(a.getFg().getVal());
                    findLet(a.getFg());
                }
                else{
                    findLet(a.getFg());
                }
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

    private int n_if = 0;
    private int n_while = 0;
    private int n_gt = 0;
    private int n_equal = 0;
    private int n_gte = 0;
    private int n_and = 0;
    private int n_or = 0;

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

                case MOINS_UNAIRE:
                    ecrireMoisUnaire(a);
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

                case AND:
                    ecrireAND(a);
                    break;
                case OR:
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
                case MOD:
                    ecrireMod(a);
                    break;

                case IF:
                    ecrireIf(a);
                    break;
                case WHILE:
                    ecrireWhile(a);
                    break;

                default:
                    parcourArbre(a.getFg());
                    parcourArbre(a.getFd());
                    PW.println("Default");
            }
        }
    }

    private void ecrireMoisUnaire(Arbre a) {
    }

    // Input/Output
    private void ecrireOutput(Arbre a) {
        parcourArbre(a.getFg());
        PW.println("    out eax");
    }

    private void ecrireInput(Arbre a) {
        PW.println("    in eax");
    }

    //LET
    private void ecrireLet(Arbre a) {
        parcourArbre(a.getFd());
        PW.println("    mov " + a.getFg().getVal() + ", eax" );
    }


    //Arithmetic
    private void ecrirePlus(Arbre a) {
        parcourArbre(a.getFd());
        PW.println("    push eax");
        parcourArbre(a.getFg());
        PW.println("    pop ebx");
        PW.println("    add eax, ebx");
    }

    private void ecrireMoins(Arbre a) {
        parcourArbre(a.getFd());
        PW.println("    push eax");
        parcourArbre(a.getFg());
        PW.println("    pop ebx");
        PW.println("    sub eax, ebx");
    }

    private void ecrireMul(Arbre a) {
        parcourArbre(a.getFd());
        PW.println("    push eax");
        parcourArbre(a.getFg());
        PW.println("    pop ebx");
        PW.println("    mul eax, ebx");
    }

    private void ecrireDiv(Arbre a) {
        parcourArbre(a.getFd());
        PW.println("    push eax");
        parcourArbre(a.getFg());
        PW.println("    pop ebx");
        PW.println("    div ebx, eax");
        PW.println("    mov eax, ebx");
    }

    private void ecrireMod(Arbre a) {
        parcourArbre(a.getFd());
        PW.println("    push eax");
        parcourArbre(a.getFg());
        PW.println("    pop ebx");
        PW.println("    mov ecx,eax");
        PW.println("    div ecx,ebx");
        PW.println("    mul ecx,ebx");
        PW.println("    sub eax,ecx");
    }

    //Boolean Operators
    private void ecrireEqual(Arbre a) {
        parcourArbre(a.getFg());
        PW.println("    push eax");
        parcourArbre(a.getFd());
        PW.println("    pop ebx");
        PW.println("    sub eax, ebx");
        PW.println("    jz vrai_equal_" + n_equal );
        PW.println("    jmp faux_equal_" + n_equal );
    }

    private void ecrireGT(Arbre a) {
        parcourArbre(a.getFg());
        PW.println("    push eax");
        parcourArbre(a.getFd());
        PW.println("    pop ebx");
        PW.println("    sub eax, ebx");
        PW.println("    jle faux_gt_" + n_gt);
        PW.println("    jmp vrai_gt_" + n_gt );
    }

    private void ecrireGTE(Arbre a) {
        parcourArbre(a.getFg());
        PW.println("    push eax");
        parcourArbre(a.getFd());
        PW.println("    pop ebx");
        PW.println("    sub eax, ebx");
        PW.println("    jl faux_gte_" + n_gte );
        PW.println("    jmp vrai_gte_" + n_gte);
    }

    private void ecrireAND(Arbre a) {
        switch((a.getFg().getType())){
            case EGAL:
                ++n_equal;
                ecrireEqual(a.getFg());
                PW.println("faux_equal_" + n_equal + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz faux_and_"+n_and);
                PW.println("vrai_equal_" + n_equal + " :");
                break;
            case GT:
                ++n_gt;
                ecrireGT(a.getFg());
                PW.println("faux_gt_" + n_gt + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz faux_and_"+n_and);
                PW.println("vrai_gt_" + n_gt + " :");
                break;
            case GTE:
                ++n_gte;
                ecrireGTE(a.getFg());
                PW.println("faux_gte_" + n_gte + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz faux_and_"+n_and);
                PW.println("vrai_gte_" + n_gte + " :");
                //SC FD
                break;
            case AND:
                ++n_and;
                ecrireAND(a.getFg());
                PW.println("faux_and_"+n_and+":");
                PW.println("    mov eax, 0");
                PW.println("    jz faux_and_"+n_and);
                PW.println("vrai_and_"+n_and+" :");
                //switch case sur fd
                break;
            case OR:
                ++n_or;
                ecrireOR(a.getFg());
                PW.println("faux_or_"+n_or+":");
                PW.println("    mov eax, 0");
                PW.println("    jz faux_and_"+n_and);
                PW.println("vrai_or_"+n_or+" :");
                //SC FD
                break;
        }
        switch((a.getFd().getType())){
            case EGAL:
                ++n_equal;
                ecrireEqual(a.getFd());
                PW.println("faux_equal_" + n_equal + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz faux_and_"+n_and);
                PW.println("vrai_equal_" + n_equal + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz vrai_and_"+n_and);
                break;
            case GT:
                ++n_gt;
                ecrireGT(a.getFd());
                PW.println("faux_gt_" + n_gt + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz faux_and_"+n_and);
                PW.println("vrai_gt_" + n_gt + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz vrai_and_"+n_and);
                break;
            case GTE:
                ++n_gte;
                ecrireGTE(a.getFd());
                PW.println("faux_gte_" + n_gte + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz faux_and_"+n_and);
                PW.println("vrai_gte_" + n_gte + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz vrai_and_"+n_and);
                break;
            case AND:
                ++n_and;
                ecrireAND(a.getFd());
                PW.println("faux_and_"+n_and+":");
                PW.println("    mov eax, 0");
                PW.println("    jz faux_and_"+n_and);
                PW.println("vrai_and_"+n_and+" :");
                PW.println("    mov eax, 0");
                PW.println("    jz vrai_and_"+n_and);
                break;
            case OR:
                ++n_or;
                ecrireOR(a.getFd());
                PW.println("faux_or_"+n_and+":");
                PW.println("    mov eax, 0");
                PW.println("    jz faux_and_"+n_and);
                PW.println("vrai_or_"+n_and+" :");
                PW.println("    mov eax, 0");
                PW.println("    jz vrai_and_"+n_and);
                break;
        }
    }

    private void ecrireOR(Arbre a) {
        switch((a.getFg().getType())){
            case EGAL:
                ++n_equal;
                ecrireEqual(a.getFg());
                PW.println("vrai_equal_" + n_equal + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz vrai_or_"+n_or);
                PW.println("faux_equal_" + n_equal + " :");
                break;
            case GT:
                ++n_gt;
                ecrireGT(a.getFg());
                PW.println("vrai_gt_" + n_gt + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz vrai_or_"+n_or);
                PW.println("faux_gt_" + n_gt + " :");
                break;
            case GTE:
                ++n_gte;
                ecrireGTE(a.getFg());
                PW.println("vrai_gte_" + n_gte + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz vrai_or_"+n_or);
                PW.println("faux_gte_" + n_gte + " :");
                break;
            case AND:
                ++n_and;
                ecrireAND(a.getFg());
                PW.println("vrai_and_"+n_and+" :");
                PW.println("    mov eax, 0");
                PW.println("    jz vrai_or_"+n_or);
                PW.println("faux_and_"+n_and+":");
                break;
            case OR:
                ++n_or;
                ecrireOR(a.getFg());
                PW.println("vrai_or_"+n_and+" :");
                PW.println("    mov eax, 0");
                PW.println("    jz vrai_or_"+n_or);
                PW.println("faux_or_"+n_and+":");
                //SC FD
                break;
        }
        switch((a.getFd().getType())){
            case EGAL:
                ++n_equal;
                ecrireEqual(a.getFd());
                PW.println("vrai_equal_" + n_equal + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz vrai_or_"+n_or);
                PW.println("faux_equal_" + n_equal + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz faux_or_"+n_or);
                break;
            case GT:
                ++n_gt;
                ecrireGT(a.getFd());
                PW.println("vrai_gt_" + n_gt + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz vrai_or_"+n_or);
                PW.println("faux_gt_" + n_gt + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz faux_or_"+n_or);
                break;
            case GTE:
                ++n_gte;
                ecrireGTE(a.getFd());
                PW.println("vrai_gte_" + n_gte + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz vrai_or_"+n_or);
                PW.println("faux_gte_" + n_gte + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz faux_or_"+n_or);
                break;
            case AND:
                ++n_and;
                ecrireAND(a.getFd());
                PW.println("vrai_and_"+n_and+" :");
                PW.println("    mov eax, 0");
                PW.println("    jz vrai_or_"+n_or);
                PW.println("faux_and_"+n_and+":");
                PW.println("    mov eax, 0");
                PW.println("    jz faux_or_"+n_or);
                break;
            case OR:
                ++n_or;
                ecrireOR(a.getFd());
                PW.println("vrai_or_"+n_and+" :");
                PW.println("    mov eax, 0");
                PW.println("    jz vrai_or_"+n_or);
                PW.println("faux_or_"+n_and+":");
                PW.println("    mov eax, 0");
                PW.println("    jz faux_or_"+n_or);
                break;
        }
    }


    // Conditionals
    private void ecrireIf(Arbre a) {
        ++n_if;
        switch(a.getFg().getType()){
            case EGAL:
                ++n_equal;
                ecrireEqual(a.getFg());
                PW.println("faux_equal_" + n_equal + " :");
                parcourArbre(a.getFd().getFg());
                PW.println("    mov eax, 0");
                PW.println("    jz fin_if_"+n_if);
                PW.println("vrai_equal_" + n_equal + " :");
                parcourArbre(a.getFd().getFd());
                break;
            case GT:
                ++n_gt;
                ecrireGT(a.getFg());
                PW.println("faux_gt_" + n_gt + " :");
                parcourArbre(a.getFd().getFg());
                PW.println("    mov eax, 0");
                PW.println("    jz fin_if_"+n_if);
                PW.println("vrai_gt_" + n_gt + " :");
                parcourArbre(a.getFd().getFd());
                break;
            case GTE:
                ++n_gte;
                ecrireGTE(a.getFg());
                PW.println("faux_gte_" + n_gte + " :");
                parcourArbre(a.getFd().getFg());
                PW.println("    mov eax, 0");
                PW.println("    jz fin_if_"+n_if);
                PW.println("vrai_gte_" + n_gte + " :");
                parcourArbre(a.getFd().getFd());
                break;
            case AND:
                ++n_and;
                ecrireAND(a.getFg());
                PW.println("faux_and_" + n_and + " :");
                parcourArbre(a.getFd().getFg());
                PW.println("    mov eax, 0");
                PW.println("    jz fin_if_"+n_if);
                PW.println("vrai_and_" + n_and + " :");
                parcourArbre(a.getFd().getFd());
                break;
            case OR:
                ++n_or;
                ecrireOR(a.getFg());
                PW.println("faux_or_" + n_or + " :");
                parcourArbre(a.getFd().getFg());
                PW.println("    mov eax, 0");
                PW.println("    jz fin_if_"+n_if);
                PW.println("vrai_or_" + n_or + " :");
                parcourArbre(a.getFd().getFd());
                break;
        }
        PW.println("fin_if_"+n_if+" :");
    }

    private void ecrireWhile(Arbre a) {
        ++n_while;
        PW.println("debut_while_"+n_while+" :");
        switch(a.getFg().getType()){
            case EGAL:
                ++n_equal;
                ecrireEqual(a.getFg());
                PW.println("faux_equal_" + n_equal + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz fin_while_"+n_while);
                PW.println("vrai_equal_" + n_equal + " :");
                parcourArbre(a.getFd());
                break;
            case GT:
                ++n_gt;
                ecrireGT(a.getFg());
                PW.println("faux_gt_" + n_gt + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz fin_while_"+n_while);
                PW.println("vrai_gt_" + n_gt + " :");
                parcourArbre(a.getFd());
                break;
            case GTE:
                ++n_gte;
                ecrireGTE(a.getFg());
                PW.println("faux_gte_" + n_gte + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz fin_while_"+n_while);
                PW.println("vrai_gte_" + n_gte + " :");
                parcourArbre(a.getFd());
                break;
            case AND:
                ++n_and;
                ecrireAND(a.getFg());
                PW.println("faux_and_" + n_and + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz fin_while_"+n_while);
                PW.println("vrai_and_" + n_and + " :");
                parcourArbre(a.getFd());
                break;
            case OR:
                ++n_or;
                ecrireOR(a.getFg());
                PW.println("faux_or_" + n_or + " :");
                PW.println("    mov eax, 0");
                PW.println("    jz fin_while_"+n_while);
                PW.println("vrai_or_" + n_or + " :");
                parcourArbre(a.getFd());
                break;
        }
        PW.println("    jmp debut_while_"+n_while);
        PW.println("fin_while_"+ n_while+" :");
    }

    //Finals
    private void ecrireIdent(Arbre a) {
        PW.println("    mov eax, " + a.getVal());
    }
    private void ecrireEntier(Arbre a) {
        PW.println("    mov eax, " + a.getVal());
    }


}
