package projet;

import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import static org.bytedeco.javacpp.opencv_core.extractChannel;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_UNCHANGED;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.opencv_core.Size;


public class HeadSave extends Showable {
	private final Mat image;

	public HeadSave(Webcam w, Mat test) {
		super(w);
		Mat a = webcam.getImage();
		Rect r = new Rect(w.width / 3, w.height / 10, w.width / 3, w.height / 2);
		image = new Mat(test,r);
		setSize(r.width()*8/10, r.height()*8/10);
		setLocation(w.width / 3 + (50 * w.height / 720), 100 * w.height / 720);
	}
	
	public Mat getImage() {
		return image;
	}

}