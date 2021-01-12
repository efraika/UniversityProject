package projet;

import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_UNCHANGED;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;

import java.util.ListResourceBundle;

public abstract class ButtonImagesBundle extends ListResourceBundle {
	private String[] keys = {"back_to_menu", "change_color", "credits",
			"menu",	"options", "play", "play_again", "quit", "tutorial"};
	private Object[][] contents = new Object[keys.length][2];
	
	public Object[][] getContents() {
		for (int i = 0; i < contents.length; i++) {
			if (contents[i][0] == null) {
				contents[i][0] = keys[i];
				contents[i][1] = imread("Images/Buttons/" + getLocale().getLanguage() + "/" + keys[i] + ".png", IMREAD_UNCHANGED);
			}
		}
		return contents;
	}
}