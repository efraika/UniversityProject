package projet;

import java.awt.Rectangle;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

import org.bytedeco.javacpp.opencv_core.Mat;

public class Controller {
	private Webcam webcam;
	private View window;
	private Locale locale;
	private ResourceBundle buttonImagesBundle, textsBundle;

	public Controller() {
		webcam = new Webcam();
		window = new View();
		locale = Locale.getDefault();
		if (!locale.getLanguage().equals("fr") && !locale.getLanguage().equals("en"))
			locale = Locale.FRENCH;
		buttonImagesBundle = ResourceBundle.getBundle("projet.ButtonImagesBundle", locale);
		textsBundle = ResourceBundle.getBundle("TextsBundle", locale);
	}

	/**
	 * Ajoute un bouton de type key au Menu menu dans la ligne et la colonne donn�es en argument.
	 * @param menu
	 * @param row
	 * @param col
	 * @param key
	 * @return
	 */
	private Button addToMenu(Menu menu, int row, int col, String key) {
		return menu.add(row, col, (Mat) buttonImagesBundle.getObject(key), textsBundle.getString(key));
	}
	
	/**
	 * Displays a board which explain who made this project and why it have been made.
	 */
	private void credits() {
		Menu menu = new Menu(webcam, 7, 5);
		addToMenu(menu, 0, 2, "back_to_menu");
		Text text1=new Text(webcam, 0, webcam.height /6, webcam.width, webcam.height / 6,
				textsBundle.getString("credits1"));
		Text text2=new Text(webcam, 0, webcam.height * 1/3, webcam.width, webcam.height / 6,
				textsBundle.getString("credits2"));
		Text text3=new Text(webcam, 0, webcam.height /2, webcam.width, webcam.height / 6,
				textsBundle.getString("credits3"));
		Text text4=new Text(webcam, 0, webcam.height * 2/3, webcam.width, webcam.height / 6,
				textsBundle.getString("credits4"));
		while (!menu.isOver()) {
			runMenu(menu);
			webcam.add(text1);
			webcam.add(text2);
			webcam.add(text3);
			webcam.add(text4);
			window.showImage(webcam.getImage());
		}
		menu();
	}

	/**
	 * Displays a scoreboard and buttons to play again or go back to the main menu.
	 */
	private void gameOver(int round) {
		Utility.playSound("game-over.wav");
		ScoreTab scoretab = new ScoreTab(webcam);
		Menu menu = new Menu(webcam, 7, 7);
		Button rejouer = addToMenu(menu, 5, 2, "play_again");
		Button retour = addToMenu(menu, 5, 4, "menu");
		scoretab.setScore(round);
		while (!menu.isOver()) {
			webcam.read();
			webcam.showFirstImage();
			webcam.add(scoretab);
			menu.detect();
			menu.addToWebcam();

			window.showImage(webcam.getImage());
		}
		if (menu.pressedButton() == rejouer)
			inGame();
		else if (menu.pressedButton() == retour)
			menu();
	}

