package jeu.affichage.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;
import jeu.options.Option;

public class JRadioButtonUp extends JRadioButton {
    public JRadioButtonUp(String s, boolean selected, Option o, int numero) {
        super(s, selected);
        addActionListener(event -> {
            if (isSelected()) {
                o.setValue(numero);
            }
        });
    }
}