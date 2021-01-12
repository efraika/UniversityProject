package jeu.affichage.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import jeu.plateau.cases.Case;

public class CasePanel extends JPanel implements Iterable<PionG> {

    private final Image background;

    public static int cote = 0;
    private final boolean affScore;
    private final int numero;
    private final int score;

    private final CopyOnWriteArrayList<PionG> pions;

    public static final int MINIMUM = 100; // taille minimale d'une case

    private final Case c;

    public static final int NATURE_NUMBER = 11;

    public CasePanel(Case c, int numero, int numeroBis) {
        super();
        this.c = c;
        setOpaque(false);
        pions = new CopyOnWriteArrayList<PionG>();
        String nom = c.toString();
        this.numero = numero;
        setMinimumSize(new Dimension(MINIMUM, MINIMUM));
        if (nom.startsWith("score")) {
            affScore = true;
            score = numeroBis;
            Random rand = new Random();
            int n = rand.nextInt(NATURE_NUMBER) + 1;
            background =
                (new ImageIcon("assets/cases/nature" + n + ".png")).getImage();
        } else if (nom.equals("normale")) {
            Random rand = new Random();
            int n = rand.nextInt(NATURE_NUMBER) + 1;
            affScore = false;
            score = 0;
            background =
                (new ImageIcon("assets/cases/nature" + n + ".png")).getImage();
        } else if (numero != numeroBis) { // pont et labyrinthe
            affScore = true;
            score = numeroBis;
            background =
                (new ImageIcon("assets/cases/" + nom + ".png")).getImage();
        } else {
            affScore = false;
            score = 0;
            background = (new ImageIcon(
                              "assets/cases/" +
                              nom.replace("é", "e").replace("ô", "o") + ".png"))
                             .getImage();
        }
    }

    public Iterator<PionG> iterator() { return pions.iterator(); }

    public Case getCase() { return c; }

    public void add(PionG p) {
        if (pions.add(p))
            this.repaint();
    }

    public PionG remove(PionG p) {
        if (pions.remove(p))
            this.repaint();
        return p;
    }

    public boolean contains(PionG p) { return pions.contains(p); }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        g.drawString("" + numero, getWidth() - 15,
                     getHeight() -
                         5); // numéro de la case affiché en bas à gauche
        if (affScore)
            g.drawString("(" + score + ")", getWidth() - 22,
                         10); // score affiché en haut à droite

        int n = 0;
        for (PionG p : pions) {
            if (n == 0) {
                p.paint(g, 0, 0, getHeight() / 2);
            } else {
                p.paint(g, 0, n * getHeight() / (2 * (pions.size() - 1)),
                        getHeight() / 2);
            }
            n++;
        }
    }
}