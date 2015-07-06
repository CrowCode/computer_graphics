
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
}
