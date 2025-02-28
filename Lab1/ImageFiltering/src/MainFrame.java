import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int bvalue;
	static final int FPS_MIN = -255;
	static final int FPS_MAX = 255;
	static final int FPS_INIT = 0;
	static final int CPS_MIN = 0;
	static final int CPS_MAX = 255;
	static final int CPS_INIT = 0;
	private JComboBox<String> FiltersCombo;
	private JFileChooser filechooser;
	private ImagePanel picPanel;
	private JLabel inputPic;
	private JLabel filters;
	private JButton loadBtn;
	private GraphControl Gc;
	private JButton resetBtn;
	private JTextField gamaco;
	
	private JSlider sliderBrigthness = new JSlider(JSlider.VERTICAL, FPS_MIN,
			FPS_MAX, FPS_INIT);
	private JSlider sliderContrast = new JSlider(JSlider.VERTICAL, CPS_MIN,
			CPS_MAX, CPS_INIT);

	GridBagConstraints constraints = new GridBagConstraints();

	public String getFilter() {
		return FiltersCombo.getSelectedItem().toString();
	}

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
		int x, y; // for clarity
		constraints.gridheight = 1;
		constraints.gridwidth = 5;
		addGB(inputPic, x = 0, y = 0);
		constraints.weighty = 10.0;
		// constraints.weightx = 3.0;
		addGB(picPanel, x = 0, y = 1);
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		constraints.insets = new Insets(10, 10, 0, 0); // set padding
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0.1;
		constraints.weighty = 0.1;
		constraints.anchor = GridBagConstraints.LINE_START;
		addGB(filters, x = 0, y = 2);
		// constraints.weightx = 0.5;
		addGB(FiltersCombo, x = 0, y = 3);
		addGB(resetBtn, x = 0, y = 4);
		addGB(gamaco,x=0, y=5);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 2.0;
		constraints.gridheight = 4;
		constraints.gridwidth = 1;
		constraints.weightx = 1;
		addGB(Gc, x = 3, y = 2);
		constraints.weightx = 0.1;
		constraints.anchor = GridBagConstraints.PAGE_START;
		addGB(sliderBrigthness, x = 2, y = 2);
		addGB(sliderContrast, x = 1, y = 2);

		constraints.insets = new Insets(0, 0, 0, 0);
		// addGB(l, x = 2, y = 2);
		constraints.gridheight = 1;
		constraints.gridwidth = 5;
		constraints.weighty = 0.1;
		addGB(loadBtn, x = 0, y = 6);

	}

	@SuppressWarnings("deprecation")
	public MainFrame() {

		super("Image Filtering");
		setSize(800, 1000);
		gamaco = new JTextField();
		gamaco.setPreferredSize(new Dimension(100,50));
		
		FiltersCombo = new JComboBox<String>();
		picPanel = new ImagePanel();
		inputPic = new JLabel("Input Image:", SwingConstants.CENTER);
		resetBtn = new JButton("Reset");
		filters = new JLabel("Filters:");
		loadBtn = new JButton("Load");
		Gc = new GraphControl();
		// Set up Combo box.
		DefaultComboBoxModel<String> FilterModel = new DefaultComboBoxModel<>();
		FilterModel.addElement("Negation");
		FilterModel.addElement("Contrast");
		FilterModel.addElement("Brightness");
		FilterModel.addElement("Blure");
		FilterModel.addElement("Gaussion Smoothing");
		FilterModel.addElement("Sharpen");
		FilterModel.addElement("Edge detection(H)");
		FilterModel.addElement("Edge detection(V)");
		FilterModel.addElement("Edge detection(A)");
		FilterModel.addElement("Edge detection(L)");
		FilterModel.addElement("Emboss(S)");
		FilterModel.addElement("Emboss(E)");
		FilterModel.addElement("Emboss(S-E)");
		FilterModel.addElement("Function");
		FilterModel.addElement("Gamacurrection");
		FiltersCombo.setModel(FilterModel);
		init();
		sliderBrigthness.setMajorTickSpacing(51);
		sliderBrigthness.setMinorTickSpacing(51);
		sliderBrigthness.setPaintTicks(true);
		sliderBrigthness.setPaintLabels(true);
		//sliderBrigthness.disable();
		sliderContrast.setMajorTickSpacing(51);
		sliderContrast.setMinorTickSpacing(51);
		sliderContrast.setPaintTicks(true);
		sliderContrast.setPaintLabels(true);
		//sliderContrast.disable();
		setJMenuBar(createMenuBar());

		// initComponents();

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

		sliderBrigthness.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				
				bvalue = sliderBrigthness.getValue() > 255 ? 255
						: sliderBrigthness.getValue();
				if (bvalue > 0)
					Gc.initBrightness(0, bvalue, 245 - bvalue, 245);
				else
					Gc.initBrightness(Math.abs(bvalue), 0, 245,
							245 - Math.abs(bvalue));
			}
		});
		sliderContrast.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				
				bvalue = sliderContrast.getValue() > 255 ? 255
						: sliderContrast.getValue();
				bvalue = sliderContrast.getValue()< 0 ? (1/Math.abs(bvalue)) : bvalue;
				if (bvalue > 0)
					Gc.initBrightness(0, bvalue,  245, 245 - bvalue);
				else
					Gc.initBrightness(Math.abs(bvalue), 0,245 - Math.abs(bvalue), 245
							);
			}
		});
		openItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (filechooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {

					try {
						picPanel.loadImage(filechooser.getSelectedFile()
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
						picPanel.saveImage(filechooser.getName().toString());

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
					picPanel.loadImage(filechooser.getSelectedFile()
							.toString());

				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
			}
		});
		
		
		
		loadBtn.addActionListener(new ActionListener() {

			
			@Override
			public void actionPerformed(ActionEvent e) {

				String value = (String) FiltersCombo.getSelectedItem();
				System.out.println(value);
				for (int i = 0; i < Gc.getPoints().length; i++) {
					System.out.println(Gc.getPoints()[i]);
				}
				// Gc.getPoints();
				try {
					switch (value) {
					case "Negation": {
						// Gc.initNegative(255, 0, 0, 255);
						Point pp[] = Gc.getPoints();
						picPanel.getImage(picPanel.NegativeFilter());
						break;
					}

					case "Brightness": {
						
						
						picPanel.getImage(picPanel.Brightness(bvalue));
						break;
					}

					case "Contrast": {
						
						//Point pp[] = Gc.getPoints();
						picPanel.getImage(picPanel.ContrastFilter(bvalue));
						break;
					}
					case "Blure": {
						picPanel.getImage(picPanel.blureFilter());
						break;
					}
					case "Sharpen": {
						picPanel.getImage(picPanel.sharpenFilter());
						break;
					}
					case "Gaussion Smoothing": {
						picPanel.getImage(picPanel.guassionFilter());
						break;
					}
					case "Edge detection(H)": {
						picPanel.getImage(picPanel.edgeHFilter());
						break;
					}
					case "Edge detection(V)": {
						picPanel.getImage(picPanel.edgeVFilter());
						break;
					}
					case "Edge detection(A)": {
						picPanel.getImage(picPanel.edgeAFilter());
						break;
					}
					case "Edge detection(L)": {
						picPanel.getImage(picPanel.edgeLFilter());
						break;
					}
					case "Emboss(S)": {
						picPanel.getImage(picPanel.edgeLFilter());
						break;
					}
					case "Emboss(E)": {
						picPanel.getImage(picPanel.edgeLFilter());
						break;
					}
					case "Emboss(S-E)": {
						picPanel.getImage(picPanel.edgeLFilter());
						break;
					}
					case "Function": {
						Point[] b = Gc.getPoints();
						picPanel.getImage(picPanel.funcFilter(b));
						break;
					}
					case "Gamacurrection": {
						float a = Float.valueOf(gamaco.getText());
						
						picPanel.getImage(picPanel.gammaFilter(a));
						break;
					}
					}

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		return menu;
	}

}
