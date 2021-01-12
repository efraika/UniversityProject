package jeu.plateau.cases;

import jeu.Joueur;

/**
 *
 */

public class CaseDepart extends Case {

    /**
     *
     */
    private static final long serialVersionUID = -8398217857639434969L;

    public CaseDepart() { super(); }

    @Override
    public String toString() {
        return "départ";
    }

    @Override
    public boolean estInitiale() {
        return true;
    }
}