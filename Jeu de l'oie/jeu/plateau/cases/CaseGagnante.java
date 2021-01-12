package jeu.plateau.cases;

import jeu.Joueur;

/**
 *
 */

public class CaseGagnante extends Case {

    /**
     *
     */
    private static final long serialVersionUID = -1652475914965460316L;

    public CaseGagnante() { super(); }

    @Override
    public String toString() {
        return "victoire";
    }

    @Override
    public boolean estFinale() {
        return true;
    }

    @Override
    public boolean peutJouer(Joueur joueur) {
        return false;
    }

    @Override
    public boolean willPlay(Joueur j) {
        return false;
    }
}