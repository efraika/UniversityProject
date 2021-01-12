package jeu.plateau.cases;

import jeu.Joueur;

/**
 *
 */

public class CaseLabyrinthe extends Case {

    /**
     *
     */
    private static final long serialVersionUID = -7985409714027022714L;

    private Case destination;

    public CaseLabyrinthe(Case c) {
        super();
        destination = c;
    }

    @Override
    public Case getCase() {
        return destination;
    }

    @Override
    public String toString() {
        return "labyrinthe";
    }
}