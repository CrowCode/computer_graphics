import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GraphControl extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean m = false;
	int x, y, index;
	private Ellipse2D.Double first = new Ellipse2D.Double(0, 255, 10, 10);
	private Ellipse2D.Double last = new Ellipse2D.Double(255, 0, 10, 10);
	private ArrayList<Ellipse2D.Double> ellipseList = new ArrayList<Ellipse2D.Double>();
	private BufferedImage buffer;

	private JPanel panel;
	private Point[] ps ;

	public GraphControl() {
		super();
		panel = new JPanel();
		setPreferredSize(new Dimension(255, 255));
		
		init();

		setLayout(new FlowLayout());
		//add(panel,2);

		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {

			}

			@Override
			public void mouseDragged(MouseEvent e) {
				
				ellipseList.get(index).x = e.getX();
				ellipseList.get(index).y = e.getY();
				repaint();
			}
		});
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				boolean f = true;
				if (SwingUtilities.isLeftMouseButton(e) || e.isControlDown()) {
					
					for (Ellipse2D w : ellipseList) {
						if (w.contains(e.getX(), e.getY())) {
							f = false;
							index = ellipseList.indexOf(w);
							break;
						}
						else {
							for (Ellipse2D s : ellipseList) {
								if (e.getX() < s.getCenterX()) {
									index = ellipseList.indexOf(s);
									f = true;
									break;
								}
							}
						}
							
						
					}
					repaint();
					
					if (f) {
						
						 Ellipse2D.Double Pointer = new Ellipse2D.Double(e.getX(), e.getY(), 10,
								10);
						
						ellipseList.add(index, Pointer);
						
						repaint();
					}
				}
				if (SwingUtilities.isRightMouseButton(e) || e.isControlDown()) {
					for (Ellipse2D w : ellipseList) {
						if (w.contains(e.getX(), e.getY())) {
							
							index = ellipseList.indexOf(w);
							break;

						}

					}
					if (f) {
						ellipseList.remove(index);
						f = false;
						repaint();
					}
				}

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {

			}

		});

	}

	@Override
	protected void paintComponent(Graphics g) {

		if (buffer == null) {
			buffer = new BufferedImage(getWidth(), getHeight(),
					BufferedImage.TYPE_INT_BGR);
		}

		Graphics2D g2 = (Graphics2D) buffer.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(Color.white);
		g2.fillRect(0, 0, 255, 255);
		g2.setColor(Color.LIGHT_GRAY);

		g2.drawLine(0, 0, 0, 255);
		g2.drawLine(51, 0, 51, 255);
		g2.drawLine(102, 0, 102, 255);
		g2.drawLine(153, 0, 153, 255);
		g2.drawLine(204, 0, 204, 255);
		g2.drawLine(255, 0, 255, 255);

		g2.drawLine(0, 0, 255, 0);
		g2.drawLine(0, 51, 255, 51);
		g2.drawLine(0, 102, 255, 102);
		g2.drawLine(0, 153, 255, 153);
		g2.drawLine(0, 204, 255, 204);
		g2.drawLine(0, 255, 255, 255);

		g2.setColor(Color.black);
		Stroke l = g2.getStroke();
		Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);
		g2.setStroke(dashed);

		g2.drawLine(0, 0, 255, 255);
		g2.setStroke(l);
		g2.setColor(Color.red);
		
		
		g2.drawString(x + ", " + y, 10, 10);
		Ellipse2D.Double pre = null;
		for (Ellipse2D.Double w : ellipseList) {
			g2.fill(w);

			if (pre != null) {
				g2.drawLine((int) pre.getCenterX(), (int) pre.getCenterY(),
						(int) w.getCenterX(), (int) w.getCenterY());

			}
			pre = w;
		}

		g.drawImage(buffer, 0, 0, panel);
	}
	
	@Override
	public void update(Graphics g) {

		paint(g);
	}
	private void init() {
		first = new Ellipse2D.Double(0, 0, 10, 10);
		last = new Ellipse2D.Double(260, 260, 10, 10);
		ellipseList.add(first);
		ellipseList.add(last);
	}
	
	public void initNegative(int aX, int aY, int bX, int bY) {
		first = new Ellipse2D.Double(aX, aY, 10, 10);
		last = new Ellipse2D.Double(bX, bY, 10, 10);
		ellipseList.removeAll(ellipseList);
		ellipseList.add(first);
		ellipseList.add(last);
		repaint();
		
	}
	public void initBrightness(int aX, int aY, int bX, int bY) {
		Ellipse2D.Double a1 = new Ellipse2D.Double(aX, aY, 10, 10);
		Ellipse2D.Double a2 = new Ellipse2D.Double(bX, bY, 10, 10);
		ellipseList.removeAll(ellipseList);
		init();
		ellipseList.add(1, a1);
		ellipseList.add(2, a2);
	 	repaint();
		
	}
	public Point[] getPoints() {
		ps = new Point[ellipseList.size()];
		int i=0;
		for(Ellipse2D.Double a : ellipseList) {
			ps[i] = new Point((int) a.getX(), (int)a.getY()) ;
			
			
			i++;
			
		}
		return ps;
		
	}

}
