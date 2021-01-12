package jeu.plateau.cases;

import jeu.Joueur;

/**
 */

public class CasePuit extends Case {

    /**
     *
     */
    private static final long serialVersionUID = -4826365605668224665L;

    private Joueur emprisonne;

    public CasePuit() {
        super();
        emprisonne = null;
    }

    @Override
    public void arriveSurCase(Joueur j) {
        emprisonne = j;
    }

    @Override
    public boolean peutJouer(Joueur joueur) {
        return joueur != emprisonne;
    }

    @Override
    public String toString() {
        return "puit";
    }

    @Override
    public void recommencer() {
        emprisonne = null;
    }

    @Override
    public boolean willPlay(Joueur j) {
        return j != emprisonne;
    }
}