package jeu.exceptions;

import jeu.Jeu;

public class ChoiceException extends GameException {
    public ChoiceException() { this("Il n'y a pas de choix possible."); }

    public ChoiceException(String message) {
        super();
        super.setMessage(message);
    }
}
