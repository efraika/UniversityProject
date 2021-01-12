package jeu;

/**
 *
 *
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import jeu.events.*;
import jeu.exceptions.*;
import jeu.listeners.*;
import jeu.options.Option;
import jeu.plateau.Plateau;
import jeu.plateau.cases.Case;
import jeu.plateau.cases.CaseDepart;

public abstract class Jeu implements Serializable, Iterable<Joueur> {

    private static final long serialVersionUID = 3350919143027733149L;
    private static final ArrayList<Class<? extends Jeu>> jeux =
        new ArrayList<Class<? extends Jeu>>();
    public static final void add(Class<? extends Jeu> c) { jeux.add(c); }
    public static final ArrayList<Class<? extends Jeu>> get() {
        ArrayList<Class<? extends Jeu>> copie =
            new ArrayList<Class<? extends Jeu>>();
        copie.addAll(jeux);
        return copie;
    }

    static {
        add(JeuOie.class);
        add(JeuNumeri.class);
    }

    private static final int MAXIMUM_JOUEURS = Integer.MAX_VALUE - 1;
    private static final int MINIMUM_JOUEURS = 2;
    protected static final Random des = new Random();

    public static int getMinimumJoueurs() { return 2; }

    public static int getMaximumJoueurs() { return Integer.MAX_VALUE - 1; }

    public Iterator<Joueur> iterator() {
        return (new ArrayList<Joueur>(Arrays.asList(joueurs))).iterator();
    }

    public boolean setNom(int numero, String nom) {
        if (numero < 0 || numero >= joueurs.length)
            throw new IllegalArgumentException(
                "le numero du joueur est invalide.");
        Joueur joueur = getJoueur(numero);
        for (Joueur j : this) {
            if (j == joueur)
                continue;
            if (j.toString().equals(nom))
                return false;
        }
        joueur.setNom(nom);
        return true;
    }

    public int lancerDes() {
        setDes(des.nextInt(6) + 1);
        return valeurDes;
    } // lancé d'un dé à 6 faces

    protected final void setDes(int i) {
        int[] t = new int[1];
        t[0] = i;
        setDes(t);
    }

    protected final void setDes(int[] t) {
        int n = 0;
        for (int i : t)
            n += i;
        valeurDes = n;
        fireChangeDesValue(t);
    }

    public final int getDes() { return valeurDes; }

    private int valeurDes = 1;
    protected int numeroDuTour;
    private int enTrainDeJouer;
    protected final Joueur[] joueurs;
    protected final Plateau plateau;
    protected final ArrayList<Joueur> classement;
    public final int nombreDeJoueurs;

    protected GameListener listener;

    public void setGameListener(GameListener j) { listener = j; }

    protected void fireGameOver(GameOverEvent e) {
        if (listener instanceof GameOverListener)
            ((GameOverListener)listener).gameOver(e);
    }

    protected void fireCannotPlay(CannotPlayEvent e) {
        if (listener instanceof CannotPlayListener)
            ((CannotPlayListener)listener).cannotPlay(e);
    }

    protected void firePlay(PlayEvent e) {
        if (listener instanceof PlayListener)
            ((PlayListener)listener).play(e);
    }

    protected void fireChangeDesValue(int[] i) {
        if (listener instanceof DesValueListener)
            ((DesValueListener)listener).changeDesValue(i);
    }

    private ArrayList<Option> options = new ArrayList<Option>();

    protected Option getOption(Class c) {
        for (Option o : options) {
            if (o.getClass().equals(c))
                return o;
        }
        return null;
    }

    public ArrayList<Option> getOptions() {
        ArrayList<Option> copie = new ArrayList<Option>();
        copie.addAll(options);
        return copie;
    }

    protected void addOption(Option o) {
        if (partieCommencee())
            throw new OptionException(
                "Impossible d'ajouter une option après le début de la partie.");
        else if (getOption(o.getClass()) != null) {
            throw new OptionException("Cette option est déjà dans le jeu.");
        } else if (o.checkJeu(this))
            options.add(o);
        else
            throw new OptionException(
                "Cette option n'est pas associée à ce jeu.");
    }

    public boolean partieCommencee() {
        return numeroDuTour != 1 || enTrainDeJouer != 0;
    }

    public int getValeurDes() { return valeurDes; }

    public Joueur joueurEnTrainDeJouer() { return joueurs[enTrainDeJouer]; }

    public int getCase(Case c) { return plateau.getCase(c); }

    protected Case getCase(int i) { return plateau.getCase(i); }

    public String getName() { return joueurEnTrainDeJouer().toString(); }

    public Joueur getJoueur(int i) { return joueurs[i]; }

    public int getNumeroDuTour() { return numeroDuTour; }

    protected Jeu(Plateau plateau, int nombreDeJoueursHumains,
                  int nbPionsParJoueur, CaseDepart depart) {
        if (plateau == null)
            throw new IllegalArgumentException(
                "Le plateau n'a pas été initialisé");
        else if (nombreDeJoueursHumains < this.getMinimumJoueurs())
            throw new IllegalArgumentException(
                "Le nombre de joueurs est trop petit");
        else if (nombreDeJoueursHumains > this.getMaximumJoueurs())
            throw new IllegalArgumentException(
                "Le nombre de joueurs est trop grand");

        nombreDeJoueurs = nombreDeJoueursHumains;
        numeroDuTour = 1;
        this.plateau = plateau;
        joueurs = new Joueur[nombreDeJoueurs];
        classement = new ArrayList<Joueur>() {
            public Iterator<Joueur> iterator() {
                Collections.sort(this, Collections.reverseOrder());
                return super.iterator();
            }
        };
        for (int i = 0; i < nombreDeJoueurs; i++) {
            joueurs[i] = new Joueur(i + 1);
            classement.add(getJoueur(i));
        }
        enTrainDeJouer = 0;
        initialiserPionsJoueurs(nbPionsParJoueur, depart);
    }

    public Jeu(Plateau plateau, int nombreDeJoueursHumains,
               int nbPionsParJoueur) {
        this(plateau, nombreDeJoueursHumains, nbPionsParJoueur,
             (CaseDepart)plateau.getCase(0));
    }

    private void initialiserPionsJoueurs(
        int nbPionsParJoueur,
        CaseDepart depart) { // doit être appelée avant de commencer à jouer
        for (Joueur joueur : joueurs)
            joueur.initialiserPionsJoueurs(nbPionsParJoueur, depart);
    }

    public void recommencer() {
        for (Joueur joueur : joueurs)
            joueur.recommencer();
        plateau.recommencer();
        valeurDes = 1;
        enTrainDeJouer = 0;
        numeroDuTour = 1;
    }

    protected Joueur joueurSuivant() {
        if (estFini())
            throw new GameOverException(
                "Le jeu est fini : aucun joueur ne peut jouer.");
        enTrainDeJouer = (enTrainDeJouer + 1) % nombreDeJoueurs;
        if (enTrainDeJouer == 0) {
            numeroDuTour++;
            plateau.unTourPasse();
        }

        if (peutJouer(joueurEnTrainDeJouer())) {
            return joueurEnTrainDeJouer();
        } else {
            fireCannotPlay(
                new CannotPlayEvent(this, joueurEnTrainDeJouer(),
                                    joueurEnTrainDeJouer().getCase()));
            return joueurSuivant();
        }
    }

    public Plateau getPlateau() { return plateau; }

    public void classement() { // met à jour le classement
        Collections.sort(classement, Collections.reverseOrder());
    }

    public String getClassement() { // TODO
        int j = 1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nombreDeJoueurs; i++)
            sb.append((i + 1) + ". " + classement.get(i) + "\n");
        return sb.toString();
    }

    public boolean estVide(Case c) {
        for (Joueur joueur : this) {
            for (Case ca : joueur) {
                if (ca == c)
                    return false;
            }
        }
        return true;
    }

    public abstract boolean estFini();
    public abstract boolean peutJouer(Joueur joueur);
    public abstract String getChoix();
    public abstract boolean choix();
    public abstract boolean choix(String entree);
    public abstract void jouer(); // joue le tour d'un joueur
}
