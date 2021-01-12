package jeu.options;

import java.io.Serializable;
import java.util.Arrays;
import jeu.Jeu;
import jeu.exceptions.OptionException;
import jeu.exceptions.WrongOptionException;

public abstract class Option implements Serializable {
    protected String option;
    protected int valeur;
    protected String[] valeurs;
    private Jeu jeu = null;

    protected void setJeu(Jeu j) {
        if (jeu != null)
            throw new OptionException("Le jeu est déjà initialisé.");
        else
            jeu = j;
    }

    public boolean checkJeu(Jeu j) { return jeu == j; }

    public String toString() { return option; }

    public String[] getValues() {
        return Arrays.copyOf(valeurs, valeurs.length);
    }

    public String getValue() { return valeurs[valeur]; }

    public int getIntValue() { return valeur; }

    public int getNombreValue() { return valeurs.length; }

    public void setValue(int i) {
        if (jeu == null)
            throw new OptionException("Le jeu n'a pas été initialisé.");
        else if (jeu.partieCommencee())
            throw new OptionException(
                "On ne peut modifier une option après le début de la partie.");
        if (i < 0 || i >= valeurs.length)
            throw new WrongOptionException(this.getClass(), i);
        else
            valeur = i;
    }
}
