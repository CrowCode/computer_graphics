import java.awt.BorderLayout;

import javax.jws.Oneway;
import javax.swing.JFrame;



public class MainFrame extends JFrame {

	private DrawingPanel panel = new DrawingPanel();
	
	public MainFrame() {
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
		panel.requestFocus();
		setVisible(true);
		
	}

}
