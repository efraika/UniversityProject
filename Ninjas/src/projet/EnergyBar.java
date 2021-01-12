package projet;

import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.Mat;
import static org.bytedeco.javacpp.opencv_imgproc.rectangle;
import static org.bytedeco.javacpp.opencv_core.CV_8UC3;
import static org.bytedeco.javacpp.opencv_imgproc.CV_AA;
import static org.bytedeco.javacpp.opencv_imgproc.CV_FILLED;
import static org.bytedeco.javacpp.opencv_core.Mat.zeros;

public class EnergyBar extends Showable {
	private final int maxEnergy;
	private int energy;
	private boolean used;
	private long[] ninjas;
	private final int nbNinjaCombo=5;
	private final int nbSecCombo=2;
	
	public EnergyBar(Webcam w) {
		super(w, w.width - 120, w.height - 30, 106, 16);
		maxEnergy = 10;
		energy = 0;
		used=false;
		ninjas=new long[nbNinjaCombo];
	}
	
	public Mat getImage() {
		Mat image = zeros(height, width, CV_8UC3).asMat();
		rectangle(image, new Rect(0, 0, width, height), Scalar.RED, 3, CV_AA, 0);
		rectangle(image, new Rect(3, 3, (width-6)*energy/maxEnergy, height-6), Scalar.GREEN, CV_FILLED, CV_AA, 0);
		return image;
	}
	
	/**
	 * Augmente l'energie de 1 si elle n'est pas deja au maximum et si un combo a eu lieu
	 */
	public void increase() {
		add();
		if (! isFull() && isCombo()) {
			energy++;
			ninjas=new long[nbNinjaCombo];
		}
	}
	
	/**
	 * Renvoie vrai si l'ecart de temps entre le nbNinjaCombo est inferieur ou egal a nbSecCombo.
	 * @return
	 */
	private boolean isCombo() {
		return Math.abs(ninjas[nbNinjaCombo-1]-ninjas[1])<=nbSecCombo*1000;
	}
	
	/**
	 * Ajoute le temps actuel dans le tableau des derniers ninjas touches
	 */
	private void add() {
		for(int i=0;i<nbNinjaCombo-1; i++) {
			ninjas[i]=ninjas[i+1];
		}
		ninjas[nbNinjaCombo-1]=System.currentTimeMillis();
	}
	/**
	 * Renvoie vrai si l'energie est au maximum.
	 * @return
	 */
	public boolean isFull() {
		return energy == maxEnergy;
	}
	
	/**
	 * Remet l'energie a 0.
	 */
	public void reset() {
		Utility.playSound("lightning.wav");
		energy = 0;
		used=false;
	}
	
	/**
	 * Met la valeur de used a true.
	 */
	public void use() {
		Utility.playSound("lightning.wav");
		used=true;
	}
	
	/**
	 * Renvoie vrai l'energie a ete utilisee, c'est a dire si used vaut true.
	 * @return
	 */
	public boolean isUsed() {
		return used;
	}
	
	/**
	 * Augmente l'energie de 1
	 */
	public void addEnergy() {
		if(!isFull()) energy ++;
	}
	
}
