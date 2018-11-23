package fr.usmb.m1isc.compilation.tp;

class Arbre {

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
        System.out.print( " " +  val + " " );
        if (fg != null)
            fg.AfficheArbre();
        else
            System.out.print(" ");
        if (fd != null)
            fd.AfficheArbre();
        else
            System.out.print(" ");
        //System.out.print(" )");
    }

    NoeudType getType() {
        return type;
    }

    String getVal() {
        return val;
    }

    Arbre getFg() {
        return fg;
    }

    Arbre getFd() {
        return fd;
    }
}
