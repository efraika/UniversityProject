package projet;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.Size;

import static org.bytedeco.javacpp.opencv_imgproc.putText;
import static org.bytedeco.javacpp.opencv_imgproc.getTextSize;
import static org.bytedeco.javacpp.opencv_core.CV_8UC4;
import static org.bytedeco.javacpp.opencv_core.FONT_HERSHEY_SIMPLEX;
import static org.bytedeco.javacpp.opencv_imgproc.CV_AA;

/**
 * A box containing text to display on a webcam feed.
 */
public class Text extends Showable {
	private Mat image;
	private String text;
	
	/**
	 * Creates a box with no text.
	 */
 	public Text(Webcam w, int x, int y, int width, int height) {
		super(w, x, y, width, height);
	}
	
	public Text(Webcam w, int x, int y, int width, int height, String text) {
		this(w, x, y, width, height);
		setText(text);
	}
	
	public Mat getImage() {
		return image;
	}
	
	/**
	 * Modifies the text contained in the box.
	 * @param text - new text
	 */
	public void setText(String text) {
		if (text.isEmpty())
			image = null;
		else if (! text.equals(this.text)) {
			this.text = text;
			text = " " + text + " ";
			int fontFace = FONT_HERSHEY_SIMPLEX;
			Size textSize = getTextSize(text, fontFace, 1, 0, new int[1]);
			double fontScale = Math.min((double)width/textSize.width(), (double)height/textSize.height()/2);
			int[] baseline = {0};
			textSize = getTextSize(text, fontFace, fontScale, 0, baseline);
			
			image = new Mat(height, width, CV_8UC4, Scalar.all(0));
			Point textOrg = new Point((width-textSize.width()) / 2,	(height+textSize.height()-baseline[0]) / 2);
			putText(image, text, textOrg, fontFace, fontScale, new Scalar(0, 0, 255, 255), 0, CV_AA, false);
		}
	}
}