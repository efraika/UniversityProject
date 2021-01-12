package jeu.affichage;

/**
 *	L'affichage en console
 */

import java.util.ArrayList;
import java.util.Scanner;
import jeu.Jeu;
import jeu.JeuNumeri;
import jeu.JeuOie;
import jeu.Joueur;
import jeu.events.CannotPlayEvent;
import jeu.events.GameEvent;
import jeu.events.GameOverEvent;
import jeu.events.PlayEvent;
import jeu.listeners.CannotPlayListener;
import jeu.listeners.GameListener;
import jeu.listeners.GameOverListener;
import jeu.listeners.PlayListener;
import jeu.options.Option;
import jeu.options.questions.Question;
import jeu.plateau.Plateau;
import jeu.plateau.cases.Case;

public class AffichageCUI extends Affichage
    implements GameOverListener, CannotPlayListener, PlayListener {

    private static Scanner sc = new Scanner(System.in, "UTF-8");

    class AffichagePlateauSpiraleCUI implements AffichagePlateau {
        @Override
        public void afficher() {
            Plateau plateau = AffichageCUI.this.getJeu().getPlateau();
            int taille = plateau.size();
            int l = NOMBRE_CASE_LARGEUR;
            int h = (taille / l) + 1;

            // on cherche un rectangle exact pour le plateau
            for (int n = NOMBRE_CASE_LARGEUR; n > NOMBRE_CASE_LARGEUR / 2;
                 n--) {
                if (taille == (taille / n) * n) {
                    l = n;
                    h = taille / n;
                    break;
                }
            }

            if (taille != l * h) {
                int d = (l * h) - taille;
                for (int n = NOMBRE_CASE_LARGEUR - 1;
                     n > NOMBRE_CASE_LARGEUR / 2; n--) {
                    if (d > n * ((taille / n) + 1) - taille) {
                        l = n;
                        h = (taille / l) + 1;
                        d = (l * h) - taille;
                    }
                }
            }
            // on trouve h et l tels que l'écart entre la taille du plateau et
            // h*l soit la plus petite possible (en ayant l pas trop petit)

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

            // c'est couteux mais je ne vois pas comment le faire autrement
            // aussi facilement

            StringBuilder sb = new StringBuilder(WHITEf);
            for (int n = 0; n < l * h; n++) {
                String s;
                if (n < taille)
                    s = COLORS[n % COLORS.length] +
                        centrer(LARGEUR_CASE,
                                (n + 1) + "-" + plateau.getCase(n).toString());
                else
                    s = WHITEb + centrer(LARGEUR_CASE, " ");

                if (directionH == 1 || directionV == -1)
                    debut[coorV] = debut[coorV] + s;
                else // (directionH==-1 || directionV==-1)
                    fin[coorV] = s + fin[coorV];

                lignes[coorV]++;
                colonnes[coorH]++;

                if (lignes[coorV] == l && directionV == 0) {
                    if (directionH == 1)
                        directionV = 1;
                    else
                        directionV = -1;
                    directionH = 0;
                } else if (colonnes[coorH] == h && directionH == 0) {
                    if (directionV == 1)
                        directionH = -1;
                    else
                        directionH = 1;
                    directionV = 0;
                }

                coorH += directionH;
                coorV += directionV;
            }
            for (int n = 0; n < h; n++)
                sb.append(debut[n] + fin[n] + RESET + "\n" + WHITEf);

            System.out.println(sb.toString() + RESET);
        }

        @Override
        public String toString() {
            return "spirale";
        }
    }

    class AffichagePlateauRectangleCUI implements AffichagePlateau {
        @Override
        public void afficher() {
            Plateau plateau = AffichageCUI.this.getJeu().getPlateau();
            StringBuilder sb = new StringBuilder(WHITEf);
            int size = plateau.size();
            for (int i = 0; i < size; i++) {
                sb.append(
                    COLORS[i % COLORS.length] +
                    centrer(LARGEUR_CASE,
                            (i + 1) + "-" + plateau.getCase(i).toString()));
                if ((i + 1) % NOMBRE_CASE_LARGEUR == 0)
                    sb.append("\n" + WHITEf);
            }
            sb.append(RESET);
            System.out.println(sb.toString());
        }

        @Override
        public String toString() {
            return "rectangle";
        }
    }

    class AffichagePlateauZigzagCUI implements AffichagePlateau {
        @Override
        public void afficher() {
            Plateau plateau = AffichageCUI.this.getJeu().getPlateau();
            StringBuilder sb = new StringBuilder(WHITEf);
            StringBuilder ligne = new StringBuilder();
            for (int i = 0; i < plateau.size(); i++) {
                String s =
                    COLORS[i % COLORS.length] +
                    centrer(LARGEUR_CASE,
                            (i + 1) + "-" + plateau.getCase(i).toString());
                if (i / NOMBRE_CASE_LARGEUR % 2 == 0)
                    sb.append(s);
                else
                    ligne.insert(0, s);

                if ((i + 1) % NOMBRE_CASE_LARGEUR == 0) {
                    sb.append(ligne.toString() + RESET + "\n");
                    ligne = new StringBuilder();
                }
            }
            sb.append(ligne.toString() + RESET);
            System.out.println(sb.toString());
        }

        @Override
        public String toString() {
            return "zigzag";
        }
    }

    class AffichagePlateauColonneCUI implements AffichagePlateau {
        @Override
        public void afficher() {
            Plateau plateau = AffichageCUI.this.getJeu().getPlateau();
            StringBuilder sb = new StringBuilder(BLACKf);
            for (int i = 0; i < plateau.size(); i++)
                sb.append(
                    COLORS[i % COLORS.length] +
                    centrer(LARGEUR_CASE,
                            (i + 1) + "-" + plateau.getCase(i).toString()) +
                    RESET + "\n" + WHITEf);
            System.out.println(sb.toString());
        }

        @Override
        public String toString() {
            return "colonne";
        }
    }

    public static final String EFFACER, RESET;
    public static final String BLACKf, REDf, GREENf, YELLOWf, BLUEf, MAGENTAf,
        CYANf, WHITEf;
    public static final String BLACKb, REDb, GREENb, YELLOWb, BLUEb, MAGENTAb,
        CYANb, WHITEb;
    private static final String[] COLORS;

    public static final String HELP =
        "Les commandes attendues sont soit indiquées explicitement soit entre parenthèses.\nTaper autre chose que ce qui est attendu ne fera rien.\nTaper q pour quitter ou m pour accéder au menu (sauf cas particuliers, si une réponse est attendue par exemple).\n";
    public static final char MENU = 'm', QUITTER = 'q', SAUVEGARDER = 's',
                             CONTINUER = 'c', RECOMMENCER = 'r', CHARGER = 'l',
                             NOUVEAU = 'n', CREDITS = 'z', MODIFIER_PARAM = 'p';

    static {
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            EFFACER = "";
            RESET = "";
            BLACKf = "";
            REDf = "";
            GREENf = "";
            YELLOWf = "";
            BLUEf = "";
            MAGENTAf = "";
            CYANf = "";
            WHITEf = "";
            BLACKb = "";
            REDb = "";
            GREENb = "";
            YELLOWb = "";
            BLUEb = "";
            MAGENTAb = "";
            CYANb = "";
            WHITEb = "";
            COLORS = new String[1];
            COLORS[0] = "";
        } else {
            EFFACER = "\033[H\033[2J";
            RESET = "\u001b[0m";
            BLACKf = "\u001b[30m";
            REDf = "\u001b[31m";
            GREENf = "\u001b[32m";
            YELLOWf = "\u001b[33m";
            BLUEf = "\u001b[34m";
            MAGENTAf = "\u001b[35m";
            CYANf = "\u001b[36m";
            WHITEf = "\u001b[37m";
            BLACKb = "\u001b[40m";
            REDb = "\u001b[41m";
            GREENb = "\u001b[42m";
            YELLOWb = "\u001b[43m";
            BLUEb = "\u001b[44m";
            MAGENTAb = "\u001b[45m";
            CYANb = "\u001b[46m";
            WHITEb = "\u001b[47m";
            COLORS = new String[6];
            COLORS[0] = REDb;
            COLORS[1] = GREENb;
            COLORS[2] = YELLOWb;
            COLORS[3] = BLUEb;
            COLORS[4] = MAGENTAb;
            COLORS[5] = CYANb;
        }
    }

    public boolean estCommande(char c) {
        return c == MENU || c == QUITTER || c == SAUVEGARDER ||
            c == CONTINUER || c == RECOMMENCER || c == CHARGER ||
            c == NOUVEAU || c == CREDITS || c == MODIFIER_PARAM;
    }

    public AffichageCUI() {
        NOMBRE_CASE_LARGEUR = 9;
        LARGEUR_CASE = 12;
        WAITING_TIME = 800;
        affichagePlateau = new AffichagePlateauSpiraleCUI();
    }

    private AffichagePlateau affichagePlateau;

    private int NOMBRE_CASE_LARGEUR;
    private int LARGEUR_CASE;
    private int WAITING_TIME;

    private void setNombreCaseLargeur(int l) { NOMBRE_CASE_LARGEUR = l; }

    private String centrer(int largeur, String s) {
        if (s.length() > largeur) {
            return s.substring(0, largeur - 1) + ".";
        } else {
            StringBuilder sb = new StringBuilder(s);
            for (int i = largeur - s.length(); i > 0; i--) {
                if (i % 2 == 0)
                    sb.append(' ');
                else
                    sb.insert(0, ' ');
            }
            return sb.toString();
        }
    }

    private void afficherPlateau() { affichagePlateau.afficher(); }

    @Override
    public void afficher() {
        help();
        while (true) {
            menu();
            char c = getCommande(super.jeuEnCours(), super.jeuFini());
            switch (c) {
            case NOUVEAU:
                setJeu();
                if (jeuEnCours() && !jeuFini())
                    jouer();
                break;
            case QUITTER:
                System.exit(0);
            case SAUVEGARDER:
                sauvegarderLeJeu();
                break;
            case CHARGER:
                charger();
                if (jeuEnCours() && !jeuFini())
                    jouer();
                break;
            case RECOMMENCER:
                super.getJeu().recommencer();
            case CONTINUER:
                jouer();
                break;
            case MODIFIER_PARAM:
                modifierParam();
                break;
            case CREDITS:
                credits();
                break;
            }
        }
    }

    private void modifierParam() {
        while (true) {
            String[] t = new String[4];
            t[0] = "type d'affichage du plateau";
            t[1] = "nombre de case sur la largeur de l'écran";
            t[2] = "largeur d'une case";
            t[3] =
                "temps d'attente après le tour d'une IA ou à la fin d'un tour";
            StringBuilder sb = new StringBuilder(
                "\nVoici les paramètres qui peuvent être modifiés :\n");
            for (int i = 0; i < t.length; i++)
                sb.append((i + 1) + ". " + t[i] + "\n");
            sb.append(
                "\nEntrez le numero de l'option à modifier. Entrez rien ou autre chose pour retourner au menu. ");
            System.out.print(sb.toString());
            String tmp = sc.nextLine();
            switch (tmp) {
            case "1":
                System.out.println(
                    t[0] +
                    ", les affichages possibles sont ceux-ci :\n1. spirale\n2. rectangle\n3. zigzag\n4. colonne\nEntrez le numero de l'option choisie. ");
                String choix = sc.nextLine();
                switch (choix) {
                case "1":
                    affichagePlateau = new AffichagePlateauSpiraleCUI();
                    break;
                case "2":
                    affichagePlateau = new AffichagePlateauRectangleCUI();
                    break;
                case "3":
                    affichagePlateau = new AffichagePlateauZigzagCUI();
                    break;
                case "4":
                    affichagePlateau = new AffichagePlateauColonneCUI();
                    break;
                }
                break;
            case "2":
                System.out.println(
                    "Le nombre actuel de case sur la largeur de l'écran est " +
                    NOMBRE_CASE_LARGEUR + ". Entrez la nouvelle valeur : ");
                try {
                    int n = Integer.parseInt(sc.nextLine());
                    if (n < 1)
                        throw new NumberFormatException();
                    NOMBRE_CASE_LARGEUR = n;
                } catch (NumberFormatException nfe) {
                    System.out.println("Entrée invalide.");
                }
                break;
            case "3":
                System.out.println("La largeur actuelle d'une case est " +
                                   LARGEUR_CASE +
                                   ". Entrez la nouvelle valeur : ");
                try {
                    int n = Integer.parseInt(sc.nextLine());
                    if (n < 1)
                        throw new NumberFormatException();
                    LARGEUR_CASE = n;
                } catch (NumberFormatException nfe) {
                    System.out.println("Entrée invalide.");
                }
                break;
            case "4":
                System.out.println(
                    "Le temps d'attente actuel après le tour d'une IA est " +
                    WAITING_TIME + "ms. Entrez la nouvelle valeur : ");
                try {
                    int n = Integer.parseInt(sc.nextLine());
                    if (n < 1)
                        throw new NumberFormatException();
                    WAITING_TIME = n;
                } catch (NumberFormatException nfe) {
                    System.out.println("Entrée invalide.");
                }
                break;
            default:
                return;
            }
        }
    }

    private boolean charger() {
        StringBuilder sb =
            new StringBuilder("Voici la liste des sauvegardes existantes :\n");
        String[] t = sauvegardes.list();
        int i = 1;
        for (String s : t) {
            if (s.matches(Affichage.REGEX_SAVE)) {
                sb.append((i) + ". " + t[i - 1] + "\n");
                i++;
            }
        }

        if (i == 1) {
            System.out.println("Il n'existe aucune sauvegarde !");
            return false;
        }

        sb.append(
            "\nEntrez le numéro ou le nom complet d'une sauvegarde pour charger la partie : ");
        System.out.print(sb.toString());
        String n = null;

        while (true) {
            n = sc.nextLine();
            if (n == null)
                continue;
            else if (n.length() == 1 && n.charAt(0) == QUITTER)
                System.exit(0);
            else if (n.length() == 1 && n.charAt(0) == MENU)
                return false;
            else if (n.matches(Affichage.REGEX_SAVE)) {
                for (String s : t) {
                    if (s.equals(n)) {
                        super.setJeu(chargerLeJeu(n));
                        return true;
                    }
                }
            } else {
                try {
                    int j = Integer.parseInt(n);
                    if (j > 0 && j < i) {
                        for (String s : t) {
                            if (s.matches(REGEX_SAVE) && j == 1) {
                                super.setJeu(chargerLeJeu(s));
                                return true;
                            } else if (s.matches(REGEX_SAVE))
                                j--;
                        }
                    } else {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException nfe) {
                    commandeInvalide();
                }
            }
            return false;
        }
    }

    private void jouer() {
        Jeu jeu = super.getJeu();
        int numeroDuTour = jeu.getNumeroDuTour() - 1;
        while (!jeu.estFini()) {
            int tour = jeu.getNumeroDuTour();
            if (tour != numeroDuTour) {

                System.out.print(RESET + EFFACER);
                afficherPlateau();
                System.out.println("");

                numeroDuTour = tour;
                System.out.println("Tour " + numeroDuTour);

                System.out.println(getPositions());
            }

            Joueur joueur = jeu.joueurEnTrainDeJouer();
            System.out.print(
                "C'est au tour de " + joueur +
                " de jouer : appuyer sur entrée pour lancer les dés ! ");
            String tmp = sc.nextLine();
            if (tmp.length() == 1) {
                if (tmp.charAt(0) == QUITTER)
                    System.exit(0);
                else if (tmp.charAt(0) == MENU)
                    return;
            }

            int d = jeu.lancerDes();
            while (jeu.choix()) { // tant qu'il y a un choix à faire
                System.out.print(jeu.getChoix()); // on affiche le choix à faire
                while (!jeu.choix(
                    sc.nextLine())) // tant que la réponse au choix n'est pas
                                    // valide, on demande une réponse
                    System.out.println("Entrée invalide !");
            }
            jeu.jouer();
        }
    }

    private String getPositions() {
        StringBuilder sb = new StringBuilder();
        Jeu jeu = super.getJeu();
        for (Joueur joueur : jeu)
            sb.append(joueur + " (score " + joueur.getScore() +
                      ") : " + getPositions(joueur) + "\n");
        return sb.toString();
    }

    private String getPositions(Joueur joueur) {
        StringBuilder sb = new StringBuilder();
        Jeu jeu = super.getJeu();
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

    private char getCommande(boolean jeuEnCours, boolean jeuFini) {
        while (true) {
            String s = sc.nextLine();
            if (s != null && s.length() == 1) {
                char c = s.charAt(0);
                if (estCommande(c)) {
                    if (c == SAUVEGARDER || c == CHARGER) { // cas particuliers
                        if (super.sauvegarde &&
                            (c == CHARGER ||
                             (c == SAUVEGARDER && jeuEnCours && !jeuFini)))
                            return c;
                    } else if (c == CONTINUER) {
                        if (jeuEnCours && !jeuFini)
                            return c;
                    } else if (!jeuEnCours && c != RECOMMENCER)
                        return c;
                    else if (jeuEnCours)
                        return c;
                }
            }
            commandeInvalide();
        }
    }

    private void commandeInvalide() { System.out.println("Entrée invalide."); }

    private void menu() {
        StringBuilder sb = new StringBuilder("MENU\n\n");
        if (super.getJeu() != null) {
            if (!super.getJeu().estFini())
                sb.append("Continuer (" + CONTINUER + ")\n" +
                          ((super.sauvegarde)
                               ? "Sauvegarder (" + SAUVEGARDER + ")\n"
                               : ""));
            sb.append("Recommencer avec les mêmes paramètres (" + RECOMMENCER +
                      ")\n");
        }
        sb.append("Nouveau jeu (" + NOUVEAU + ")\n" +
                  ((super.sauvegarde)
                       ? "Charger une sauvegarde (" + CHARGER + ")\n"
                       : "") +
                  "Modifier les paramètres (" + MODIFIER_PARAM +
                  ")\nCrédits (" + CREDITS + ")\nQuitter (" + QUITTER + ")\n");
        System.out.println(sb.toString());
    }

    private Jeu setJeu() {
        System.out.print(
            "Entrer o pour jouer au jeu de l'oie ou n pour jouer au numéri : ");
        String s = null;
        boolean b = true;
        Jeu jeu = super.getJeu();

        int min = 2, max = 2;

        do {
            s = sc.nextLine();
            if (s != null && s.length() == 1) {
                if (s.charAt(0) == 'n') {
                    b = false;
                    min = JeuNumeri.getMinimumJoueurs();
                    max = JeuNumeri.getMaximumJoueurs();
                } else if (s.charAt(0) == 'o') {
                    b = false;
                    min = JeuOie.getMinimumJoueurs();
                    max = JeuOie.getMaximumJoueurs();
                } else if (s.charAt(0) == QUITTER)
                    System.exit(0);
                else if (s.charAt(0) == MENU)
                    return jeu;
                else
                    commandeInvalide();
            }
        } while (b);

        System.out.print("Il doit y avoir entre " + min + " et " + max +
                         " joueurs. Entrez le nombre de joueurs : ");

        int humains = -1;
        do {
            try {
                humains = Integer.parseInt(sc.next());
            } catch (NumberFormatException nfe) {
                commandeInvalide();
            }
        } while (humains < 0 || humains > max);

        if (s.charAt(0) == 'o') {
            jeu = new JeuOie(humains);
        } else {
            jeu = new JeuNumeri(humains);
        }
        super.setJeu(jeu);

        // paramétrage de la partie // TODO
        System.out.print("Modifier les options par défaut du jeu ? (o/N) ");
        sc.nextLine();
        String tmp = sc.nextLine();
        if (tmp != null && tmp.length() == 1 &&
            tmp.toLowerCase().charAt(0) == 'o') {
            StringBuilder sb = new StringBuilder(
                "\nEntrer le numero de l'option que vous souhaitez changer, entrer autre chose qu'un nombre pour quitter :\n" +
                0 + ". modifier les noms des joueurs\n");
            ArrayList<Option> options = jeu.getOptions();
            int nb = 1;
            for (Option option : options) {
                sb.append(nb + ". " + option + "\n");
                nb++;
            }
            nb = -2;
        loop:
            do {
                System.out.print(sb.toString());
                try {
                    nb = Integer.parseInt(sc.nextLine()) - 1;
                    if (nb == -1) {
                        System.out.println(
                            "\nEntrez les nouveaux noms des joueurs : ");
                        int n = 0;
                        for (Joueur joueur : jeu) {
                            b = false;
                            do {
                                if (b)
                                    System.out.println("Ce nom est déjà pris.");
                                System.out.print(joueur + " : ");
                                b = !jeu.setNom(n, sc.nextLine());
                            } while (b);
                            n++;
                        }
                    } else if (nb >= 0 && nb < options.size()) {
                        Option o = options.get(nb);
                        System.out.println(
                            "Entrez le numero de la valeur à donner à l'option, une entrée invalide fera retourner à l'affichage des options : \n" +
                            o);
                        int i = 1;
                        for (String t : o.getValues()) {
                            System.out.println("    " + i + ". " + t);
                            i++;
                        }
                        try {
                            int m = Integer.parseInt(sc.nextLine()) - 1;
                            if (m < 0 || m >= o.getNombreValue())
                                throw new NumberFormatException();
                            else {
                                o.setValue(m);
                            }
                        } catch (NumberFormatException e) {
                        }
                    }
                } catch (NumberFormatException e) {
                    break loop;
                }
            } while (true);
        }
        return jeu;
    }

    public void help() {
        StringBuilder sb = new StringBuilder(RESET + HELP + "\n");
        for (Class c : Jeu.get()) {
            sb.append("\n" + c.getSimpleName() + "\n");
            try {
                sb.append("\n" + c.getDeclaredField("description").get(null) +
                          "\n");
            } catch (Exception e) {
            }
        }
        System.out.println(sb.toString());
    }

    public void credits() { System.out.println(Affichage.credits); }

    public void gameOver(GameOverEvent e) {
        System.out.println("Partie finie ! " + e + ".\n\n" + e.getClassement());
        sleep();
    }

    public void cannotPlay(CannotPlayEvent e) {
        System.out.println(e.toString());
        sleep();
    }

    public void play(PlayEvent e) {
        Joueur joueur = e.getJoueur();
        System.out.println(joueur + " (score " + joueur.getScore() +
                           ") a fait " + e.getDes() + " : " +
                           getPositions(joueur));
        sleep();
    }

    public void question(Question e) {
        System.out.println(e.getQuestion());
        if (((JeuOie)super.getJeu()).repondre(sc.nextLine()))
            System.out.println("Bonne réponse !");
        else {
            System.out.println("Mauvaise réponse ! La réponse était " +
                               e.getReponse() + ".");
        }
        sleep();
    }

    private void sleep() {
        try {
            Thread.sleep(WAITING_TIME);
        } catch (InterruptedException e) {
        }
    }

    protected void display(String s) { System.out.println(s); }
}