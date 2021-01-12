package jeu.affichage.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import jeu.Jeu;
import jeu.JeuNumeri;
import jeu.JeuOie;
import jeu.affichage.Affichage;
import jeu.options.Option;

public class NouveauPanel extends JPanelUp {

    private final Affichage affichage;

    public NouveauPanel(Affichage affichage, Fenetre parent) {
        super(parent);
        this.affichage = affichage;
    }

    public void goTo() {
        this.removeAll();
        this.revalidate();

        super.parent.setMinimumSize(new Dimension(900, 630));
        // on affiche les deux choix possibles :
        this.setLayout(new GridBagLayout());
        Box page = Box.createVerticalBox();
        Box jeux = Box.createHorizontalBox();
        Box bouton = Box.createHorizontalBox();

        ButtonUp oie = new ButtonUp("Jeu de l'oie", true, true,
                                    event -> choisirJoueurs(true));
        ButtonUp numeri =
            new ButtonUp("Numéri", true, true, event -> choisirJoueurs(false));
        ButtonUp menu = new ButtonUp("Retourner au menu", true, true,
                                     event -> super.parent.fireGoToMenu());

        oie.addToBox(jeux, true, super.parent.getWidth() / 8, 5);
        numeri.addToBox(jeux, true);
        menu.addToBox(page, false);

        bouton.add(menu);
        page.add(jeux);
        page.add(Box.createRigidArea(new Dimension(5, 25)));
        page.add(bouton);

        this.add(page, new GridBagConstraints());

        // description des jeux
        Box descriptions = Box.createHorizontalBox();

        JLabel label = new JLabel("Jeu de l'oie");
        LabelSized labelT = new LabelSized(JeuOie.description, super.parent);
        labelT.setOpaque(false);

        Box oieB = Box.createVerticalBox();
        Box tmp = Box.createHorizontalBox();
        tmp.add(label);
        oieB.add(tmp);
        tmp = Box.createHorizontalBox();
        tmp.add(labelT);
        oieB.add(tmp);

        label = new JLabel("Numéri");
        labelT = new LabelSized(JeuNumeri.description, super.parent);
        labelT.setOpaque(false);

        Box numeriB = Box.createVerticalBox();
        tmp = Box.createHorizontalBox();
        tmp.add(label);
        numeriB.add(tmp);
        tmp = Box.createHorizontalBox();
        tmp.add(labelT);
        numeriB.add(tmp);

        descriptions.add(oieB);
        descriptions.add(Box.createRigidArea(new Dimension(20, 5)));
        descriptions.add(numeriB);

        page.add(descriptions);

        super.parent.getRootPane().setDefaultButton(oie);
        this.repaint();
    }

    private void choisirJoueurs(boolean oie) {
        int min = ((oie) ? JeuOie.getMinimumJoueurs()
                         : JeuNumeri.getMinimumJoueurs());
        int max = ((oie) ? JeuOie.getMaximumJoueurs()
                         : JeuNumeri.getMaximumJoueurs());
        this.removeAll();
        this.revalidate();

        namesPlayer = new ArrayList<JTextField>();
        numberPlayers = 0;

        Box box = Box.createVerticalBox();
        this.add(box, new GridBagConstraints());

        Box titre = Box.createHorizontalBox();
        JLabel jl = new JLabel("Entrez les joueurs (entre " + min + " et " +
                               max + ") :");
        titre.add(jl);
        box.add(titre);
        box.add(Box.createRigidArea(new Dimension(5, 20)));

        Box liste = Box.createVerticalBox();
        box.add(liste);

        JButton add = new JButton("Ajouter un joueur");
        add.addActionListener(event -> {
            if (numberPlayers < max) {
                numberPlayers++;
                Box b = Box.createHorizontalBox();

                JTextField jtf = new JTextField(getNextValidName());
                jtf.setBorder(
                    BorderFactory.createLineBorder(Color.GREEN, 2, true));
                jtf.getDocument().addDocumentListener(new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        if (validName(jtf.getText()) == 1)
                            jtf.setBorder(BorderFactory.createLineBorder(
                                Color.GREEN, 2, true));
                        else
                            jtf.setBorder(BorderFactory.createLineBorder(
                                Color.RED, 2, true));
                        checkValid(min, max);
                    }
                    public void removeUpdate(DocumentEvent e) {
                        changedUpdate(e);
                    }
                    public void insertUpdate(DocumentEvent e) {
                        changedUpdate(e);
                    }
                });

                JButton jb = new JButton(new ImageIcon(
                    new ImageIcon("assets/suppr.png")
                        .getImage()
                        .getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
                jb.addActionListener(e -> {
                    liste.remove(b);
                    namesPlayer.remove(jtf);
                    numberPlayers--;
                    checkValid(min, max);
                    revalidate();
                    repaint();
                });
                b.add(jb);

                namesPlayer.add(jtf);
                b.add(jtf);
                liste.add(b);

                super.parent.revalidate();
                super.parent.repaint();
                checkValid(min, max);
            } else {
                super.parent.display("Vous ne pouvez plus ajouter de joueur !");
            }
        });
        Box tmp = Box.createHorizontalBox();
        tmp.add(add);
        liste.add(tmp);

        Box boutons = Box.createHorizontalBox();
        tmp = Box.createVerticalBox();
        tmp.add(new ButtonUp("Retourner au menu", true, true,
                             event -> super.parent.fireGoToMenu()));
        boutons.add(tmp);
        boutons.add(Box.createRigidArea(new Dimension(20, 5)));
        tmp = Box.createVerticalBox();
        param = new ButtonUp("Paramétrer la partie", true, true, event -> {
            Jeu jeu;
            if (oie)
                jeu = new JeuOie(namesPlayer.size());
            else
                jeu = new JeuNumeri(namesPlayer.size());
            for (int i = 0; i < namesPlayer.size(); i++)
                jeu.setNom(i, namesPlayer.get(i).getText());
            parametrerPartie(jeu);
        });
        param.setOpaque(true);
        tmp.add(param);
        boutons.add(tmp);
        box.add(boutons);

        super.parent.getRootPane().setDefaultButton(add);
        checkValid(min, max);
        this.repaint();
    }

