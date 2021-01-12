package projet;

import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import static org.bytedeco.javacpp.opencv_core.extractChannel;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_UNCHANGED;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;

/**
 * A chest containing coins.
 */
public class Chest extends Showable {
	private final Mat image;
	private int coins;
	private Mat[] digitImages;

	//Devra être changé selon la difficulté
	private int rayoncoffre;
	private int centerx;
	private int centery;
	//Rapidité du déplacement du coffre
	private int coeffdiff = 1;
	private boolean canchangecoeff;
	public Chest(Webcam w) {
		super(w);
		image = imread("Images/chest.png", IMREAD_UNCHANGED);
		coins = 25;
		digitImages = new Mat[10];
		for (int i = 0; i < 10; i++)
			digitImages[i] = imread("Images/number" + i + ".png", IMREAD_UNCHANGED);
		
		setSize(image.cols() * w.height / 720, image.rows() * w.height / 720);
		setLocation((webcam.width - width) / 2, (webcam.height - height) - webcam.height/10);
		rayoncoffre = 0;
		centerx = this.x;
		centery = this.y;
		canchangecoeff = true;
	}	
	
	public Mat getImage() {
		if(this.y == centery && this.x > this.centerx - rayoncoffre) {
			this.x -= ((10* webcam.height / 720)*coeffdiff);
		}else if(this.x == this.centerx - rayoncoffre &&  this.y > centery - rayoncoffre && rayoncoffre != 0) {
			this.y -= ((10* webcam.height / 720)*coeffdiff);
		}else if(this.y == centery - rayoncoffre && this.x < centerx + rayoncoffre) {
			this.x += ((10* webcam.height / 720)*coeffdiff);
		}else if(rayoncoffre != 0){
			this.y += ((10* webcam.height / 720)*coeffdiff);
		}
		
		Mat copy = image.clone();
		for(int i = 0; i < 3; i++) {
			//the i-th digit of "coins" counting from the right
			Mat digit = digitImages[coins / (int)Math.pow(10, i) % 10];
			Mat alpha = new Mat(digit.size(), CV_8UC1);
			extractChannel(digit, alpha, 3);
			digit.copyTo(copy.apply(new Rect(70-i*25, 90, digit.cols(), digit.rows())), alpha);
		}
		return copy;
	}
	
	/**
	 * Modifies the number of coins in the chest.
	 * @param n - number of coins to add to the chest (can be negative).
	 */
	public void modifyCoins(int n) {
		coins += n;
		if (coins < 0)
			coins = 0;
	}
	
	/**
	 * Returns whether the chest is empty.
	 * @return true if the number of coins in the chest is equal to zero; false otherwise.
	 */
	public boolean empty() {
		return coins == 0;
	}
	
	/**
	 * Modifie rayon en fonction de l'entier passe en parametre
	 * @param w
	 * @param diff
	 */
	public void setRayon(Webcam w, int diff) {
		if(coeffdiff%4 == 0) {
			coeffdiff = 1;
		}
		if(diff%9==0) {
			if(canchangecoeff && coeffdiff < 2) {
				coeffdiff++;
				canchangecoeff = false;
			}
	
			rayoncoffre = 0;
			this.x = centerx;
			this.y = centery;
		}else {
			if(!canchangecoeff) {
				canchangecoeff = true;
			}
			if(diff%9 == 1 || diff%9 == 2 || diff%9 == 3) {
				rayoncoffre = 0;
			}else if(diff%9==4 || diff%9 == 5 || diff%9 == 6) {
				rayoncoffre = ((w.width / 7) * 1) - ((w.width / 7) * 1)%20;
			}else {
				rayoncoffre = ((w.width / 7) * 2) - ((w.width / 7) * 2)%20;
			}
		}

		
	}
	
	/**
	 * test if the player is cheating
	 * @return true if is cheating
	 */
	public boolean isCheating() {
		int pixels = 0;
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				if (webcam.isCouleur(y + row, x + col)) {
					pixels++;
					if (pixels > width*height/2) return true;
				}
			}
		}return false;
	}
	
}
