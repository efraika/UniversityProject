package jeu.options;

import jeu.Jeu;

public class OptionQuestionOie extends Option {
    public OptionQuestionOie(Jeu jeu) {
        super.setJeu(jeu);
        super.option = "pose d'une question à la fin du tour";
        String[] s = new String[2];
        s[0] =
            "pas de question posée à la fin du tour (la partie finit quand tous les joueurs ont fini)";
        s[1] =
            "une question est posée au joueur (la partie finit dès qu'un joueur finit, pas d'IA possible)";
        super.valeurs = s;
        super.setValue(0);
    }
}