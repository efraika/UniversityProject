package jeu.events;

import jeu.Jeu;
import jeu.Joueur;

public class PlayEvent extends GameEvent {
    private Joueur joueur;
    private int des;

    public PlayEvent(Jeu jeu, Joueur player, int d) {
        super(jeu);
        joueur = player;
        des = d;
    }

    public Joueur getJoueur() { return joueur; }

    public int getDes() { return des; }
}