package jeu.listeners;

import jeu.events.PlayEvent;

public interface PlayListener extends GameListener {
    public void play(PlayEvent e);
}