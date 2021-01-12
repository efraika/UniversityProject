package jeu;

/**
 *
 */

import java.util.ArrayList;
import jeu.events.GameOverEvent;
import jeu.events.PlayEvent;
import jeu.exceptions.ChoiceException;
import jeu.exceptions.WrongOptionException;
import jeu.listeners.GameListener;
import jeu.options.OptionPionCaseOie;
import jeu.options.OptionPositionFinOie;
import jeu.options.OptionQuestionOie;
import jeu.options.questions.*;
import jeu.plateau.Plateau;
import jeu.plateau.cases.Case;
import jeu.plateau.cases.CaseDepart;

public class JeuOie extends Jeu {

    private static final long serialVersionUID = -6311358070333990328L;

    public static final String description =
        "Ce jeu est joué par au moins 2 joueurs, chaque joueur possède un pion situé sur la premiere case au début. A chaque tour le joueur lance un dé et avance du nombre du case indiqué par le dé. Certaines cases ont des effets spéciaux : la case oie fait avancer du même nombre de case, la case hotel passe 2 tours, le labyrinthe envoie sur une case en arrière alors que le pont envoie sur une case en avant, la case mort renvoie au début, la prison et le puit bloquent un joueur indéfiniement tant qu'un autre joueur ne les libère pas en arrivant sur la même case : dans le cas de la case puit le joueur est alors bloqué. Une des options possibles concerne le placement des pions : il est possible d'avoir plusieurs pions sur la même case ou alors quand un pion arrive sur une case occupée il recule jusqu'à une case libre ou alors il échange de place avec le pion qui y était avant. Une autre option est la possibilité qu'une question soit posée au joueur à la fin de son tour : le joueur gagne alors des points en répondant à celle-ci (la partie finie alors dès qu'un joueur finit). Les joueurs en prison, dans le puit ou à l'hotel n'auront pas de question.";

    public JeuOie(Plateau plateau, int nombreDeJoueursHumains) {
        super(plateau, nombreDeJoueursHumains, 1,
              (CaseDepart)plateau.getCase(0));

        super.addOption(new OptionPositionFinOie(this));
        super.addOption(new OptionPionCaseOie(this));
        super.addOption(new OptionQuestionOie(this));
    }

    public JeuOie(int nombreDeJoueursHumains) {
        this(Plateau.getDefaultOie(), nombreDeJoueursHumains);
    }

    @Override
    public int lancerDes() {
        int[] t = new int[2];
        t[0] = Jeu.des.nextInt(6) + 1;
        t[1] = Jeu.des.nextInt(6) + 1;
        super.setDes(t);
        return super.getDes();
    }

    @Override
    public boolean choix() {
        return false;
    }

    @Override
    public String getChoix() {
        throw new ChoiceException();
    }

    @Override
    public boolean choix(String entree) {
        throw new ChoiceException();
    }

    @Override
    public boolean estFini() {
        if (gameOverFired)
            return true;

        int option = super.getOption(OptionQuestionOie.class).getIntValue();
        boolean tousFini = true;
        for (Joueur joueur : this) {
            if (option == 1 &&
                joueur.getCase().estFinale()) { // cas où l'on pose une question
                                                // : fin dès qu'un joueur a fini
                super.fireGameOver(new GameOverEvent(this, joueur + " a fini",
                                                     super.getClassement()));
                gameOverFired = true;
                return true;
            }
            if (joueur.getCase().willPlay(joueur))
                return false;
            if (tousFini && !joueur.getCase().estFinale())
                tousFini = false;
        }

        if (!gameOverFired) {
            if (tousFini)
                super.fireGameOver(new GameOverEvent(
                    this, "Tous les joueurs ont fini", super.getClassement()));
            else
                super.fireGameOver(new GameOverEvent(
                    this, "Tous les joueurs ont fini ou ne peuvent plus jouer",
                    super.getClassement()));
            gameOverFired = true;
        }
        return true;
    }

