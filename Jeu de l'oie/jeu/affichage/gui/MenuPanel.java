package jeu.affichage.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.Box;
import jeu.Jeu;
import jeu.affichage.Affichage;
import jeu.affichage.AffichageGUI;
import jeu.affichage.AffichageGUI.JeuModifiedStateListener;
import jeu.options.questions.Question;

public class MenuPanel extends JPanelUp implements JeuModifiedStateListener {

    public ButtonUp continuer, sauvegarder, recommencer, nouveau, charger,
        modifier, credits, quitter;

    private final Affichage affichage;

    @Override
    public void modifiedState(Jeu j) {
        continuer.setEnabled(j != null && !j.estFini());
        sauvegarder.setEnabled(affichage.sauvegarde && j != null &&
                               !j.estFini());
        recommencer.setEnabled(j != null);
    }

    public MenuPanel(AffichageGUI affichage, Fenetre parent) {
        super(parent);

        this.affichage = affichage;
        setLayout(new GridBagLayout());
        Box box = Box.createVerticalBox();
        add(box, new GridBagConstraints());
        affichage.add(this);

        continuer = new ButtonUp("Continuer", true, false,
                                 event -> super.parent.fireGoToJeu());
        continuer.addToBox(box, false, 5, 5);

        recommencer = new ButtonUp("Recommencer", true, false, event -> {
            affichage.recommencer();
            super.parent.fireGoToJeu();
        });
        recommencer.addToBox(box, false, 5, 5);

        nouveau = new ButtonUp("Nouvelle partie", true, true,
                               event -> super.parent.fireGoToNouveau());
        nouveau.addToBox(box, false, 5, 5);

        sauvegarder = new ButtonUp("Sauvegarder", true, false,
                                   event -> affichage.sauvegarderLeJeu());
        sauvegarder.addToBox(box, false, 5, 5);

        charger = new ButtonUp("Charger une partie", true, affichage.sauvegarde,
                               event -> super.parent.fireGoToCharger());
        charger.addToBox(box, false, 5, 5);

        modifier = new ButtonUp("Paramètres", true, true,
                                event -> super.parent.fireGoToParam());
        modifier.addToBox(box, false, 5, 5);

        credits =
            new ButtonUp("Credits", true, true,
                         event -> super.parent.display(Affichage.credits));
        credits.addToBox(box, false, 5, 5);

        quitter = new ButtonUp("Quitter", true, true, event -> System.exit(0));
        quitter.addToBox(box, false, 5, 5);
    }

    @Override
    public void goTo() {
        super.goTo();
        super.parent.getRootPane().setDefaultButton(nouveau);
    }
}