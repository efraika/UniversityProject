package projet;

import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_UNCHANGED;
import static org.bytedeco.javacpp.opencv_core.flip;

import java.util.LinkedList;
import java.util.Random;

import org.bytedeco.javacpp.opencv_core.Mat;

/**
 * A ninja that tries to steal coins from the chest.
 */
public class Ninja extends Showable {
	private int hp;
	private boolean hasCoin;
	private int difficulty;
	private int imgNb;
	private static Mat[] runningLeftImgTwo = new Mat[6];
	private static Mat[] runningRightImgTwo = new Mat[6];
	private static Mat[] runningLeftImg = new Mat[6];
	private static Mat[] runningRightImg = new Mat[runningLeftImg.length];
	private static Mat[] hitLeftImg = new Mat[6];
	private static Mat[] hitRightImg = new Mat[6];
	private boolean fromleft;
	static {
		for (int i= 0; i < runningLeftImgTwo.length; i++) {
			runningLeftImgTwo[i] = imread("Images/Ninjas/flyingFaridah.png", IMREAD_UNCHANGED);
			runningRightImgTwo[i] = new Mat();
			flip(runningLeftImgTwo[i], runningRightImgTwo[i], 1);
		}
		for (int i = 0; i < runningLeftImg.length; i++) {
			runningLeftImg[i] = imread("Images/Ninjas/runningFaridah" + (i+1) + ".png", IMREAD_UNCHANGED);
			runningRightImg[i] = new Mat();
			flip(runningLeftImg[i], runningRightImg[i], 1);
		}
		for(int i = 0 ; i < hitLeftImg.length ; i++ ) {
			hitLeftImg[i] = imread("Images/" + (i%3+1) + "f.png", IMREAD_UNCHANGED);
			hitRightImg[i] = new Mat();
			flip(hitLeftImg[i], hitRightImg[i], 1);
		}
	}
	private Mat[] images;
	private int dx, dy;
	private static LinkedList<Double> combo;
	private boolean touched;
	private int count = 0;
	
	
	
	public Ninja(Webcam w, int diff, Chest coffre, int hp) {
		super(w);
		touched = false;
		this.hp = hp;
		hasCoin = false;
		difficulty = diff;
		imgNb = 0;
		
		setSize(w.height / 10, w.height / 8);

		Random rand = new Random();
		do {
			setLocation(rand.nextInt(2*xMax()) - xMax()/2, rand.nextInt(3*yMax()/2) - yMax()/2);
		} while (! isOut());
		
		int n = rand.nextInt(2);
		if (x < w.width / 2) {
			fromleft = true;
			if( n == 1) {
				images = runningRightImgTwo.clone();
				
			}else {
				images = runningRightImg.clone();
			}
			
		}else {
			fromleft = false;
			if(n==1) {
				images = runningLeftImgTwo.clone();
			}else {
				images = runningLeftImg.clone();
			}
			
		}
		
	}

	public Mat getImage() {
		return images[imgNb];
	}
	
	/**
	 * Renvoie la coordonee maximale possible des ninjas en x. 
	 */
	private int xMax() {
		return webcam.width - width;
	}
	
	/**
	 * Renvoie la coordonee maximale possuble des ninjas en y.
	 * @return
	 */
	private int yMax() {
		return webcam.height - height;
	}
	
	/**
	 * Moves the ninja in the correct direction depending on its state.
	 */
	public void move(Chest chest) {
		if(touched) {
			if(count != 200) {
				count += 10;
				if(x > webcam.width / 2) {
					this.x += 10;
					this.y +=10;
				}else {
					this.x -= 10;
					this.y -=10;
				}
			}else if(count == 200) {
				touched = false;
				count = 0;
				
				if (this.x < webcam.width / 2) {
					images = runningRightImg.clone();
				}else {
					images = runningLeftImg.clone();
				}
			}

		}else {
			
		if (isDead())
			dy += acceleration();
		else if (! hasCoin()) {
			dx = 2 * (chest.x - x) + chest.width - width;
			dy = 2 * (chest.y - y) + chest.height - height;
			double k = runningSpeed() / Math.sqrt(dx * dx + dy * dy);
			dx *= k;
			dy *= k;
		}
		
		translate(dx, dy);
		imgNb = (imgNb + 1) % images.length;
		}
	}
	
	/**
	 * @return The distance covered by the running ninja in one frame.
	 */
	private int runningSpeed() {
		return difficulty * webcam.height / 40;
	}
	
	/**
	 * @return The initial distance covered by the falling ninja in one frame.
	 */
	private int fallingSpeed() {
		return webcam.height / 20;
	}
	
	/**
	 * @return The vertical acceleration of the falling ninja.
	 */
	private int acceleration() {
		return webcam.height / 320;
	}
	
	/**
	 * Enleve un hp au ninja et s'il est mort modifie sa trajectoire pour l'envoyer dans la trajectoire dans laquelle il a ete touche.
	 */
	public void hit() {
		if(!touched) {
			touched = true;
			images = hitLeftImg.clone();
		Utility.playSound("hitFaridah" + (new Random().nextInt(6)+1) + ".wav");
		if (hp > 0) hp--;
		if (isDead()) {
			for(int i=0; i<images.length; i++) {
				images[i] = imread("Images/" + (i%3+1) + "f.png", IMREAD_UNCHANGED);
			}
			dx = dy = 0;
			if (isTouchedTop())
				dy = 1;
			else if (isTouchedBottom())
				dy = -1;
			if (isTouchedLeft())
				dx = 1;
			else if (isTouchedRight())
				dx = -1;
			double k = fallingSpeed() / Math.sqrt(dx * dx + dy * dy);
			dx *= k;
			dy *= k;
		}
		}
	}
	
	/**
	 * Renvoie vrai si les hp du ninja valent 0.
	 * @return
	 */
	public boolean isDead() {
		return hp == 0;
	}
	
	/**
	 * Renvoie vrai si le ninja est en dehors de l'�cran.
	 * @return
	 */
	public boolean isOut() {
		return x < 0 || x > xMax() || y < 0 || y > yMax();
	}
	
	public static boolean combo() {
		return false;
	}
	
	/**
	 * Retourne l'image du ninja pour le faire partir dans la direction opposee,
	 * Met le boolean hasCoin a true, et modifie dy et dx pour changer la trajectoire du ninja.
	 */
	public void takeCoin() {
		Utility.playSound("piece.wav");
		dx *= -1;
		dy *= -1;
		if(fromleft) {
			images = runningLeftImg.clone();
		}else {
			images = runningRightImg.clone();
		}
		hasCoin = true;
	}
	
	/**
	 * Renvoie vrai si le ninja a pris une pi�ce dans le coffre, cad si le boolean hasCoin vaut true.
	 * @return
	 */
	public boolean hasCoin() {
		return hasCoin;
	}
	
}