    private boolean gameOverFired = false;
    private BanqueQuestions questions = new BanqueQuestions();

    public static int getMinimumJoueurs() { return 2; }

    public static int getMaximumJoueurs() { return Integer.MAX_VALUE - 1; }

    private void fireQuestion(Question q) { listener.question(q); }

    public boolean repondre(String reponse) {
        this.reponse = reponse;
        return question.isTrue(reponse);
    }

    @Override
    public void jouer() {
        Joueur joueur = super.joueurEnTrainDeJouer();
        Case tmp = getCase(joueur.getCase(), super.getDes());
        ArrayList<Case> list = new ArrayList<Case>();
        while (
            tmp != null && tmp.moveAgain() &&
            !list.contains(tmp)) { // pour éviter de boucler sur des cases oie
                                   // // tmp==null ssi on tombe sur la case mort
            list.add(tmp);
            tmp = getCase(tmp, super.getDes());
        }
        joueur.setCase(tmp);
        int option = super.getOption(OptionQuestionOie.class).getIntValue();
        if (option == 0) // on modifie le score après que les dés aient été
                         // lancés, mais on pose la question après avoir averti
                         // l'affichage que le joueur avait joué
            joueur.setScore(super.getCase(joueur.getCase()));
        super.firePlay(new PlayEvent(this, joueur, super.getDes()));

        switch (option) {
        case 0:
            break;
        case 1:
            question = questions.get();
            fireQuestion(question);
            if (question.isTrue(reponse))
                joueur.incrementerScore();
            reponse = null;
            break;
        default:
            throw new WrongOptionException(OptionQuestionOie.class, option);
        }
        if (!estFini())
            super.joueurSuivant();
        super.classement();
    }

    private Question question;
    private String reponse = null;

    @Override
    public boolean peutJouer(Joueur joueur) {
        return joueur.getCase().peutJouer(joueur);
    }

    private Case getCase(Case depart, int des) {
        return getCase(getCase(depart), des);
    }

    private Case getCase(int depart, int des) { // pas optimisé, TODO ?
        Joueur joueur = super.joueurEnTrainDeJouer();
        Case departC = super.getCase(depart);
        while (des != 0) {
            if (depart == 0 && des < 0)
                des = 0;
            else if (depart == super.getPlateau().size() - 1 && des > 0) {
                int option =
                    super.getOption(OptionPositionFinOie.class).getIntValue();
                switch (option) {
                case 0:
                    des = -des;
                    break;
                case 1:
                    des = 0;
                    break;
                default:
                    throw new WrongOptionException(OptionPositionFinOie.class,
                                                   option);
                }
            } else {
                depart += Math.abs(des) / des;
                des -= Math.abs(des) / des;
            }
        }
        Case tmp = super.getCase(depart);

        int option = super.getOption(OptionPionCaseOie.class).getIntValue();
        switch (option) {
        case 0:
            break;
        case 1:
            if (!tmp.estInitiale() && !tmp.estFinale() &&
                tmp.willPlay(joueur)) { // il peut y avoir plusieurs joueurs sur
                                        // les cases finales et initiales, ainsi
                                        // que sur les cases puit et prison
                // je sais que c'est en partie redondant mais c'est plus clair
                for (Joueur j : this) {
                    if (j.getCase() == tmp) {
                        j.setCase(departC);
                        break;
                    }
                }
            }
            break;
        case 2:
            while (!tmp.estFinale() && !tmp.estInitiale() &&
                   tmp.willPlay(joueur) && !super.estVide(tmp) &&
                   super.getCase(tmp) != 0)
                tmp = super.getCase(super.getCase(tmp) - 1);
            break;
        default:
            throw new WrongOptionException(OptionPionCaseOie.class, option);
        }
        return tmp.getCase();
    }

    @Override
    public void recommencer() {
        super.recommencer();
        gameOverFired = false;
    }
}