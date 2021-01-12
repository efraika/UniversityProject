package jeu.affichage;

/**
 *	Une interface définissant les fonctions que chaque affichage doit
 *implémenter
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import jeu.Jeu;
import jeu.listeners.GameListener;
import jeu.plateau.Plateau;

public abstract class Affichage implements Serializable, GameListener {

    private Jeu jeu;

    public static final String CHEMIN_SAUVEGARDE = "./sauvegardes/";
    public static final String REGEX_SAVE = ".+\\.save$";
    public final File sauvegardes;

    public static final String credits =
        "Programme créé entièrement par Marie Bétend et Pierre Gimalac.\nLes bugs doivent avoir été causés par Maxime car aucun code écrit par Marie ou Pierre ne peut être bugé.";

    public final boolean sauvegarde;

    {
        sauvegardes = new File(CHEMIN_SAUVEGARDE);
        if (!sauvegardes.exists() || !sauvegardes.isDirectory())
            display("Sauvegarde ou chargement de sauvegarde impossible.");
        else if (!sauvegardes.canWrite() || !sauvegardes.canRead())
            display(
                "Droits manquants sur le dossier de sauvegarde pour charger et sauvegarder des parties.");
        sauvegarde = sauvegardes.exists() && sauvegardes.isDirectory() &&
                     sauvegardes.canWrite() && sauvegardes.canRead();
        jeu = null;
    }

    public abstract void afficher();
    protected abstract void display(String s);

    public boolean jeuEnCours() { return jeu != null; }

    public boolean jeuFini() { return jeuEnCours() && jeu.estFini(); }

    public void setJeu(Jeu j) {
        jeu = j;
        if (jeu != null)
            jeu.setGameListener(this);
    }

    public Jeu getJeu() { return jeu; }

    public Jeu chargerLeJeu(String nom) {
        ObjectInputStream ois = null;
        Jeu jeu = null;
        try {
            final FileInputStream fichier =
                new FileInputStream("./sauvegardes/" + nom);
            ois = new ObjectInputStream(fichier);
            jeu = (Jeu)ois.readObject();
        } catch (final java.io.IOException e) {
            e.printStackTrace();
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
        return jeu;
    }

    public void sauvegarderLeJeu(String nom) {
        if (nom == null || nom.equals(""))
            sauvegarderLeJeu();
        if (!nom.matches(REGEX_SAVE))
            nom = nom + ".save";

        jeu.setGameListener(null);
        ObjectOutputStream oos = null;
        try {
            final FileOutputStream fichier =
                new FileOutputStream("./sauvegardes/" + nom);
            oos = new ObjectOutputStream(fichier);
            oos.writeObject(jeu);
            oos.flush();
        } catch (IOException e) {
            display("Erreur de sauvegarde.");
        } finally {
            jeu.setGameListener(this);
            try {
                if (oos != null) {
                    oos.flush();
                    oos.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    public void sauvegarderLeJeu() {
        Date aujourdhui =
            new Date(System.currentTimeMillis() +
                     3600000); // décallage horaire d'une heure en plus
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        sauvegarderLeJeu(date.format(aujourdhui) + ".save");
    }
}