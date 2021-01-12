package jeu.listeners;

import jeu.options.questions.Question;

public interface GameListener extends java.util.EventListener {
    public void question(Question e);
}