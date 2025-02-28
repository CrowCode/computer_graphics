import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JFileChooser filechooser;
	private PictureBox pic;
	private JLabel inputPic;
	private JButton loadDitheringBtn;
	private JButton loadQuantizationBtn;
	private JButton resetBtn;
	private JTextField interText;
	GridBagConstraints constraints = new GridBagConstraints();

	void addGB(Component component, int x, int y) {
		constraints.gridx = x;
		constraints.gridy = y;
		add(component, constraints);
	}

	private void init() {
		setLayout(new GridBagLayout());
		constraints.weightx = 1.0;
		constraints.weighty = 0.1;
		constraints.fill = GridBagConstraints.BOTH;
		
		constraints.gridheight = 1;
		constraints.gridwidth = 3;
		addGB(inputPic,  0,  0);
		constraints.weightx = 4.0;
		constraints.weighty = 4.0;
		addGB(pic, 0, 1);
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.insets = new Insets(5, 5, 5, 5); // set padding
		addGB(resetBtn, 0, 2);
		addGB(loadDitheringBtn, 1, 2);
		addGB(loadQuantizationBtn, 2, 2);
		constraints.weightx = 1.0;
		constraints.weighty = 0.5;
		addGB(interText, 1, 3);
	}

	public MainFrame() {

		super("Image Filtering");
		setSize(800, 1000);

		pic = new PictureBox(700, 400);
		inputPic = new JLabel("Input Image:", SwingConstants.CENTER);
		resetBtn = new JButton("Reset");
		loadDitheringBtn = new JButton("Dithering");
		loadQuantizationBtn = new JButton("Quantization");
		interText = new JTextField();
		
		init();
		setJMenuBar(createMenuBar());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}

	private JMenuBar createMenuBar() {

		ImageFileFilter filter = new ImageFileFilter();
		JMenuBar menu = new JMenuBar();
		filechooser = new JFileChooser();
		filechooser.addChoosableFileFilter(filter);

		// Set up MenuBar.
		JMenu fileMenu = new JMenu("File");
		JMenuItem openItem = new JMenuItem("  Open  ");
		JMenuItem saveItem = new JMenuItem("  Save  ");
		JMenuItem exitItem = new JMenuItem("  Exit  ");

		menu.add(fileMenu);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);

		openItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (filechooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
					try {
						pic.loadImage(filechooser.getSelectedFile()
								.toString());

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

			}
		});

		saveItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				if (filechooser.showSaveDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
					try {
						pic.saveImage(filechooser.getSelectedFile()
								.toString());

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				
				
				
			}
		});

		resetBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					pic.loadImage(filechooser.getSelectedFile().toString());
					inputPic.setText("Input Image");
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		});

		loadDitheringBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (interText.getText() != null) {
					int foo = Integer.parseInt(interText.getText());
					
					pic.scaleImage(pic.arrayToImage(pic.randDithering(pic.getImage(), foo)));
					
					
				}
				inputPic.setText("Output Image");
			}
		});
		loadQuantizationBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (interText.getText() != null) {
					int foo = Integer.parseInt(interText.getText());
					
					pic.scaleImage(pic.arrayToImage(pic.kMeanQuantization(pic.getImage(), foo)));
					
				}
				inputPic.setText("Output Image");
			}
		});
		return menu;
	}

}
