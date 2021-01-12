package jeu.plateau.cases;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;
import jeu.Joueur;

/**
 *
 */

public class CaseHotel extends Case {

    /**
     *
     */

    private static final long serialVersionUID = -7025272972861574181L;

    private Hotel hotel;
    private int TOURS_A_PASSER;

    private class Hotel implements Serializable {
        private CopyOnWriteArrayList<Chambre> chambres;

        Hotel() { chambres = new CopyOnWriteArrayList<Chambre>(); }

        public void unTourPasse() {
            for (Chambre chambre : chambres) {
                chambre.unTourPasse();
                if (chambre.getTime() == -1)
                    chambres.remove(chambre);
            }
        }

        public boolean contains(Joueur joueur) {
            for (Chambre chambre : chambres) {
                if (chambre.contains(joueur))
                    return true;
            }
            return false;
        }

        public void add(Joueur joueur) {
            chambres.add(new Chambre(joueur, TOURS_A_PASSER));
        }

        private class Chambre
            implements Serializable { // couple joueur-entier (je trouvais ça
                                      // plus sympa de faire une classe chambre
                                      // et une classe hotel que d'utiliser map
                                      // ^^)
            private Joueur joueur;
            private int temps;

            Chambre(Joueur joueur, int temps) {
                this.joueur = joueur;
                this.temps = temps;
            }

            public int unTourPasse() {
                temps--;
                return temps;
            }

            public boolean contains(Joueur joueur) {
                return this.joueur == joueur;
            }

            public int getTime() { return temps; }
        }
    }

    public CaseHotel() { this(2); }

    public CaseHotel(int temps) {
        super();
        hotel = new Hotel();
        this.TOURS_A_PASSER = temps;
    }

    @Override
    public void arriveSurCase(Joueur j) {
        hotel.add(j);
    }

    @Override
    public boolean peutJouer(Joueur j) {
        return !hotel.contains(j);
    }

    @Override
    public String toString() {
        return "hôtel";
    }

    @Override
    public void unTourPasse() {
        hotel.unTourPasse();
    }

    @Override
    public void recommencer() {
        hotel = new Hotel();
    }
}