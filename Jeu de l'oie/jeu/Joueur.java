package jeu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import jeu.exceptions.GameException;
import jeu.plateau.cases.Case;
import jeu.plateau.cases.CaseDepart;

/*
 **
 */

public class Joueur
    implements Comparable<Joueur>, Serializable, Iterable<Case> {
    private Pion[] pions;
    private String nom;
    private int score;
    private CaseDepart depart;

    public Case getCase() { return getCase(0); }
    public Case getCase(int i) { return pions[i].getCase(); }

    public void setCase(Case c) { setCase(0, c); }
    public void setCase(int i, Case c) {
        if (c == null)
            c = depart;
        pions[i].setCase(c);
        c.arriveSurCase(this);
    }

    public boolean estSurCase(Case c) {
        for (Case cc : this) {
            if (cc == c)
                return true;
        }
        return false;
    }

    public void incrementerScore() { score++; }

    public Iterator<Case> iterator() {
        ArrayList<Case> a = new ArrayList<Case>();
        for (Pion p : pions)
            a.add(p.getCase());
        return a.iterator();
    }

    public int getScore() { return score; }
    public void setScore(int sc) { score = sc; }

    public void setNom(String nom) { this.nom = nom; }
    public String toString() { return nom; }

    public Joueur(String s) {
        nom = s;
        score = 0;
        pions = null;
        depart = null;
    }

    public Joueur(int i) { this("Joueur " + i); }

    public int compareTo(Joueur j) {
        if (j == null)
            throw new NullPointerException();
        return ((j.getScore() == this.getScore())
                    ? 0
                    : ((j.getScore() > this.getScore()) ? -1 : 1));
    }

    public int getNombrePions() { return pions.length; }

    public void initialiserPionsJoueurs(int nbPionsParJoueur,
                                        CaseDepart depart) {
        pions = new Pion[nbPionsParJoueur];
        this.depart = depart;
        for (int i = 0; i < nbPionsParJoueur; i++)
            pions[i] = new Pion(depart);
    }

    public void recommencer() {
        score = 0;
        for (Pion p : pions)
            p.setCase(depart);
    }

    public boolean peutJouer() { return true; }

    class Pion implements Serializable {
        private Case c;

        void setCase(Case c) { this.c = c; }

        Case getCase() { return c; }

        Pion(CaseDepart c) { this.c = c; }
    }
}