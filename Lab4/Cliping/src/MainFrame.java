import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	DrawingPanel drawingPanel;
	private static final long serialVersionUID = 1L;

	public MainFrame(String str) {

		setSize(new Dimension(800, 600));
		setTitle(str);
		setLocationRelativeTo(null);
		setJMenuBar(addMenuBar());
		setVisible(true);
		setLayout(new BorderLayout());

		drawingPanel = new DrawingPanel();
		add(drawingPanel, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private JMenuBar addMenuBar() {

		JMenuBar menuBar = new JMenuBar();
		JMenu shape = new JMenu("Shape");
		JMenu action = new JMenu("Action");
		JMenuItem clip = new JMenuItem("Clip");
		JMenuItem clear = new JMenuItem("Clear");
		JMenuItem fill = new JMenuItem("Fill");
		JMenu select = new JMenu("Select...");
		ButtonGroup myGroup = new ButtonGroup();
		JRadioButtonMenuItem item1 = new JRadioButtonMenuItem("Rectangle");
		JRadioButtonMenuItem item2 = new JRadioButtonMenuItem("Line");
		item1.setActionCommand("rectangle");
		item1.setSelected(true);
		myGroup.add(item1);
		select.add(item1);
		item2.setActionCommand("line");
		myGroup.add(item2);
		select.add(item2);
		action.add(clip);
		action.add(clear);
		action.add(fill);
		shape.add(select);
		
		
		menuBar.add(shape);
		menuBar.add(action);

		item1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ev) {
				String command = ev.getActionCommand().toString();

				drawingPanel.setShape(command);

			}
		});
		item2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ev) {
				String command = ev.getActionCommand().toString();

				drawingPanel.setShape(command);

			}
		});
		clear.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				drawingPanel.clearScr();
				
			}
		});
		clip.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				drawingPanel.applyClipping();
			}
		});
		fill.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				drawingPanel.setFill(true);
				
			}
		});
		return menuBar;
	}

}
