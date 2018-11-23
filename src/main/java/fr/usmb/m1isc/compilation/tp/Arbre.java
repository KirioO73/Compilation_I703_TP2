package fr.usmb.m1isc.compilation.tp;

public class Arbre {

    private NoeudType type;
    private String val;
    private Arbre fg;
    private Arbre fd;

    Arbre(NoeudType type, String val, Arbre fg, Arbre fd ){
        this.type = type;
        this.val = val;
        this.fd = fd;
        this.fg = fg;
    }

    void AfficheArbre(){
        System.out.print("( " + val + " ");
        fg.AfficheArbre();
        fd.AfficheArbre();
        System.out.print(" )");
    }
}