    private JButton param;

    private void checkValid(int min, int max) {
        boolean b = true;
        if (namesPlayer.size() < min || namesPlayer.size() > max)
            b = false;
        for (int i = 0; b && i < namesPlayer.size(); i++)
            b = (validName(namesPlayer.get(i).getText()) == 1);
        param.setEnabled(b);
        if (b) {
            param.setBackground(Color.GREEN);
        } else {
            param.setBackground(Color.RED);
        }
    }

    private String getNextValidName() {
        int n = 1;
        while (validName("Joueur " + n) != 0)
            n++;
        return "Joueur " + n;
    }

    private int
    validName(String s) { // renvoie le nombre de joueurs qui ont ce nom
        int n = 0;
        for (JTextField j : namesPlayer) {
            if (j.getText().equals(s))
                n++;
        }
        return n;
    }

    private ArrayList<JTextField> namesPlayer;
    private int numberPlayers;

    private void parametrerPartie(Jeu jeu) {
        this.removeAll();
        this.revalidate();

        Box box = Box.createVerticalBox();
        Box options = Box.createHorizontalBox();
        Box boutons = Box.createHorizontalBox();

        Box b = Box.createVerticalBox();
        options.add(b);
        box.add(options);
        box.add(Box.createRigidArea(new Dimension(5, 20)));
        box.add(boutons);
        this.add(box, new GridBagConstraints());

        ArrayList<Option> op = jeu.getOptions();
        for (Option o : op) {
            Box tmp = Box.createVerticalBox();
            JLabel label = new JLabel(o.toString().toUpperCase());
            Box tmp2 = Box.createVerticalBox();
            tmp2.add(label);
            tmp.add(tmp2);
            ButtonGroup bg = new ButtonGroup();
            String[] t = o.getValues();
            for (int n = 0; n < t.length; n++) {
                JRadioButtonUp jr =
                    new JRadioButtonUp(t[n], (n == o.getIntValue()), o, n);
                jr.setOpaque(false);

                bg.add(jr);
                tmp2.add(jr);
            }
            tmp.add(tmp2);
            b.add(Box.createRigidArea(new Dimension(5, 15)));
            b.add(tmp);
        }

        Box tmp = Box.createVerticalBox();
        tmp.add(new ButtonUp("Retourner au menu", true, true,
                             event -> super.parent.fireGoToMenu()));
        boutons.add(tmp);
        boutons.add(Box.createRigidArea(new Dimension(20, 5)));
        tmp = Box.createVerticalBox();
        ButtonUp creer = new ButtonUp("Créer la partie", true, true, event -> {
            affichage.setJeu(jeu);
            super.parent.fireGoToJeu();
        });
        tmp.add(creer);
        boutons.add(tmp);

        super.parent.getRootPane().setDefaultButton(creer);
        this.repaint();
    }
}