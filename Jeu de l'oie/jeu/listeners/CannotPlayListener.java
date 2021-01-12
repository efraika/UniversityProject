package jeu.listeners;

import jeu.events.CannotPlayEvent;

public interface CannotPlayListener extends GameListener {
    public void cannotPlay(CannotPlayEvent e);
}