package jeu.options;

import jeu.Jeu;

public class OptionFaceSuppNumeri extends Option {
    public OptionFaceSuppNumeri(Jeu j) {
        super.setJeu(j);
        super.option = "possibilite de tirer 0 au dé";
        String[] s = new String[2];
        s[0] = "on tire un nombre entre 1 et 6";
        s[1] =
            "on tire un nombre entre 0 et 6 : tirer 0 permet de reculer un pion ennemi avant de rejouer";
        super.valeurs = s;
        super.setValue(0);
    }
}