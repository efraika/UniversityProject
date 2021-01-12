import jeu.*;
import jeu.affichage.*;

public class Jouer {

    private Affichage affichage;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Affichage affichage;
                if (args != null && args.length == 1 &&
                    args[0].toLowerCase().equals("gui"))
                    affichage = new AffichageGUI();
                else
                    affichage = new AffichageCUI();
                affichage.afficher();
            }
        });
    }
}