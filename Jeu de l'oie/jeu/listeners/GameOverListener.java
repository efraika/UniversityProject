package jeu.listeners;

import jeu.events.GameOverEvent;

public interface GameOverListener extends GameListener {
    public void gameOver(GameOverEvent e);
}