package jeu.exceptions;

public class QuestionException extends GameException {
    public QuestionException(String s) {
        super();
        super.setMessage(s);
    }
}