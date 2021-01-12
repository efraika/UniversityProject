package projet;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;

public class View extends CanvasFrame {
	private OpenCVFrameConverter.ToMat converter;
	
	public View() {
		super("Titre");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		converter = new OpenCVFrameConverter.ToMat();
	}
	
	public void showImage(Mat image) {
		showImage(converter.convert(image));
	}
}
