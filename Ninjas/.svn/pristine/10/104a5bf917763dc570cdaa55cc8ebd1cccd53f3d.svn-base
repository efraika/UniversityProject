package projet;

import java.util.Arrays;
import java.util.LinkedList;

import org.bytedeco.javacpp.opencv_core.Mat;

public class Menu {
	private LinkedList<Button> buttons;
	private Button selectedButton;
	private final Webcam webcam;
	private int rows, cols;
	
	public Menu(Webcam w, Button... b) {
		webcam = w;
		buttons = new LinkedList<Button>(Arrays.asList(b));
	}
	
	public Menu(Webcam w, int rows, int cols) {
		webcam = w;
		buttons = new LinkedList<Button>();
		this.rows = rows;
		this.cols = cols;
	}

	/**
	 * Detecte si l'un des boutons du menu est touche.
	 * Si c'est le cas on decremente le timer du bouton, sinon on reset les boutons.
	 */
	public void detect() {
		if (selectedButton == null) {
			for (Button b : buttons) {
				if (b.isTouched()) {
					selectedButton = b;
					selectedButton.decrease();
					break;
				}
			}
		}
		else if (selectedButton.isTouched())
			selectedButton.decrease();
		else {
			selectedButton.reset();
			selectedButton = null;
		}
	}
	
	/**
	 * Ajoute a l'image les boutons du menu.
	 * @param w
	 */
	public void addToWebcam() {
		for (Button b : buttons)
			webcam.add(b);
	}
	
	/**
	 * Renvoie vrai si l'un des boutons est termine.
	 * @return
	 */
	public boolean isOver() {
		 return selectedButton != null && selectedButton.isOver();
	}
	
	/**
	 * Renvoie le bouton touche par l'utilisateur, renvoie null si aucun n'est touche.
	 * @return
	 */
	public Button pressedButton() {
		if (isOver())
			return selectedButton;
		else
			return null;
	}
	
	/**
	 * Ajoute le Button b au Menu.
	 * @param b
	 */
	public void add(Button b) {
		buttons.add(b);
	}	
	
	public Button add(int row, int col, Mat img, String text) {
		Button b = new Button(webcam, col*webcam.width/cols, row*webcam.height/rows, webcam.width/cols, webcam.height/rows, img, text);
		add(b);
		return b;
	}
	
	/**
	 * Supprime le Button b du Menu.
	 * @param b
	 */
	public void remove(Button b) {
		buttons.remove(b);
	}
	
	/**
	 * Reset les boutons.
	 */
	public void reset() {
		selectedButton.reset();
		selectedButton=null;
	}
}