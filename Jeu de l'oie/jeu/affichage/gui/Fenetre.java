package jeu.affichage.gui;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import jeu.affichage.Affichage;
import jeu.affichage.AffichageGUI;
import jeu.events.CannotPlayEvent;
import jeu.events.GameEvent;
import jeu.events.GameOverEvent;
import jeu.events.PlayEvent;
import jeu.options.questions.Question;

public class Fenetre extends JFrame {

    private final AffichageGUI affichage;
    private final CardLayout cardLayout;
    private final Container content;

    private final MenuPanel menu;
    private final JeuPanel jeu;
    private final NouveauPanel nouveau;
    private final JPanelUp parametres;
    private final JPanelUp charger;

    private boolean mouvement;
    private boolean position;
    private boolean cantPlay;

    private int aff;

    public static final int SPIRALE = 1;
    public static final int RECTANGLE = 2;
    public static final int COLONNE = 3;
    public static final int ZIGZAG = 4;

    private Container affiche;

    public boolean estAffiche(Container c) { return affiche == c; }

    public void setAffichagePlateau(int aff) {
        if (this.aff != aff) {
            this.aff = aff;
            jeu.setAffichagePlateau(aff);
        }
    }

    public boolean cantPlay() { return cantPlay; }
    public boolean mouvement() { return mouvement; }
    public boolean position() { return position; }
    public int aff() { return aff; }

    public void display(String s) {
        javax.swing.SwingUtilities.invokeLater(
            ()
                -> JOptionPane.showMessageDialog(
                    null, s, "Information", JOptionPane.INFORMATION_MESSAGE));
    }

    public String question(Question q) { return ask(q.getQuestion()); }

    public String ask(String q) {
        return (String)JOptionPane.showInputDialog(
            null, q, "Question", JOptionPane.QUESTION_MESSAGE, null, null, "");
    }

