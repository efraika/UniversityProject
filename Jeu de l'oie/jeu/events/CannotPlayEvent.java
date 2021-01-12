package jeu.events;

import jeu.Jeu;
import jeu.Joueur;
import jeu.plateau.cases.Case;

public class CannotPlayEvent extends GameEvent {
    public Joueur getJoueur() { return joueur; }

    private Joueur joueur;
    private Case c;

    public CannotPlayEvent(Jeu jeu, Joueur joueur, Case c) {
        super(jeu);
        this.joueur = joueur;
    }

    public CannotPlayEvent(Jeu jeu, Joueur joueur) { this(jeu, joueur, null); }

    public String toString() {
        return joueur + " ne peut pas jouer !" +
            ((c == null) ? "" : " (case " + c + ")");
    }
}
