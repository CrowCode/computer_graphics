import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.prefs.BackingStoreException;

import javax.swing.JPanel;


public class LineDrawing extends JPanel{
	
	private Point startingPoint;
	private Point endingPoint;
	
	private BufferedImage bi;
	private String currentObject = "";
	private int[][] panelArray;
	private int panelW = 800;
	private int panelH = 600;
	
	
	public LineDrawing(){
		
		setDoubleBuffered(true);
		startingPoint = null;
		endingPoint = null;
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(panelW, panelH));
		panelArray = new int[800][600];
		for (int i = 0; i < panelW; i++){
			for (int j = 0; j < panelH; j++) {
				panelArray[i][j] = 255 ;
			}
		}
		
		
		addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e){
				
				startingPoint = e.getPoint();
			}
			
			public void mouseReleased(MouseEvent e){
				
				startingPoint = null;
			}
		});
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				endingPoint = e.getPoint();
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				endingPoint = e.getPoint();
				repaint();
			}
		});
			
	}
	
	
	public void setCurrentObject(String s){
		currentObject = s;
	}
	
	public void update(Graphics g){
		paintComponent(g);
	}
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (startingPoint != null) {
        	for(int i = 0; i < panelW; i++){
        		for(int j = 0; j < panelH; j++){
        			panelArray[i][j] = 255 ;
        		}
        	}
        	
//        	g.setColor(Color.DARK_GRAY);
//        	g.drawLine(startingPoint.x, startingPoint.y, endingPoint.x, endingPoint.y);
        	drawLine(startingPoint.x, startingPoint.y, endingPoint.x, endingPoint.y);
        	bi = convertToBufferedImage(panelArray);
			g2.drawImage(bi, 0, 0, null);
        }
    }
	
	
	public void drawLine(int x1, int y1, int x2, int y2){
//		
//		int dx = x2 - x1;
//		int dy = y2 - y1;
//		int d = 2 * dy - dx;			//initial value of d
//		int dE = 2 * dy;			//increment used when moving to E
//		int dNE = 2 * (dy - dx);	//increment used when moving to NE
//		int x = x1;
//		int y = y1;
//		panelArray[x][y] = 33;
//		while(x < x2)
//		{
//			
//			if(d < 0)				//move to E
//			{
//				d += dE;
//				x++;
//			}
//			else					//move to NE
//			{
//				 d += dNE;
//				 ++x;
//				 ++y;
//			}
//			panelArray[x][y] = 33;
//		}
		
		  int w = x2 - x1 ;
		    int h = y2 - y1 ;
		    int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
		    if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
		    if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
		    if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
		    int longest = Math.abs(w) ;
		    int shortest = Math.abs(h) ;
		    if (!(longest>shortest)) {
		        longest = Math.abs(h) ;
		        shortest = Math.abs(w) ;
		        if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
		        dx2 = 0 ;            
		    }
		    int numerator = longest >> 1 ;
		    for (int i=0;i<=longest;i++) {
		    	panelArray[x1][y1] = 33;
		        numerator += shortest ;
		        if (!(numerator<longest)) {
		            numerator -= longest ;
		            x1 += dx1 ;
		            y1 += dy1 ;
		        } else {
		            x1 += dx2 ;
		            y1 += dy2 ;
		        }
		    }
	
	}
	
	public void drawCircle(int x1, int y1, int x2, int y2){
		
	}
	
	
	
	
	
	BufferedImage convertToBufferedImage(int [][] array){
		
		final BufferedImage outputImage = new BufferedImage(panelW, panelH, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) outputImage.getGraphics();
		
		for (int i = 0; i < panelW; i++){
			for (int j = 0; j < panelH; j++) {
				int a = array[i][j];
				g.setColor(new Color(a, a, a));
				g.fillRect(i, j, 1, 1);
			}
		}
//		System.out.println("CONVERTION TO BUFFEREDIMAGE DONE.");
		return outputImage;
	}

}
