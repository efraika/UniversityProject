package projet;

import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_UNCHANGED;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;

import org.bytedeco.javacpp.opencv_core.Mat;

public class Shape extends Showable {
	private Mat image; 
	
	public Shape(Webcam w) {
		super(w);
		image = imread("Images/visage.png", IMREAD_UNCHANGED);
		setSize(8*getImage().cols() * w.height / (720*5), 5*getImage().rows() * w.height /(4* 720));
		setLocation(w.width / 2 - (100 * w.height / 720), 100* w.height / 720);
	}
	
	public Mat getImage() {
		return image;
	}
	
}
