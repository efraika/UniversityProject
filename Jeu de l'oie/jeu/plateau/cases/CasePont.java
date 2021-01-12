package jeu.plateau.cases;

import jeu.Joueur;

/**
 *
 */

public class CasePont extends Case {

    /**
     *
     */
    private static final long serialVersionUID = 9079542156062507379L;

    private Case destination;

    public CasePont(Case c) {
        super();
        destination = c;
    }

    @Override
    public Case getCase() {
        return destination;
    }

    @Override
    public String toString() {
        return "pont";
    }
}