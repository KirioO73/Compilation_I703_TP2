package fr.usmb.m1isc.compilation.tp;

public enum NoeudType {
    ENTIER,
    IDENT,
    // logique
    IF,
    ACTION, // then ou else
    //Boolean operators
    GT,       // "<"
    GTE,      // "<="
    AND,      // "&&"
    OR,       // "||"
    EGAL,     // "==" ou "="
    //Arithmetic operators
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
    SEMI
}
