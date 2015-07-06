import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ImageFileFilter extends FileFilter {

	@Override
	public boolean accept(File f) {

		String name = f.getName();

		String fileExtention = Utils.getExtintionOfFile(name);
		if (f.isDirectory()) {
			return true;
		}
		if (fileExtention == null) {
			return false;
		}
		if (fileExtention.equals("jpg") || fileExtention.equals("png")) {
			return true;
		}
		return false;

	}
	
	@Override
	public String getDescription() {

		return "Picture files (*.JPG, *.PNG)";
	}

}
