import java.awt.image.BufferedImage;

public class Utils {

	public static String getExtintionOfFile(String name) {

		int PointIndex = name.lastIndexOf(".");

		if (PointIndex == -1) {
			return null;
		}
		if (PointIndex == name.length() - 1) {
			return null;
		}

		return name.substring(PointIndex + 1, name.length());

	}

	static int[][] convertTo2DUsingGetRGB(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] result = new int[height][width];

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				result[row][col] = image.getRGB(col, row);
				
			}
		}

		return result;
	}

}
