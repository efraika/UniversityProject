package jeu.exceptions;

import jeu.Jeu;
import jeu.options.Option;

public class WrongOptionException extends OptionException {
    public WrongOptionException(Class<? extends Option> option, int valeur) {
        super("L'option " + option.getName() +
              " n'a pas pour valeur possible " + valeur + ".");
    }
}