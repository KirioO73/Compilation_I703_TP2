package fr.usmb.m1isc.compilation.tp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

class codeGenerator {
    private Arbre A;
    private List<String> variables;
    //private PrintWriter PW;

    codeGenerator( Arbre A, String Path) throws FileNotFoundException {
        this.A = A;
        this.variables = new ArrayList<String>();
        PrintWriter PW = new PrintWriter(new FileOutputStream(Path));
        genereData(PW);
        genereCode(PW);
        PW.close();
    }

    private void genereData(PrintWriter PW){
        PW.println("DATA SEGMENT");
        // TODO : Get all data from arbre
        findLet(A);
        PW.println("DATA ENDS");
    }

    private void findLet(Arbre T) {
        if (T == null) return;
        else {
            if (T.getType() == NoeudType.LET) {
                this.variables.add(T.getFd().getVal());
                findLet(T.getFg());
            } else {
                findLet(T.getFd());
                findLet(T.getFg());
            }
        }
    }

    private void genereCode(PrintWriter PW){
        PW.println("CODE SEGMENT");
        // TODO : Get all codes from arbre
        PW.println("CODE ENDS");
    }
}
