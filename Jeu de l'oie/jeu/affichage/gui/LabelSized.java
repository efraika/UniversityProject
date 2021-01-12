package jeu.affichage.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JTextArea;

public class LabelSized extends JTextArea {

    private final Fenetre fenetre;
    public LabelSized(String label, Fenetre parent) {
        super(label, 10, 1);
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setEditable(false);
        this.setVisible(true);
        this.fenetre = parent;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setMinimumSize(new Dimension(4 * fenetre.getWidth() / 10, 5));
        this.setMaximumSize(
            new Dimension(4 * fenetre.getWidth() / 10, fenetre.getHeight()));
    }
}