    public Fenetre(AffichageGUI affichage) {
        super();
        this.content = this.getContentPane();
        this.affichage = affichage;
        this.setSize(900, 642);
        this.setTitle("Un super jeu de qualité");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        cardLayout = new CardLayout();

        jeu = new JeuPanel(affichage, this);
        menu = new MenuPanel(affichage, this);
        nouveau = new NouveauPanel(affichage, this);

        /* PAGE DE PARAMETRES */
        ButtonUp menuButton =
            new ButtonUp("MENU", true, true, event -> fireGoToMenu());
        parametres = new JPanelUp(this) {
            public void goTo() {
                Fenetre.this.setMinimumSize(new Dimension(622, 444));
                super.parent.getRootPane().setDefaultButton(menuButton);
            }
        };
        parametres.setLayout(new GridBagLayout());
        Box boite = Box.createVerticalBox();
        parametres.add(boite, new GridBagConstraints());
        /* IDEES DE PARAMETRES
                        - checkbox : afficher les positions des pions de chaque
           joueur à la fin de chaque tour
                        - checkbox : afficher les informations de jeu dès qu'un
           joueur joue ("joueur fait .. et arrive sur la case ..")
                        - checkbox : afficher quand un joueur ne peut pas jouer
                        - radioButton : choisir le type d'affichage
        */
        position = false;
        JCheckBox opt = new JCheckBox(
            "Afficher un message contenant la position des pions de chaque joueur à la fin de chaque tour.",
            position);
        opt.addActionListener(event -> { position = !position; });
        opt.setOpaque(false);
        Box box1 = Box.createHorizontalBox();
        box1.add(opt);
        boite.add(box1);
        boite.add(Box.createRigidArea(new Dimension(5, 10)));

        mouvement = false;
        opt = new JCheckBox(
            "Afficher un message contenant les informations du mouvement dès qu'un joueur joue.",
            mouvement);
        opt.addActionListener(event -> { mouvement = !mouvement; });
        opt.setOpaque(false);
        box1 = Box.createHorizontalBox();
        box1.add(opt);
        boite.add(box1);
        boite.add(Box.createRigidArea(new Dimension(5, 10)));

        cantPlay = false;
        opt = new JCheckBox(
            "Afficher un message quand un joueur ne peut pas jouer.", cantPlay);
        opt.addActionListener(event -> { cantPlay = !cantPlay; });
        opt.setOpaque(false);
        box1 = Box.createHorizontalBox();
        box1.add(opt);
        boite.add(box1);
        boite.add(Box.createRigidArea(new Dimension(5, 10)));

        box1 = Box.createHorizontalBox();
        Box b = Box.createVerticalBox();
        box1.add(b);
        boite.add(box1);

        JLabel j = new JLabel("Type d'affichage du plateau");
        b.add(j);

        setAffichagePlateau(SPIRALE);
        ButtonGroup bg = new ButtonGroup();

        JRadioButton jr = new JRadioButton("Spirale", aff == SPIRALE);
        jr.addActionListener(event -> {
            if (jr.isSelected())
                setAffichagePlateau(SPIRALE);
        });
        jr.setOpaque(false);
        bg.add(jr);
        b.add(jr);

        JRadioButton jr2 = new JRadioButton("Zigzag", aff == ZIGZAG);
        jr2.addActionListener(event -> {
            if (jr2.isSelected())
                setAffichagePlateau(ZIGZAG);
        });
        jr2.setOpaque(false);
        bg.add(jr2);
        b.add(jr2);

        JRadioButton jr3 = new JRadioButton("Rectangle", aff == RECTANGLE);
        jr3.addActionListener(event -> {
            if (jr3.isSelected())
                setAffichagePlateau(RECTANGLE);
        });
        jr3.setOpaque(false);
        bg.add(jr3);
        b.add(jr3);

        JRadioButton jr4 = new JRadioButton("Colonne", aff == COLONNE);
        jr4.addActionListener(event -> {
            if (jr4.isSelected())
                setAffichagePlateau(COLONNE);
        });
        jr4.setOpaque(false);
        bg.add(jr4);
        b.add(jr4);

        boite.add(Box.createRigidArea(new Dimension(5, 10)));
        box1 = Box.createHorizontalBox();
        box1.add(menuButton);
        boite.add(box1);

        /* FIN DE LA PAGE DE PARAMETRES */

        if (affichage.sauvegarde) {
            charger = new JPanelUp(this) {
                public void goTo() {
                    Fenetre.this.setMinimumSize(new Dimension(387, 276));

                    this.removeAll();
                    this.revalidate();

                    this.setLayout(new GridBagLayout());
                    Box boite = Box.createVerticalBox();
                    this.add(boite, new GridBagConstraints());
                    String[] t = affichage.sauvegardes.list();
                    int i = 1;
                    for (String s : t) {
                        if (s.matches(Affichage.REGEX_SAVE)) {
                            boite.add(
                                Box.createHorizontalBox().add(new ButtonUp(
                                    i + ". " + t[i - 1], true, true, event -> {
                                        affichage.setJeu(
                                            affichage.chargerLeJeu(s));
                                        Fenetre.this.fireGoToJeu();
                                    })));
                            boite.add(Box.createRigidArea(new Dimension(5, 5)));
                            i++;
                        }
                    }
                    if (i == 1) {
                        display("Aucune sauvegarde disponible.");
                        Fenetre.this.fireGoToMenu();
                    }
                    boite.add(Box.createRigidArea(new Dimension(5, 20)));
                    boite.add(new ButtonUp("MENU", true, true,
                                           event -> fireGoToMenu()));
                }
            };
        } else {
            charger = null;
        }

        affichage.add(
            jeu); // on doit refaire le panneau jeu lorsque le jeu est modifié

        content.setLayout(cardLayout);

        content.add(menu, "menu");
        content.add(jeu, "jeu");
        content.add(nouveau, "nouveau");
        content.add(charger, "charger");
        content.add(parametres, "parametres");

        this.setVisible(true);

        fireGoToMenu();
    }

    public void fireGoToJeu() {
        affiche = jeu;
        jeu.goTo();
        cardLayout.show(content, "jeu");
    }
    public void fireGoToNouveau() {
        affiche = nouveau;
        nouveau.goTo();
        cardLayout.show(content, "nouveau");
    }
    public void fireGoToParam() {
        affiche = parametres;
        parametres.goTo();
        cardLayout.show(content, "parametres");
    }
    public void fireGoToCharger() {
        affiche = charger;
        charger.goTo();
        if (affiche == charger)
            cardLayout.show(content, "charger");
    }
    public void fireGoToMenu() {
        affiche = menu;
        menu.goTo();
        cardLayout.show(content, "menu");
    }

    public void gameOver(GameOverEvent e) { jeu.gameOver(e); }

    public void cannotPlay(CannotPlayEvent e) { jeu.cannotPlay(e); }

    public void play(PlayEvent e) { jeu.play(e); }

    public void changeDesValue(int[] i) { jeu.changeDesValue(i); }
}