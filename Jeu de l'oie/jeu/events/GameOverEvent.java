package jeu.events;

import jeu.Jeu;

public class GameOverEvent extends GameEvent {
    private String raison;
    private String classement;

    public GameOverEvent(Jeu j, String raison, String classement) {
        super(j);
        this.raison = raison;
        this.classement = classement;
    }

    public String toString() { return raison; }

    public String getClassement() { return classement; }
}