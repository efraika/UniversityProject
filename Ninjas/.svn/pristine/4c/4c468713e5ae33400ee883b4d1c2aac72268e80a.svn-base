package projet;

import org.bytedeco.javacpp.opencv_core.Mat;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_UNCHANGED;

/**
 * A background image to display on a webcam feed.
 */
public class Background extends Showable {
	private Mat image;
	
	public Background(Webcam w) {
		super(w, 0, 0, w.width, w.height);
		image = imread("Images/background.png", IMREAD_UNCHANGED);
	}
	
	public Mat getImage() {
		return image;
	}
}
