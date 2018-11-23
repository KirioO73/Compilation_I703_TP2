package fr.usmb.m1isc.compilation.tp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

public class codeGenerator {
    private Arbre A;
    private List<String> variables;
    private PrintWriter PW;

    codeGenerator( Arbre A, String Path) throws FileNotFoundException {
        this.A = A;
        PW = new PrintWriter(new FileOutputStream(Path));
        genereData();
        genereCode();
    }

    void genereData(){
        PW.write("DATA SEGMEN");
        // TODO : Get all data from arbre
        PW.write("DATA ENDS");
    }

    void genereCode(){
        PW.write("CODE SEGMENT");
        // TODO : Get all codes from arbre
        PW.write("CODE ENDS");
    }
}
