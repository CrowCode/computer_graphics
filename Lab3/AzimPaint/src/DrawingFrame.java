import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class DrawingFrame extends JFrame {

	private JPanel contentPane;
	private JSpinner objectSpinner;
	private SpinnerListModel objectSpinnerModel;
	private GridBagConstraints menuGBC;
	
	
	private Color menuBgColor = new Color(133,27,76);
	private int frameW = 800;
	private int frameH = 600;
	private String[] objects = {"line", "Circle", "more"};
	private String currentObject = "";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DrawingFrame frame = new DrawingFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DrawingFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, frameW, frameH);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		
						/*	lineDrawingPanel--------------------LineDrawing	*/
		
		final LineDrawing lineDrawingPanel = new LineDrawing();
		contentPane.add(lineDrawingPanel);
		
				
						/*	menuPanel-------------------------JPanel	*/
		
		JPanel menuPanel = new JPanel();
		menuPanel.setBackground(menuBgColor);
		menuPanel.setPreferredSize(new Dimension(frameW, frameH / 7));
		contentPane.add(menuPanel, BorderLayout.PAGE_END);

		
						/*	objectSpinner-----------------------JSpinner	*/
		
		objectSpinnerModel = new SpinnerListModel(objects);
		objectSpinner = new JSpinner(objectSpinnerModel);
		//objectSpinner.setBounds(0, 0, 60, 10);
		objectSpinner.setPreferredSize(new Dimension(100, 20));
		JFormattedTextField cy = ((JSpinner.DefaultEditor)objectSpinner.getEditor()).getTextField();
		cy.setForeground(Color.WHITE);
		cy.setBackground(menuBgColor);
		cy.setHorizontalAlignment(JFormattedTextField.LEFT);
		cy.setFocusable(false);
		objectSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				currentObject = objectSpinner.getValue().toString();
				lineDrawingPanel.setCurrentObject(currentObject);
				System.out.println(">> Selected object "+currentObject);
			}
		});
		
		
						/*		tempBtn------------------------JButton	*/
		
		JButton tempBtn = new JButton(" TEMP BTN ");
		tempBtn.setBackground(Color.WHITE);
		tempBtn.setBorder(null);
		
		
		/************************ Add components to menuPanel ********************/
		
		GridBagLayout gbl_menuPanel = new GridBagLayout();
		menuPanel.setLayout(gbl_menuPanel);
		menuGBC = new GridBagConstraints();
		menuGBC.insets = new Insets(5, 1, 0, 1);
		
		
		menuGBC.gridx = 0;
		menuGBC.gridy = 0;
		menuGBC.gridheight = 1;
		menuGBC.gridwidth = 3;
		menuGBC.fill = GridBagConstraints.HORIZONTAL;
		menuPanel.add(objectSpinner, menuGBC);
		
		menuGBC.gridx = 3;
		menuGBC.gridy = 0;
		menuGBC.gridheight = 1;
		menuGBC.gridwidth = 1;
		menuGBC.fill = GridBagConstraints.HORIZONTAL;
		menuPanel.add(tempBtn, menuGBC);
	}

}
