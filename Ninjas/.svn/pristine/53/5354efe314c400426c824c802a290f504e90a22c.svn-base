package projet;

import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_UNCHANGED;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;

import java.util.Random;

import org.bytedeco.javacpp.opencv_core.Mat;

public class Brick extends Showable {
	private Mat image; 
	private boolean canbetouched;
	private int centery;
	private int timer;
	private boolean monte;
	private int coinswon;
	
	public Brick(Webcam w) {
		super(w);
		image = imread("Images/brique.png", IMREAD_UNCHANGED);
		centery =  w.height / 10;
		setSize(100 * w.height / 720 ,100 * w.height / 720);
		setLocation(w.width / 2,  w.height / 10);
		canbetouched = true;
		monte = true;
		coinswon = 0;
		timer = 0;
	}
	
	/**
	 * Incremente timer de 1.
	 */
	public void addTimer() {
		timer ++;
	}
	
	/**
	 * Renvoie vrai si timet vaut 100 et met timer a 0, renvoie faux sinon.
	 * @return
	 */
	public boolean finish() {
		if (timer == 100) {
			timer = 0;
			return true;
		}
		return false;
	}
	
	public Mat getImage() {
		return image;
	}
	
	/**
	 *Joue un son si canbetouched vaut vrai et modifie canbetouched.
	 * @param b
	 */
	public void setCanBeTouched(boolean b) {
		if(canbetouched) {
			Utility.playSound("piece.wav");
		}
		canbetouched = b;
	}
	
	/**
	 * @return coinswon
	 */
	public int getCoinsWon() {
		return coinswon;
	}
	
	/**
	 * Fais monter et descendre la brique ou la deplace sur l'ecran.
	 * @param w
	 */
	public void goUpAndDown(Webcam w) {
		if(!canbetouched) {
			if(monte && this.y > centery - 50 * w.height / 720) {
				this.y -= 10* w.height / 720;
			}else if(this.y == centery - 50 * w.height / 720) {
				monte = false;
				this.y +=10* w.height / 720;
			}else if(this.y < centery  && !monte) {
				this.y += 10 * w.height / 720;
			}else {
				coinswon += 1;
				monte = true;
				canbetouched = true;
				int newx = new Random().nextInt(w.width / 6);
				int newy = new Random().nextInt(w.height / 3);
				this.x =(w.width / 6) + newx * 4;
				this.y =(w.height/3) + (newy );
				centery =( w.height/3) + (newy );
			}
		}
	}	
}
