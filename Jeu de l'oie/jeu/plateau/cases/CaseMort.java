package jeu.plateau.cases;

import jeu.Joueur;

/**
 *
 */

public class CaseMort extends CaseLabyrinthe {

    /**
     *
     */
    private static final long serialVersionUID = -6125234066920613090L;

    public CaseMort() { super(null); }

    @Override
    public String toString() {
        return "mort";
    }
}