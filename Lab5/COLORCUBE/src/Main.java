
public class Main {

	public static void main(String[] args) {
		new Runnable() {
			
			@Override
			public void run() {
				new MainFrame();
				
			}
		}.run();;

	}

}
