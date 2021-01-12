package jeu.plateau.cases;

import jeu.Joueur;

/**
 *
 */

public class CasePrison extends Case {

    /**
     *
     */
    private static final long serialVersionUID = -1341834908599164419L;

    private Joueur emprisonne;

    public CasePrison() {
        super();
        emprisonne = null;
    }

    @Override
    public void arriveSurCase(Joueur joueur) {
        if (emprisonne == null)
            emprisonne = joueur;
        else
            emprisonne = null;
    }

    @Override
    public boolean peutJouer(Joueur joueur) {
        return joueur != emprisonne;
    }

    @Override
    public String toString() {
        return "prison";
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