	/**
	 * Gere le deroulement du jeu et son affichage.
	 */
	private void inGame() {
	
		Background background = new Background(webcam);
		Chest chest = new Chest(webcam);
		EnergyBar energyBar = new EnergyBar(webcam);
		Round round = new Round(1, webcam, chest);
		Button pause = new Button(webcam, webcam.width / 2, 0, webcam.height / 15, webcam.height / 15, Button.PAUSE,
				"err",2);
		Button pause2 = new Button(webcam, 0, 0, webcam.height /15, webcam.height /15, Button.PAUSE, "err",2);
		Button power = new Button(webcam, 8 * webcam.width / 10, 7 * webcam.height / 10, webcam.width / 8,
				webcam.height / 10, Button.LIGHTNING, "err", 8);
		Menu menu = new Menu(webcam, pause);
		Menu menu2 = new Menu(webcam, pause2);
		myRunnable mr = new myRunnable(false);
		int cheat=0;
		final int cheatMax=10; 
		while (!chest.empty()) {
			if (webcam.read()) {
				if (mr.test && !mr.b) {
					new Thread(mr).start();
				} else {
					
				if(!mr.b) {
					chest.modifyCoins(mr.brick.getCoinsWon());
					mr.reset();
					chest.setRayon(webcam, round.getRound());
					
					if(isCheating(chest)) {
						if(cheat>cheatMax) cheat();
						else cheat++;
					}
					else cheat=0;
				if (round.getListe().isEmpty()) {
					round.addNinjas(webcam, chest);
					Random rand = new Random();
					int a = rand.nextInt(10);
					if(a == 1) {
						mr.test = true;
					}
					round.nextRound();	
				}
				for (Iterator<Ninja> iterator = round.getListe().iterator(); iterator.hasNext();) {
					Ninja ninja = iterator.next();
					if (ninja.isOut() && (ninja.isDead() || ninja.hasCoin()))
						iterator.remove();
					else {
						if (!ninja.isDead() && !ninja.hasCoin() && ninja.intersects(chest)) {
							ninja.takeCoin();
							chest.modifyCoins(-1);
						}
						if (!ninja.isOut()) {
							if (!ninja.isDead() && !ninja.hasCoin() && ninja.isTouched()) {
								ninja.hit();
								energyBar.increase();
							}
							webcam.add(ninja);
						}
						ninja.move(chest);
					}
				}
			
				if (energyBar.isFull() && !energyBar.isUsed()) {
					menu.add(power);
					Utility.playSound("lightning.wav");
					energyBar.use();
				}

				if (menu.isOver()) {
					if (menu.pressedButton() == power) {
						Utility.playSound("lightning.wav");
						for (Ninja ninja : round.getListe()) {
							if (!ninja.isOut())
								ninja.hit();
						}
						menu.remove(power);
						menu.reset();
						energyBar.reset();
					} else if (menu.pressedButton() == pause && menu2.pressedButton()==pause2)
						pause();
				}
					webcam.add(background);
					webcam.add(round);
					webcam.add(chest);
					webcam.add(energyBar);
					menu.detect();
					menu.addToWebcam();
					menu2.detect();
					menu2.addToWebcam();
					window.showImage(webcam.getImage());
				}
				}
			}
		}
		gameOver(round.getRound());
	}

	/**
	 * Initializes color detection.
	 */
	private void initialization() {
		Text text1 = new Text(webcam, 0, webcam.height * 2/3, webcam.width, webcam.height / 6,
				textsBundle.getString("initialization1"));
		Text text2 = new Text(webcam, 0, webcam.height * 5/6, webcam.width, webcam.height / 6,
				textsBundle.getString("initialization2"));
		Menu menu = new Menu(webcam, 5, 5);
		addToMenu(menu, 3, 2, "menu");
		Rectangle rect = new Rectangle(webcam.width*3/5, webcam.height - webcam.height * 2/5, webcam.width/8, webcam.height/16);
		long startTime = System.nanoTime();
		double waitingTime = 5e9;
		while (System.nanoTime() - startTime < waitingTime) {
			webcam.read();
			webcam.rectangleDetection(rect);
			webcam.add(new Shape(webcam));
			webcam.add(text1);
			webcam.add(text2);
			window.showImage(webcam.getImage());
		}
		webcam.read();
		webcam.change();
		webcam.detectColor(rect);
		startTime = System.nanoTime();
		waitingTime = 3e9;
		while (System.nanoTime() - startTime < waitingTime) {
			webcam.read();
			webcam.testColor();
			window.showImage(webcam.getImage());
		}
		while (!menu.isOver()) {
			webcam.read();
			menu.detect();
			menu.addToWebcam();
			webcam.testColor();
			window.showImage(webcam.getImage());
		}
		menu();
	}

