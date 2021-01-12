package jeu.affichage.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import jeu.options.Option;
import jeu.options.questions.Question;

public class JPanelUp extends JPanel {

    protected final Fenetre parent;
    private final BufferedImage img;

    public JPanelUp(Fenetre parent) {
        this.parent = parent;
        BufferedImage i;
        try {
            i = ImageIO.read(new File("assets/menu.JPG"));
        } catch (IOException e) {
            i = null;
        }
        img = i;
    }

    public void goTo() { // comportement par défaut à l'arrivée sur la page
        parent.setMinimumSize(new Dimension(600, 428));
    }

    @Override
    public void paintComponent(Graphics g) {
        if (parent.estAffiche(this)) {
            super.paintComponent(g);
            setSize(new Dimension(parent.getWidth(), parent.getHeight()));
            int w = 129;
            int h = 92;
            if (getWidth() > w) {
                h = h * getWidth() / w;
                w = getWidth();
            }
            if (getHeight() > h) {
                w = w * getHeight() / h;
                h = getHeight();
            }

            g.drawImage(img, getWidth() / 2 - w / 2, getHeight() / 2 - h / 2, w,
                        h, this);
        }
    }
}