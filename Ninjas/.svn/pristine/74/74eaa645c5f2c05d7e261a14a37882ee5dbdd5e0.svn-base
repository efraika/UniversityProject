package projet;

import java.awt.Rectangle;
import org.bytedeco.javacpp.opencv_core.Mat;

/**
 * An element that can be displayed on a webcam feed and interact with it.
 */
public abstract class Showable extends Rectangle {
	protected final Webcam webcam;
	
	public Showable(Webcam w, int x, int y, int width, int height) {
		super(x, y, width, height);
		webcam = w;
	}
	
	public Showable(Webcam w, int x, int y) {
		this(w, x, y, 0, 0);
	}
	
	public Showable(Webcam w) {
		this(w, 0, 0);
	}
	
	/**
	 * Returns the image of the element.
	 * @return a CV_8UC3 or CV_8UC4 BGR image of the same size as the Showable element.
	 */
	public abstract Mat getImage();
	
	/**
	 * Returns whether the Showable element is touched by the player.
	 * @return true if there is a green object at the position of the Showable element in the webcam image; false otherwise.
	 */
	public boolean isTouched() {
		return isTouched(0, width, 0, height);
	}
	
	protected boolean isTouched(int xMin, int xMax, int yMin, int yMax) {
		int pixels = 0;
		for (int col = xMin; col < xMax; col++) {
			for (int row = yMin; row < yMax; row++) {
				if (webcam.isCouleur(y + row, x + col)) {
					pixels++;
					if (pixels > 10) return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Renvoie vrai si l'element est touche a gauche.
	 * @return
	 */
	public boolean isTouchedLeft() {
		return isTouched(0, width/3, 0, height);
	}
	
	/**
	 * Renvoie vrai si l'element est touche a droite.
	 * @return
	 */
	public boolean isTouchedRight() {
		return isTouched(2*width/3, width, 0, height);
	}
	
	/**
	 * Renvoie vrai si l'element est touche en haut.
	 * @return
	 */
	public boolean isTouchedTop() {
		return isTouched(0, width, 0, height/3);
	}
	
	/**
	 * Renvoie vrai si l'element est touche en bas.
	 * @return
	 */
	public boolean isTouchedBottom() {
		return isTouched(0, width, height*2/3, height);
	}
}
