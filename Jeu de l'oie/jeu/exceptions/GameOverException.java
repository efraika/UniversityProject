package jeu.exceptions;

import jeu.Jeu;

public class GameOverException extends GameException {
    public GameOverException() { this("Le jeu est fini."); }

    public GameOverException(String m) {
        super();
        super.setMessage(m);
    }
}