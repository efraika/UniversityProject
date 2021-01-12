package jeu.plateau;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import jeu.plateau.cases.*;

/**
 *
 *
 */

public class Plateau implements Serializable, Iterable<Case> {

    private static final long serialVersionUID = 2520277206423394352L;
    private final Case[] cases;

    private Plateau(int i) { cases = new Case[i]; }
    private Plateau(Case[] c) { cases = c; }

    public Case getCase(int i) { return cases[i]; }

    public int getCase(Case c) {
        int i = 0;
        for (Case cc : cases) {
            if (cc == c)
                break;
            i++;
        }
        return i;
    }

    public Iterator<Case> iterator() {
        return (new ArrayList<Case>(Arrays.asList(cases))).iterator();
    }

    public int size() { return cases.length; }

    public static final Plateau getDefaultOie() {
        Case[] c = new Case[63];
        c[0] = new CaseDepart();
        for (int i = 1; i < c.length; i++) {
            if (i == 6)
                c[i] = null;
            else if (i == 62)
                c[i] = new CaseGagnante();
            else if (i == 18)
                c[i] = new CaseHotel();
            else if (i == 30)
                c[i] = new CasePuit();
            else if (i == 41)
                c[i] = new CaseLabyrinthe(c[29]);
            else if (i == 51)
                c[i] = new CasePrison();
            else if (i == 57)
                c[i] = new CaseMort();
            else if ((i + 1) % 9 == 0)
                c[i] = new CaseOie();
            else
                c[i] = new Case();
        }
        c[6] = new CasePont(c[11]);
        return new Plateau(c);
    }
    public static final Plateau getDefaultNumeri() {
        int[] t = {0, -3, -3, -3, -2, -2, -1, -1, 0,  0, 1,  0,  0, 0,
                   2, 0,  3,  0,  4,  0,  5,  6,  0,  0, 7,  0,  0, 8,
                   0, 9,  10, 0,  11, 12, 0,  13, 15, 0, 20, 25, 30};
        Case[] c = new Case[t.length];
        c[0] = new CaseDepart();
        for (int i = 1; i < t.length; i++) {
            if (t[i] == 0)
                c[i] = new Case();
            else
                c[i] = new CaseScore(t[i]);
        }
        return new Plateau(c);
    }

    public Case getLast() { return this.cases[cases.length - 1]; }

    public void recommencer() {
        for (Case c : cases)
            c.recommencer();
    }

    public void unTourPasse() {
        for (Case c : this)
            c.unTourPasse();
    }
}