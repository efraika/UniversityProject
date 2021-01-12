package jeu.exceptions;

import jeu.Jeu;

public abstract class GameException extends RuntimeException {
    String message;

    protected void setMessage(String s) { message = s; }

    public String toString() { return message; }
}