	/**
	 * Displays the main menu containing the following buttons:
	 * Play, Options, Credits, Tutorial and Quit.
	 */
	private void menu() {
		Menu menu = new Menu(webcam, 5, 5);
		Button play = addToMenu(menu, 1, 1, "play");
		Button options = addToMenu(menu, 1, 3, "options");
		Button credits = addToMenu(menu, 3, 1, "credits");
		Button tutorial = addToMenu(menu, 2, 2, "tutorial");
		addToMenu(menu, 3, 3, "quit");

		while (!menu.isOver()) {
			runMenu(menu);
			window.showImage(webcam.getImage());
		}
		if (menu.pressedButton() == play)
			inGame();
		else if (menu.pressedButton() == options)
			options();
		else if (menu.pressedButton() == credits)
			credits();
		else if (menu.pressedButton() == tutorial) {
			tutorial();
			menu();
		}
	}

	/**
	 * Displays a menu to change color detection and language options.
	 */
	private void options() {
		Menu menu = new Menu(webcam, 5, 5);
		Button changeColor = addToMenu(menu, 1, 1, "change_color");
		Button language = menu.add(1, 3, null, locale.getLanguage());
		Button retour = addToMenu(menu, 3, 2, "back_to_menu");
		while (!menu.isOver()) {
			runMenu(menu);
			window.showImage(webcam.getImage());
		}
		if (menu.pressedButton() == retour)
			menu();
		else if (menu.pressedButton() == changeColor)
			initialization();
		else if (menu.pressedButton() == language) {
			locale = (locale.getLanguage().equals("fr")) ? Locale.ENGLISH : Locale.FRENCH;
			buttonImagesBundle = ResourceBundle.getBundle("projet.ButtonImagesBundle", locale);
			textsBundle = ResourceBundle.getBundle("TextsBundle", locale);
			options();
		}
	}

	/**
	 * Affiche le menu pause
	 */
	private void pause() {
		Menu menu = new Menu(webcam, 5, 7);
		menu.add(2, 3, Button.PAUSE, "err");
		while (!menu.isOver()) {
			runMenu(menu);
			window.showImage(webcam.getImage());
		}
	}
	
	private boolean isCheating(Chest chest) {
		return chest.isCheating();
	}
	
	/**
	 * Shows when isCheating
	 */
	private void cheat() {
		Text text = new Text(webcam, 0, webcam.height * 2/3, webcam.width, webcam.height / 6,
				textsBundle.getString("cheat"));
		Menu menu = new Menu(webcam, 5, 7);
		menu.add(2, 3, Button.PAUSE, "err");
		while (!menu.isOver()) {
			runMenu(menu);
			webcam.add(text);
			window.showImage(webcam.getImage());
		}
	}

	/**
	 * Runs the game.
	 */
	public void run() {
		if (webcam.isOpened()) {
			initialization();
			window.dispose();
			webcam.close();
		} else
			System.out.println("Error: Webcam not opened");
	}

	/**
	 * Ajoute le menu a l'ecran et detecte les interactions avec les boutons
	 * 
	 * @param menu
	 */
	private void runMenu(Menu menu) {
		webcam.read();
		menu.detect();
		menu.addToWebcam();
	}
	
