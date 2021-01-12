package jeu.events;

import java.util.EventObject;
import jeu.Jeu;

public class GameEvent extends EventObject {
    public GameEvent(Jeu jeu) { super(jeu); }
}
