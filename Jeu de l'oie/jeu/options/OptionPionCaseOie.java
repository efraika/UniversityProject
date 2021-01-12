package jeu.options;

import jeu.Jeu;

public class OptionPionCaseOie extends Option {
    public OptionPionCaseOie(Jeu j) {
        super.setJeu(j);
        super.option = "arrivée d'un pion sur une case occupée";
        String[] s = new String[3];
        s[0] = "rien ne se passe (plusieurs pions sont sur la même case)";
        s[1] = "les deux pions échangent de place";
        s[2] = "le pion qui arrive recule jusqu'à une case libre";
        super.valeurs = s;
        super.setValue(0);
    }
}