	/**
	 * Affiche un tutoriel passant de niveau en niveau apres un temps d'attente ou une action de la part du joueur.
	 */
	private void tutorial() {
		Menu menu = new Menu(webcam, 5, 5);
		Button retour = addToMenu(menu, 4, 4, "back_to_menu");
		int niveau = 0, i=0;
		boolean end=false;
		long start=System.currentTimeMillis();
		long timeWait=5000;
		Chest coffre= new Chest(webcam);
		Ninja ninja=new Ninja(webcam,1, coffre,1);
		Button pause1 = new Button(webcam, webcam.width / 2, 0, webcam.height / 15, webcam.height / 15, Button.PAUSE,"err",2);
		Button pause2 = new Button(webcam, 0, 0, webcam.height /15, webcam.height /15, Button.PAUSE, "err",2);
		Menu menu1= new Menu(webcam, pause1);
		Menu menu2= new Menu(webcam, pause2);
		EnergyBar energy= new EnergyBar(webcam);
		Button power = new Button(webcam, 8 * webcam.width / 10, 7 * webcam.height / 10, webcam.width / 8,
				webcam.height / 10, Button.LIGHTNING, "err", 8);
		Brick brick= new Brick(webcam);
		myRunnable mr= new myRunnable(false);
		while (!end) {
			runMenu(menu);
			switch (niveau) {
			case 0:
				if(Math.abs(System.currentTimeMillis()-start)>timeWait) {
					niveau++;
					start=System.currentTimeMillis();
				}
				webcam.add(new Text(webcam, 0, webcam.height*1/3, webcam.width, webcam.height *1/6, "Bienvenue dans le tutoriel !"));
				break;
			case 1:
				if(Math.abs(System.currentTimeMillis()-start)>timeWait) {
					niveau++;
				}
				webcam.add(coffre);
				webcam.add(new Text(webcam, 0, webcam.height*1/3, webcam.width, webcam.height *1/6, "Votre but est de proteger les pieces, du coffre en bas de l'ecran, des ninjas ennemis"));
				break;
					
			case 2:
				webcam.add(coffre);
				if(!ninja.isOut()) {
					if( ninja.isTouched()) {
						niveau ++;
						start=System.currentTimeMillis();
					}
					webcam.add(ninja);
				}
				ninja.move(coffre);
				webcam.add(new Text(webcam,0,webcam.height*1/3, webcam.width, webcam.height *1/6, "Attention voici un ninja! Essayer de le toucher avec la couleur que vous avez choisi"));
				break;
			case 3:
				if(Math.abs(System.currentTimeMillis()-start)>timeWait) {
					niveau++;
					ninja= new Ninja(webcam, 2,coffre,3);
				}
				webcam.add(new Text(webcam, 0, webcam.height*1/3, webcam.width, webcam.height *1/6, "Bravo vous avez reussi a chasser un ninja!"));
				webcam.add(new Text(webcam, 0, webcam.height*1/3, webcam.width, webcam.height * 1/2, "Cependant certains ninjas sont plus coriaces et il faut les frapper plusieurs fois"));
				break;
			case 4:
				webcam.add(coffre);
				if(!ninja.isOut()) {
					webcam.add(ninja);
					if(ninja.isTouched()) 
						ninja.hit();
					if(ninja.isDead()) {
						coffre.setRayon(webcam, 8);
						start=System.currentTimeMillis();
						niveau++;
					}
				}
				ninja.move(coffre);
				break;
			case 5:
				if(Math.abs(System.currentTimeMillis()-start)>timeWait*3/2) {
					niveau++;
				}
				webcam.add(new Text(webcam, 0,webcam.height*1/3, webcam.width, webcam.height*1/6, "Maintenant que vous avez compris comment combattre les ninjas, passons a une autre mecanique."));
				webcam.add(new Text(webcam, 0,webcam.height*1/3, webcam.width, webcam.height*1/2, "Pour augmenter le niveau de difficulte le coffre se met a bouger, comme ceci."));
				webcam.add(coffre);
				break;
			case 6:
				menu1.detect();
				menu1.addToWebcam();
				menu2.detect();
				menu2.addToWebcam();
				if(menu1.isOver() && menu2.isOver()) {
					start=System.currentTimeMillis();
					niveau ++;
				}
				webcam.add(new Text(webcam,0,webcam.height*1/3, webcam.width, webcam.height*1/6, "Vous pouvez a tout moment mettre le jeu en pause en touchant les deux boutons pause en meme temps."));
				webcam.add(new Text(webcam,0,webcam.height*1/3, webcam.width, webcam.height*1/2, "Essayez de mettre le jeu en pause."));
				break;
			case 7:
				if(Math.abs(System.currentTimeMillis()-start)>timeWait*3/2) {
					niveau++;
					menu.add(power);
					i=0;
				}else {
					if(i%35==0) energy.addEnergy();
					i++;
				}
				webcam.add(energy);
				webcam.add(new Text(webcam, 0, webcam.height*1/3, webcam.width, webcam.height*1/6, "En bas de l'ecran vous avez une barre d'energie qui se remplit lorsque vous faites des combos."));
				webcam.add(new Text(webcam,0, webcam.height*1/3, webcam.width, webcam.height*1/2, "Vous faites un combo lorsque vous toucher au moins 5 ninjas en moins de 2 secondes."));
				break;
			case 8:
				menu.detect();
				if(menu.pressedButton()==power) {
					menu.remove(power);
					Utility.playSound("lightning.wav");
					start=System.currentTimeMillis();
					niveau++;
				}
				webcam.add(new Text(webcam, 0, webcam.height*1/3, webcam.width, webcam.height*1/6, "Lorsque votre energie est pleine vous pouvez activer votre pouvoir en appuyant sur le bouton qui vient d'apparaitre."));
				webcam.add(new Text(webcam, 0, webcam.height*1/3, webcam.width, webcam.height*1/2, "Ce pouvoir permet de tuer tous les ninjas presents a l'ecran. Essayez de l'utiliser."));
				break;
			case 9:
				if(Math.abs(System.currentTimeMillis()-start)>timeWait*3/2) {
					niveau++;
				}
				webcam.add(brick);
				webcam.add(new Text(webcam,0,webcam.height*1/3, webcam.width, webcam.height*1/6, "De maniere aleatoire une brique apparait sur l'ecran comme maintenant."));
				webcam.add(new Text(webcam,0,webcam.height*1/3, webcam.width, webcam.height*1/2, "En appuyant dessus vous pouvez gagner des pieces qui s'ajouteront a celles de votre coffre."));
				break;
			case 10:
				webcam.add(brick);
				if (brick.isTouched()) {
					brick.setCanBeTouched(false);
					i++;
					if(i==3) niveau ++;
				}
				brick.goUpAndDown(webcam);
				webcam.add(new Text(webcam, 0, webcam.height*1/3, webcam.width, webcam.height*1/6, "Essayer de gagner 1 piece"));
				break;
			case 11:
				webcam.add(new Text(webcam,0,webcam.height*1/3, webcam.width, webcam.height*1/6, "Felicitations, vous avez fini le tutoriel !"));
				webcam.add(new Text(webcam,0,webcam.height*1/3, webcam.width, webcam.height*1/2, "Vous pouvez desormais retourner au menu et lancer une partie en appuyant sur jouer."));
				break;
			default: 
				webcam.add(new Text(webcam,0, webcam.height*1/3, webcam.width, webcam.height *1/6, "Une erreur est survenue. Essayez de retourner au menu puis de revenir au tutoriel."));
				break;
			}
			window.showImage(webcam.getImage());
			
			if (menu.pressedButton() == retour)
			break;
		}
	}
	
	/**
	 * Classe utilisee pour la gestion de la brique
	 */
	class myRunnable implements Runnable{
		private boolean b;
		private boolean test = false;
		private Brick brick;
		Background background = new Background(webcam);
		public myRunnable(boolean b) {
			this.b = b;
			brick = new Brick(webcam);
		}
		public void run() {
			b = true;
			int count = 0;
			while(count < 300) {
				count ++ ;
				try {
					Thread.sleep(5);
					webcam.add(background);
					webcam.add(brick);
					if (brick.isTouched()) {
						brick.setCanBeTouched(false);
					}
					brick.goUpAndDown(webcam);
					window.showImage(webcam.getImage());
				}catch(Exception e) {
					
				}
			}
			b= false;
			test = false;		
		}
		public void reset() {
			brick = new Brick(webcam);
		}
		public void setBoolean(boolean b) {
			this.b = b;
		}
		
	}
}