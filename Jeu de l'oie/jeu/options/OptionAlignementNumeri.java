package jeu.options;

import jeu.Jeu;

public class OptionAlignementNumeri extends Option {
    public OptionAlignementNumeri(Jeu jeu) {
        super.setJeu(jeu);
        super.option = "alignement de 3 pions";
        String[] s = new String[2];
        s[0] = "on joue normalement";
        s[1] = "on rejoue si trois pions sont alignés";
        super.valeurs = s;
        super.setValue(0);
    }
}