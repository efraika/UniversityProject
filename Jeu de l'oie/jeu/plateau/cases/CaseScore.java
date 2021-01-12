package jeu.plateau.cases;

import jeu.Joueur;

public class CaseScore extends Case {

    /**
     *
     */

    private static final long serialVersionUID = -1992087893710853030L;
    private int score;

    public CaseScore(int sc) {
        super();
        score = sc;
    }

    public CaseScore() {
        super();
        score = 0;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "score(" + score + ")";
    }
}
