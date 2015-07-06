import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JPanel;

public class DrawingPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Shape> shapes = new ArrayList<Shape>();
	ArrayList<Point> startPoints = new ArrayList<Point>();
	ArrayList<Point> endPoints = new ArrayList<Point>();
	Point startDrag, endDrag;
	String shape = "rectangle";
	int left, right, top, bottom;
	float tL, tE;
	BufferedImage image;
	Graphics2D g2;
	Point s;
	public boolean fill = false;

	public boolean isFill() {
		return fill;
	}

	public void setFill(boolean fill) {
		this.fill = fill;
		repaint();
	}

	public DrawingPanel() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				startDrag = new Point(e.getX(), e.getY());
				startPoints.add(startDrag);
				endDrag = startDrag;
				s = new Point(e.getX(), e.getY());
				repaint();
			}

			public void mouseReleased(MouseEvent e) {
				Shape r = null;
				endPoints.add(new Point(e.getX(), e.getY()));
				if (shape.equals("rectangle")){
					r = makeRectangle(startDrag.x, startDrag.y, e.getX(),
							e.getY());
					left = startDrag.x;
					top = startDrag.y;
					right = e.getX();
					bottom = e.getY();
					
				}
				else if (shape.equals("line"))
					r = makeLine(startDrag.x, startDrag.y, e.getX(), e.getY());
				shapes.add(r);
				startDrag = null;
				endDrag = null;
				repaint();
			}

		});

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				endDrag = new Point(e.getX(), e.getY());
				repaint();
			}
		});

	}

	private void paintBackground(Graphics2D g2) {
		g2.setPaint(Color.WHITE);
		g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));

	}

	public void paint(Graphics g) {
		image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
		g2 = (Graphics2D) image.getGraphics();

		
		
		
//		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);
		paintBackground(g2);
		if(fill)
		floodFill(g2);
		g2.setStroke(new BasicStroke(2));
//		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
//				0.50f));

		for (Shape s : shapes) {
			g2.setPaint(Color.BLACK);
			g2.draw(s);
		}

		if (startDrag != null && endDrag != null) {
			g2.setPaint(Color.BLACK);

			switch (shape) {
			case "line":
				Shape l = makeLine(startDrag.x, startDrag.y, endDrag.x,
						endDrag.y);
				g2.draw(l);
				break;
			case "rectangle":
				Shape r = makeRectangle(startDrag.x, startDrag.y, endDrag.x,
						endDrag.y);
				g2.draw(r);
				break;
			}

		}
		g.drawImage(image, 0, 0, null);
	}

	private Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2) {
		return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2),
				Math.abs(x1 - x2), Math.abs(y1 - y2));
	}

	private Line2D.Float makeLine(int x1, int y1, int x2, int y2) {
		return new Line2D.Float(x1, y1, x2, y2);
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public void clearScr() {
		shapes.removeAll(shapes);
		startPoints.removeAll(startPoints);
		endPoints.removeAll(endPoints);
		repaint();
	}

	public boolean Clip(float denom, float numer) {
		if (denom == 0) { // Paralel line
			if (numer > 0)
				return false; // outside - discard
			return true; // skip to next edge
		}
		float t = numer / denom;
		if (denom < 0) { // PE
			if (t > tL)  // tE > tL - discard
				return false;
			if (t > tE)
			{
				tE = t;
				System.out.println("tE :"+tE);
			}
		} else { // denom < 0 - PL
			if (t < tE) // tL < tE - discard
				return false; 
			if (t < tL)
			{
				tL = t;
				System.out.println("tL :"+tL);
			}
		}
		return true;
	}

	void LiangBarsky(Point p1, Point p2, Rectangle2D.Float clip) {
		
		float dx = p2.x - p1.x, dy = p2.y - p1.y;
		/*
		if(p1.x<left){
			p1.x = left;
			p1.y = (int)((dy/dx)*(p1.x-p2.x)+p2.y);
		}
		
		if(p1.x>right){
			p1.x = right;
			p1.y = (int)((dy/dx)*(p1.x-p2.x)+p2.y);
		}
		
		if(p1.y<top){
			p1.y = top;
			p1.x = (int)((dx/dy)*(p1.y-p2.y)+p2.x);
		}
		
		if(p1.y>bottom){
			p1.y = bottom;
			p1.x = (int)((dx/dy)*(p1.y-p2.y)+p2.x);
		}
		
		if(p2.x>right){
			p2.x = right;
			p2.y = (int)((dy/dx)*(p2.x-p1.x)+p1.y);
		}
		
		if(p2.x<left){
			p2.x = left;
			p2.y = (int)((dy/dx)*(p2.x-p1.x)+p1.y);
		}
		
		if(p2.y>bottom){
			p2.y=bottom;
			p2.x = (int)((dx/dy)*(p2.y-p1.y)+p1.x);
		}
		
		if(p2.y<top){
			p2.y = top;
			p2.x = (int)((dx/dy)*(p2.y-p1.y)+p1.x);
		}*/
		
		
		tE = 0; tL = 1;
		if (Clip((-1)*dx, p1.x - left))
			if (Clip(dx, right - p1.x))
				if (Clip(dy, bottom - p1.y))
					if (Clip((-1)*dy, p1.y - top)) {
						if (tL < 1) {
							p2.x = (int) (p1.x + dx * tL);
							p2.y = (int) (p1.y + dy * tL);
							System.out.println("YES");
						}
						if (tE > 0) {
							p1.x += dx * tE;
							p1.y += dy * tE;
							System.out.println("YES");
						}
						shapes.remove(1);
						shapes.add(makeLine(p1.x, p1.y, p2.x, p2.y));
					}
//		shapes.remove(1);
//		shapes.add(makeLine(p1.x, p1.y, p2.x, p2.y));
	
		repaint();
	}
	
	public void applyClipping(){
		
		
		Rectangle2D.Float clip = (Rectangle2D.Float) shapes.get(0);
		
		for(int i=1; i<shapes.size(); i++){
			Point p1 = startPoints.get(i);
			Point p2 = endPoints.get(i);
			LiangBarsky(p1, p2, clip);
		}
		
		
		
	}
	
	public void floodFill(Graphics2D g2) {

	    LinkedList<Point> stack = new LinkedList<Point>();
	   

	    
	   
	    stack.add(new Point(s.x,s.y)); // adding the point where the mouse was clicked.

	    Point temp;
	    Rectangle2D.Float clip = (Rectangle2D.Float) shapes.get(0);
	   
	    while( !stack.isEmpty() ){
	    	
	    	
	        temp = stack.pop();

	        int pixelColorRGB = image.getRGB( (int)temp.getX(), (int)temp.getY() );
	        
	        Color pixelColor = new Color(pixelColorRGB, true);

	        if(pixelColor.equals(Color.WHITE)){
	        	
	        	
	            g2.setColor(Color.BLACK);
	            g2.fillRect((int)temp.getX(), (int)temp.getY(), 1, 1);
	           
	            if(clip.contains((int) temp.getX() - 1, (int) temp.getY()))
	                stack.add( new Point( (int) temp.getX() - 1, (int) temp.getY() ) );

	            if(clip.contains((int) temp.getX() + 1, (int) temp.getY()))
	                stack.add( new Point( (int) temp.getX() + 1, (int) temp.getY() ) );

	            if(clip.contains((int) temp.getX(), (int) temp.getY() - 1))
	                stack.add( new Point( (int) temp.getX(), (int) temp.getY() - 1 ) );

	            if(clip.contains((int) temp.getX(), (int) temp.getY() + 1))
	                stack.add( new Point( (int) temp.getX(), (int) temp.getY() + 1 ) );

	        }
	        
	       
	    }
	    
	}
	
}
