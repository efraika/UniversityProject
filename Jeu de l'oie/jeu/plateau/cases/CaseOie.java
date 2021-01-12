package jeu.plateau.cases;

import jeu.Joueur;

/**
 *
 */

public class CaseOie extends Case {

    /**
     *
     */
    private static final long serialVersionUID = 5595502758871248475L;

    public CaseOie() { super(); }

    @Override
    public String toString() {
        return "oie";
    }

    @Override
    public boolean moveAgain() {
        return true;
    }
}