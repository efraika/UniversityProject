package jeu.affichage.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import jeu.Joueur;
import jeu.plateau.cases.Case;

public class PionG {

    private final Joueur joueur;
    private Case c;
    private final Image image;
    private boolean affNum;
    private int num;

    public PionG(Joueur joueur, Case c, Image image, int num, boolean affNum) {
        this.joueur = joueur;
        this.c = c;
        this.image = image;
        this.affNum = affNum;
        this.num = num;
    }

    public void update() { this.c = joueur.getCase(num); }

    public Image getImage() { return image; }

    public Joueur getJoueur() { return joueur; }

    public void setCase(Case c) { this.c = c; }

    public Case getCase() { return c; }

    public void paint(Graphics g, int x, int y, int size) {
        g.drawImage(image, x, y, size, size, null);
        if (affNum) {
            g.setFont(new Font("Arial", Font.BOLD, size / 2));
            g.setColor(Color.BLACK);
            g.drawString("" + (num + 1), x + size / 3, y + 2 * size / 3);
        }
    }
}