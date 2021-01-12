package jeu.affichage;

import java.util.ArrayList;
import java.util.EventListener;
import javax.swing.JOptionPane;
import jeu.Jeu;
import jeu.JeuNumeri;
import jeu.JeuOie;
import jeu.Joueur;
import jeu.affichage.gui.*;
import jeu.events.CannotPlayEvent;
import jeu.events.GameEvent;
import jeu.events.GameOverEvent;
import jeu.events.PlayEvent;
import jeu.listeners.CannotPlayListener;
import jeu.listeners.DesValueListener;
import jeu.listeners.GameListener;
import jeu.listeners.GameOverListener;
import jeu.listeners.PlayListener;
import jeu.options.Option;
import jeu.options.questions.Question;
import jeu.plateau.Plateau;
import jeu.plateau.cases.Case;

public class AffichageGUI
    extends Affichage implements GameOverListener, CannotPlayListener,
                                 PlayListener, DesValueListener {

    public void afficher() {}

    private Fenetre fenetre;

    public AffichageGUI() {
        modifiedStateListeners = new ArrayList<JeuModifiedStateListener>();
        fenetre = new Fenetre(this);
        setJeu(null); // pas necéssaire mais on ne sait jamais
    }

    @Override
    public void question(Question q) {
        if (((JeuOie)super.getJeu()).repondre(fenetre.question(q))) {
            display("Bonne réponse ! 1 point en plus !");
        } else {
            display("Mauvaise réponse ! La bonne réponse était " +
                    q.getReponse() + ".");
        }
    }

    @Override
    protected void display(String s) {
        javax.swing.SwingUtilities.invokeLater(
            ()
                -> JOptionPane.showMessageDialog(
                    null, s, "Information", JOptionPane.INFORMATION_MESSAGE));
    }

    @Override
    public void setJeu(Jeu jeu) {
        super.setJeu(jeu);
        fireJeuModifState(jeu);
    }

    public interface JeuModifiedStateListener extends EventListener {
        public void modifiedState(Jeu jeu);
    }

    private ArrayList<JeuModifiedStateListener> modifiedStateListeners;

    public void add(JeuModifiedStateListener j) {
        if (j != null)
            modifiedStateListeners.add(j);
    }

    public void recommencer() {
        super.getJeu().recommencer();
        fireJeuModifState(super.getJeu());
    }

    private void fireJeuModifState(Jeu jeu) {
        for (JeuModifiedStateListener j : modifiedStateListeners)
            j.modifiedState(jeu);
    }

    public void gameOver(GameOverEvent e) { fenetre.gameOver(e); }

    public void cannotPlay(CannotPlayEvent e) { fenetre.cannotPlay(e); }

    public void play(PlayEvent e) { fenetre.play(e); }

    public void changeDesValue(int[] i) { fenetre.changeDesValue(i); }
}