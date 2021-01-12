package jeu.affichage.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class ButtonUp extends JButton {
    public ButtonUp(String etiquette, boolean visible, boolean enabled,
                    ActionListener listener) {
        super(etiquette);
        setForeground(Color.BLACK);
        setVisible(visible);
        setEnabled(enabled);
        setHorizontalAlignment(SwingConstants.CENTER);
        setHorizontalTextPosition(SwingConstants.CENTER);
        addActionListener(listener);
        setOpaque(false);
        setContentAreaFilled(true);
        setBorderPainted(true);
        setFocusPainted(true);
    }

    public void addToBox(Box box, boolean vertical, int w, int h) {
        Box box1;
        if (vertical)
            box1 = Box.createVerticalBox();
        else
            box1 = Box.createHorizontalBox();
        box1.add(this);
        box.add(box1);
        if (w != 0 || h != 0)
            addEmptyBox(box, w, h);
    }

    public void addToBox(Box box, boolean vertical) {
        addToBox(box, vertical, 0, 0);
    }

    public void addEmptyBox(Box box, int w, int h) {
        box.add(Box.createRigidArea(new Dimension(w, h)));
    }
}
