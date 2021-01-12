package jeu.affichage.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;
import jeu.Jeu;
import jeu.Joueur;
import jeu.affichage.Affichage;
import jeu.affichage.AffichageGUI;
import jeu.affichage.AffichageGUI.JeuModifiedStateListener;
import jeu.affichage.AffichagePlateau;
import jeu.events.CannotPlayEvent;
import jeu.events.GameEvent;
import jeu.events.GameOverEvent;
import jeu.events.PlayEvent;
import jeu.options.Option;
import jeu.plateau.Plateau;
import jeu.plateau.cases.Case;

public class JeuPanel extends JSplitPane implements JeuModifiedStateListener {

    private JPanel plateauIn;
    private JScrollPane plateau;
    private JLabel tourLabel;
    private JLabel joueurLabel;
    private JScrollPane joueursListe;
    private JPanel liste;

    private JButton de;
    private JLabel desImage1;
    private JLabel desImage2;

    private final Fenetre parent;
    private final Affichage affichage;
    private int formerWidth;
    private int numTour;

    public JeuPanel(Affichage affichage, Fenetre parent) {
        super(JSplitPane.HORIZONTAL_SPLIT);

        plateauIn = new JPanel();
        this.affichage = affichage;

        plateau = new JScrollPane(
            plateauIn, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        plateau.setOpaque(false);
        plateauIn.setOpaque(false);
        JPanel infos = new JPanel();

        addImpl(infos, JSplitPane.LEFT, 1);
        addImpl(plateau, JSplitPane.RIGHT, 2);

        /* LE PANNEAU INFO */
        infos.setLayout(new GridBagLayout());
        Box box = Box.createVerticalBox();
        infos.add(box, new GridBagConstraints());
        infos.setMinimumSize(new Dimension(150, parent.getHeight()));
        infos.setMaximumSize(new Dimension(150, parent.getHeight()));

        /* BOUTON MENU */
        ButtonUp menu =
            new ButtonUp("MENU", true, true, event -> parent.fireGoToMenu());
        Box box1 = Box.createHorizontalBox();
        box1.add(menu);
        box.add(box1);

        /* LE CONTENU DU TITRE */
        JPanel titre = new JPanel();
        box1 = Box.createHorizontalBox();
        box1.add(titre);
        box.add(box1);

        tourLabel = new JLabel();
        joueurLabel = new JLabel();
        box1 = Box.createVerticalBox();
        Box box2 = Box.createHorizontalBox();
        box2.add(tourLabel);
        box1.add(box2);
        box2 = Box.createHorizontalBox();
        box2.add(joueurLabel);
        box1.add(box2);
        titre.setLayout(new GridBagLayout());
        titre.add(box1, new GridBagConstraints());

        /* LA LISTE DE JOUEURS */
        liste = new JPanel();
        joueursListe = new JScrollPane(
            liste, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        box1 = Box.createHorizontalBox();
        box1.add(joueursListe);
        box.add(box1);

        /* LES DES */
        /* IMAGES DES */
        JPanel desImages = new JPanel();
        desImages.setMinimumSize(new Dimension(160, 80));
        desImages.setPreferredSize(new Dimension(160, 80));
        desImages.setMaximumSize(new Dimension(160, 80));
        desImages.setLayout(new GridLayout(1, 2));
        desImage1 = new JLabel();
        desImages.add(desImage1);
        desImage1.setMinimumSize(new Dimension(80, 80));
        desImage1.setPreferredSize(new Dimension(80, 80));
        desImage1.setMaximumSize(new Dimension(80, 80));
        desImage2 = new JLabel();
        desImages.add(desImage2);
        desImage2.setMinimumSize(new Dimension(80, 80));
        desImage2.setPreferredSize(new Dimension(80, 80));
        desImage2.setMaximumSize(new Dimension(80, 80));

        /* BOUTON DES */
        de = new JButton("Lancer les dés !");
        box1 = Box.createHorizontalBox();
        box1.add(de);
        box.add(box1);
        box.add(desImages);
        de.setForeground(Color.BLACK);
        de.setHorizontalAlignment(SwingConstants.CENTER);
        de.setHorizontalTextPosition(SwingConstants.CENTER);
        de.addActionListener(event -> {
            de.setEnabled(false);
            Jeu jeu = affichage.getJeu();
            jeu.lancerDes();
            while (jeu.choix() &&
                   !jeu.choix(parent.ask(
                       jeu.getChoix()))) // tant que la réponse au choix n'est
                                         // pas valide, on demande une réponse
                parent.display("Entrée invalide !");
            jeu.jouer();

            rePaintPawns();
            paintScores();

            if (!jeu.estFini()) {
                de.setEnabled(true);
                joueurLabel.setText(jeu.joueurEnTrainDeJouer().toString());
                if (numTour != jeu.getNumeroDuTour()) {
                    numTour = jeu.getNumeroDuTour();
                    tourLabel.setText("Tour " + numTour);
                    if (parent.position())
                        parent.display("Positions des joueurs :\n" +
                                       getPositions());
                }
            }
            paintScores();
        });

        this.setDividerLocation(165);
        this.parent = parent;
        formerWidth = parent.getWidth();
    }

    public void modifiedState(Jeu jeu) { // appelée lorsque le jeu est modifié :
                                         // une partie a été chargée ou créée
        desImage1.setIcon(null);
        desImage2.setIcon(null);

        joueurLabel.setText("");
        tourLabel.setText("");

        liste.removeAll();
        liste.revalidate();

        plateauIn.removeAll();
        plateauIn.revalidate();

        if (jeu != null) {
            de.setEnabled(true);
            numTour = jeu.getNumeroDuTour();
            tourLabel.setText("Tour " + jeu.getNumeroDuTour());
            joueurLabel.setText(jeu.joueurEnTrainDeJouer().toString());

            // on affiche la liste des joueurs
            liste.setLayout(new GridBagLayout());
            Box boite = Box.createVerticalBox();
            boite.setOpaque(false);
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.weightx = 1;
            constraints.weighty = 0;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.fill = constraints.BOTH;
            liste.add(boite, constraints);

            int n = 0;
            ArrayList<JLabel> tmp = new ArrayList<JLabel>();
            int cote = 120;
            joueursListe.setMaximumSize(
                new Dimension(140, parent.getHeight() - 200));
            joueursListe.setMinimumSize(new Dimension(
                140, ((parent.getHeight() - 200 > jeu.nombreDeJoueurs * 135)
                          ? jeu.nombreDeJoueurs * 135
                          : parent.getHeight() - 200)));
            for (Joueur joueur : jeu) {
                ImageIcon i =
                    new ImageIcon("assets/pions/pion" + (n % 18 + 1) + ".png");
                JLabel l;
                if (i.getIconWidth() > i.getIconHeight())
                    l = new JLabel(new ImageIcon(i.getImage().getScaledInstance(
                        cote, i.getIconHeight() * cote / i.getIconWidth(),
                        Image.SCALE_DEFAULT)));
                else
                    l = new JLabel(new ImageIcon(i.getImage().getScaledInstance(
                        i.getIconWidth() * cote / i.getIconHeight(), cote,
                        Image.SCALE_DEFAULT)));
                tmp.add(l);
                l.setText(joueur.toString());
                l.setHorizontalTextPosition(JLabel.CENTER);
                l.setVerticalTextPosition(JLabel.BOTTOM);
                l.setVisible(true);
                l.setMinimumSize(new Dimension(135, cote + 15));
                l.setMaximumSize(new Dimension(135, cote + 15));
                l.setPreferredSize(new Dimension(135, cote + 15));
                Box b = Box.createHorizontalBox();
                b.add(l);
                boite.add(b);
                n++;
            }
            joueursLabels = new CopyOnWriteArrayList<JLabel>(tmp);

            /* On crée les cases du plateau */
            ArrayList<CasePanel> tmp2 = new ArrayList<CasePanel>();
            n = 1;
            for (Case c : jeu.getPlateau()) {
                int i;
                if (c.getScore() != 0)
                    i = c.getScore();
                else if (c.getCase() == null) {
                    i = 1;
                } else {
                    i = jeu.getCase(c.getCase()) + 1;
                }
                tmp2.add(new CasePanel(c, n, i));
                n++;
            }
            cases = new CopyOnWriteArrayList<CasePanel>(tmp2);

            remplirPlateau();
            paintPawns();
            paintScores();
        }
    }

    private CopyOnWriteArrayList<JLabel> joueursLabels;
    private CopyOnWriteArrayList<CasePanel>
        cases; // propre à chaque jeu, contient la liste des cases du plateau du
               // jeu

    private void paintScores() {
        int n = 0;
        for (Joueur joueur : affichage.getJeu()) {
            joueursLabels.get(n).setText(joueur.toString() + " (" +
                                         joueur.getScore() + ")");
            n++;
        }
    }

    public void goTo() {
        parent.setMinimumSize(new Dimension(1161, 828));
        parent.getRootPane().setDefaultButton(de);
    }

    private AffichagePlateau affichagePlateau;

    private void remplirPlateau() { affichagePlateau.afficher(); }

    public void setAffichagePlateau(int aff) {
        if (cases != null) {
            plateauIn.setMinimumSize(
                new Dimension(plateau.getWidth(), plateau.getHeight()));
            plateauIn.setPreferredSize(
                new Dimension(plateau.getWidth(), plateau.getHeight()));
            plateauIn.setMaximumSize(
                new Dimension(plateau.getWidth(), plateau.getHeight()));
            for (CasePanel cp : cases) {
                cp.setBorder(null);
                cp.setPreferredSize(
                    new Dimension(CasePanel.MINIMUM, CasePanel.MINIMUM));
                cp.setMaximumSize(null);
                cp.setMinimumSize(
                    new Dimension(CasePanel.MINIMUM, CasePanel.MINIMUM));
            }
        }

        switch (aff) {
        case Fenetre.SPIRALE:
            affichagePlateau = new AffichagePlateauSpiraleGUI();
            break;
        case Fenetre.COLONNE:
            affichagePlateau = new AffichagePlateauColonneGUI();
            break;
        case Fenetre.RECTANGLE:
            affichagePlateau = new AffichagePlateauRectangleGUI();
            break;
        case Fenetre.ZIGZAG:
            affichagePlateau = new AffichagePlateauZigzagGUI();
            break;
        }

        parent.setAffichagePlateau(aff);
        if (affichage.getJeu() != null) {
            remplirPlateau();
        }
    }

    private void paintPawns() {
        Jeu jeu = affichage.getJeu();
        Plateau plateau = jeu.getPlateau();

        for (Joueur joueur : jeu) {
            int n = 0;
            for (Case c : joueur) {
                cases.get(jeu.getCase(c))
                    .add(new PionG(
                        joueur, c,
                        getIcon(joueursLabels, joueur.toString()).getImage(), n,
                        joueur.getNombrePions() > 1));
                n++;
            }
        }
    }

    private void rePaintPawns() {
        // utiliser joueursLabels qui contient les icones des joueurs
        // utiliser cases qui contient la liste des casePanel pour les
        // positionner
        Jeu jeu = affichage.getJeu();
        Plateau plateau = jeu.getPlateau();

        for (CasePanel cp : cases) {
            for (PionG p : cp) {
                p.update(); // on le bouge à sa bonne case
                if (p.getCase() != cp.getCase()) {
                    cp.remove(p);
                    cases.get(jeu.getCase(p.getCase())).add(p);
                }
            }
        }
    }

    private ImageIcon getIcon(CopyOnWriteArrayList<JLabel> liste, String s) {
        for (JLabel jl : liste) {
            if (jl.getText().equals(s))
                return (ImageIcon)jl.getIcon();
        }
        return null;
    }

    class AffichagePlateauSpiraleGUI implements AffichagePlateau {
        @Override
        public void afficher() {
            plateauIn.removeAll();
            plateauIn.revalidate();

            int size = cases.size();
            int l = size;
            int h = size;
            for (int i = 10; i > 5; i--) {
                if (size / i * i == size) {
                    l = i;
                    h = size / i;
                    break;
                } else if (size - size / i * i < l * h - size) {
                    l = i;
                    h = size / i + 1;
                }
            }

            String[] debut = new String[h];
            String[] fin = new String[h];

            for (int n = 0; n < h; n++) {
                debut[n] = "";
                fin[n] = "";
            }

            int[] lignes = new int[h];
            int[] colonnes = new int[l];

            int directionH = 1; // -1 si l'on va à gauche
            int directionV = 0; // -1 si l'on va vers le haut
            int coorH = 0;
            int coorV = 0;

            StringBuilder sb = new StringBuilder();
            for (int n = 0; n < l * h; n++) {
                boolean[] bordures =
                    new boolean[4]; // haut, gauche, bas, droite
                bordures[0] = false;
                bordures[1] = false;
                bordures[2] = false;
                bordures[3] = false;
                int formerDirectionH = directionH;
                int formerDirectionV = directionV;
                String s;
                if (n < size)
                    s = n + " ";
                else
                    s = -1 + " ";

                if (directionH == 1 || directionV == -1)
                    debut[coorV] = debut[coorV] + s;
                else // (directionH==-1 || directionV==-1)
                    fin[coorV] = s + fin[coorV];

                lignes[coorV]++;
                colonnes[coorH]++;

                if (lignes[coorV] == l && directionV == 0) {
                    if (directionH == 1) {
                        directionV = 1;
                        bordures[0] = true;
                        bordures[3] = true;
                    } else {
                        directionV = -1;
                        bordures[1] = true;
                        bordures[2] = true;
                    }
                    directionH = 0;
                } else if (colonnes[coorH] == h && directionH == 0) {
                    if (directionV == 1) {
                        directionH = -1;
                        bordures[2] = true;
                        bordures[3] = true;
                    } else {
                        directionH = 1;
                        bordures[0] = true;
                        bordures[1] = true;
                    }
                    directionV = 0;
                } else {
                    if (directionV == 0) {
                        bordures[0] = true;
                        bordures[2] = true;
                    } else {
                        bordures[1] = true;
                        bordures[3] = true;
                    }
                }

                for (boolean b : bordures) {
                    if (formerDirectionH == 1 || formerDirectionV == -1)
                        debut[coorV] = debut[coorV] + b + " ";
                    else
                        fin[coorV] = b + " " + fin[coorV];
                }

                coorH += directionH;
                coorV += directionV;
            }
            for (int n = 0; n < h; n++)
                sb.append(debut[n] + fin[n]);

            Scanner sc = new Scanner(
                sb.toString()); // on va lire les numéros obtenus dans l'ordre

            plateauIn.setLayout(new GridLayout(h, l));
            int n = 0;
            while (sc.hasNextInt() || sc.hasNextBoolean()) {
                int i;
                boolean[] b = new boolean[4];
                if (sc.hasNextInt()) {
                    i = sc.nextInt();
                    for (int m = 0; m < 4; m++) {
                        b[m] = sc.nextBoolean();
                    }
                } else {
                    for (int m = 3; m >= 0; m--) {
                        b[m] = sc.nextBoolean();
                    }
                    i = sc.nextInt();
                }

                if (i == -1) {
                    Container c = new Container();
                    c.setVisible(false);
                    plateauIn.add(c);
                } else {
                    CasePanel cp = cases.get(i);
                    cp.setBorder(BorderFactory.createMatteBorder(
                        (b[0] ? 1 : 0), (b[1] ? 1 : 0), (b[2] ? 1 : 0),
                        (b[3] ? 1 : 0), Color.BLACK));
                    plateauIn.add(cases.get(i));
                }
            }
        }

        @Override
        public String toString() {
            return "spirale";
        }
    }

    class AffichagePlateauZigzagGUI implements AffichagePlateau {
        @Override
        public void afficher() {
            plateauIn.removeAll();
            plateauIn.revalidate();

            int size = cases.size();
            int w = size;
            int h = size;
            for (int i = 10; i > 5; i--) {
                if (size / i * i == size) {
                    w = i;
                    h = size / i;
                    break;
                } else if (size - size / i * i < w * h - size) {
                    w = i;
                    h = size / i + 1;
                }
            }

            plateauIn.setLayout(new GridLayout(h, w));
            for (int n = 0; n < w * h; n++) {
                if ((n / w) % 2 == 0) {
                    if (n <= size) {
                        plateauIn.add(cases.get(n));
                    } else {
                        Container c = new Container();
                        c.setVisible(false);
                        plateauIn.add(c);
                    }
                } else {
                    if (n <= size) {
                        plateauIn.add(cases.get(n / w * w + w - 1 - (n % w)));
                    } else {
                        Container c = new Container();
                        c.setVisible(false);
                        plateauIn.add(c);
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "zigzag";
        }
    }
    class AffichagePlateauRectangleGUI implements AffichagePlateau {
        @Override
        public void afficher() {
            plateauIn.removeAll();
            plateauIn.revalidate();

            int size = cases.size();
            int w = size;
            int h = size;
            for (int i = 10; i > 5; i--) {
                if (size / i * i == size) {
                    w = i;
                    h = size / i;
                    break;
                } else if (size - size / i * i < w * h - size) {
                    w = i;
                    h = size / i + 1;
                }
            }

            plateauIn.setLayout(new GridLayout(h, w));
            for (CasePanel cp : cases) {
                plateauIn.add(cp);
            }
        }

        @Override
        public String toString() {
            return "rectangle";
        }
    }

    class AffichagePlateauColonneGUI implements AffichagePlateau {
        @Override
        public void afficher() {
            plateauIn.removeAll();
            plateauIn.revalidate();

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.weightx = 1;
            constraints.weighty = 0;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.fill = constraints.BOTH;
            Box boite = Box.createVerticalBox();
            plateauIn.setLayout(new GridBagLayout());
            plateauIn.add(boite, constraints);
            for (CasePanel cp : cases) {
                cp.setPreferredSize(
                    new Dimension(CasePanel.MINIMUM, CasePanel.MINIMUM));
                cp.setMaximumSize(
                    new Dimension(CasePanel.MINIMUM, CasePanel.MINIMUM));
                boite.add(Box.createHorizontalBox().add(cp));
            }
            plateauIn.setMinimumSize(new Dimension(
                CasePanel.MINIMUM,
                affichage.getJeu().getPlateau().size() * CasePanel.MINIMUM));
            plateauIn.setPreferredSize(new Dimension(
                CasePanel.MINIMUM,
                affichage.getJeu().getPlateau().size() * CasePanel.MINIMUM));
            plateauIn.setMaximumSize(new Dimension(
                CasePanel.MINIMUM,
                affichage.getJeu().getPlateau().size() * CasePanel.MINIMUM));
        }

        @Override
        public String toString() {
            return "colonne";
        }
    }

    public void gameOver(GameOverEvent e) {
        de.setEnabled(false);
        parent.display("Partie finie ! " + e.toString() + "\n" +
                       e.getClassement());
    }

    public void cannotPlay(CannotPlayEvent e) {
        if (parent.cantPlay())
            parent.display(e.toString());
    }

    public void play(PlayEvent e) {
        if (parent.mouvement())
            parent.display(e.getJoueur().toString() + " (score " +
                           e.getJoueur().getScore() + ") a fait " + e.getDes() +
                           " :\n" + getPositions(e.getJoueur()));
    }

    private String getPositions(Joueur joueur) {
        StringBuilder sb = new StringBuilder();
        Jeu jeu = affichage.getJeu();
        if (joueur.getNombrePions() == 1)
            sb.append("case " + (jeu.getCase(joueur.getCase()) + 1) +
                      " (case " + joueur.getCase() + ")");
        else {
            int i = 1;
            for (Case c : joueur) {
                sb.append("pion " + i + " : " + (jeu.getCase(c) + 1) +
                          " (case " + c + "). ");
                i++;
            }
        }
        return sb.toString();
    }

    private String getPositions() {
        StringBuilder sb = new StringBuilder();
        Jeu jeu = affichage.getJeu();
        for (Joueur joueur : jeu)
            sb.append(joueur + " (score " + joueur.getScore() +
                      ") : " + getPositions(joueur) + "\n");
        return sb.toString();
    }

    public void changeDesValue(int[] i) {
        desImage1.setIcon(new ImageIcon("assets/des/" + i[0] + ".png"));

        if (i.length == 2)
            desImage2.setIcon(new ImageIcon("assets/des/" + i[1] + ".png"));
        else
            desImage2.setIcon(null);
    }

    @Override
    public void paintComponent(Graphics g) {
        if (parent.estAffiche(this)) {
            super.paintComponent(g);
            setSize(new Dimension(parent.getWidth(), parent.getHeight()));
        }
    }
}