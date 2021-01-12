package projet;

import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import static org.bytedeco.javacpp.opencv_core.extractChannel;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_UNCHANGED;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.opencv_core.Size;


public class ScoreTab extends Showable {
	private Mat image;
	private int coin;
	private Mat[] count;
	private int score;
	
	public ScoreTab(Webcam w) {
		super(w);
		//image = imread("Images/cadre.png", IMREAD_UNCHANGED);
		image = imread("Images/cadretest.png", IMREAD_UNCHANGED);
		setSize(getImage().cols() * w.height / 720, getImage().rows() * w.height / 720);
		setLocation(w.width/5, 30 * w.height / 720);
	
		count = new Mat[3];
	}

	
	public Mat getImage() {
		Mat copy = image.clone();

		Mat digitu = imread("Images/number" + score%10 + ".png", IMREAD_UNCHANGED);
		Rect rectu = new Rect(image.cols() / 4, image.rows() / 4 , image.cols() / 10, image.cols() / 8);
		opencv_imgproc.resize(digitu, digitu, rectu.size());
		
		//Mat digitd = imread("Images/number" + String.valueOf(score / 10) + ".png", IMREAD_UNCHANGED);
		Mat alphau = new Mat(digitu.size(), CV_8UC1);
		extractChannel(digitu, alphau, 3);
		digitu.copyTo(copy.apply(rectu), alphau);
		
		Mat digitd = imread("Images/number" + score/10 + ".png", IMREAD_UNCHANGED);
		Rect rectd = new Rect(image.cols() / 6, image.rows() / 4 , image.cols() / 10, image.cols() / 8);
		opencv_imgproc.resize(digitd, digitd, rectd.size());
		
		//Mat digitd = imread("Images/number" + String.valueOf(score / 10) + ".png", IMREAD_UNCHANGED);
		Mat alphad = new Mat(digitd.size(), CV_8UC1);
		extractChannel(digitd, alphad, 3);
		digitd.copyTo(copy.apply(rectd), alphad);
		return copy;
	}

	public void setScore(int n) {
		score = n;
	}
}
