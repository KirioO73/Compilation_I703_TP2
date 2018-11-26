package fr.usmb.m1isc.compilation.tp;

public enum NoeudType {

    ENTIER,

    // logique

    AND,
	OR,
    EGAL,
    IF,
    ACTION, // then ou else

    //comparateurs

    GT,       // "<"
    GTE,      // "<="

    //operateurs

    PLUS,
    MOINS,
    MOINS_UNAIRE,
    MUL,
    DIV,
    MOD,

    // autre
    OUTPUT,
    INPUT,
    WHILE,
    LET,
    NIL,
    IDENT,
    SEMI;
}
