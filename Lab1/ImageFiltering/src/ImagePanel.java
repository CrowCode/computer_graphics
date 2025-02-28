import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Image scaledImage;
	private int panelW = 500;
	private int panelH = 400;

	// constructor
	public ImagePanel() {
		super();

		setPreferredSize(new Dimension(panelW, panelH));

	}

	public void loadImage(String file) throws IOException {
		Image image = ImageIO.read(new File(file));
		scaleImage(image);
		repaint();
	}

	// e.g., containing frame might call this from formComponentResized
	public void scaleImage(Image image) {
		setScaledImage(image);

	}

	// override paintComponent
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (scaledImage != null) {

			g.drawImage(scaledImage,
					(getWidth() / 2) - (scaledImage.getWidth(this) / 2), 0,
					this);
		}
	}

	private void setScaledImage(Image image) {
		if (image != null) {

			// use floats so division below won't round
			float iw = image.getWidth(this);
			float ih = image.getHeight(this);
			float pw = panelW; // panel width
			float ph = panelH; // panel height

			if (pw < iw || ph < ih) {

				/*
				 * compare some ratios and then decide which side of image to
				 * anchor to panel and scale the other side (this is all based
				 * on empirical observations and not at all grounded in theory)
				 */

				if ((pw / ph) > (iw / ih)) {
					iw = -1;
					ih = ph;
				} else {
					iw = pw;
					ih = -1;
				}

				// prevent errors if panel is 0 wide or high
				if (iw == 0) {
					iw = -1;
				}
				if (ih == 0) {
					ih = -1;
				}

				scaledImage = image.getScaledInstance(new Float(iw).intValue(),
						new Float(ih).intValue(), Image.SCALE_DEFAULT);

			} else {
				scaledImage = image;
			}

		}
	}

	public void getImage(int pixels[][]) {
		int w = pixels.length;
		int h = pixels[0].length;
		BufferedImage image = new BufferedImage(h, w,
				BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {

				image.setRGB(j, i, pixels[i][j]);
			}
		}
		scaleImage(image);
		repaint();

	}

	public void saveImage(String name) {
		File output = new File(name);
		BufferedImage img = new BufferedImage(scaledImage.getWidth(null),
				scaledImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
		g.drawImage(scaledImage, 0, 0, null);
		g.dispose();

		try {
			ImageIO.write(img, "jpg", output);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*-------------------------- Brightness ------------------------*/
	public int[][] Brightness(int a) throws IOException {

		BufferedImage image = new BufferedImage(scaledImage.getWidth(null),
				scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bGr = image.createGraphics();
		bGr.drawImage(scaledImage, 0, 0, null);
		bGr.dispose();

		int PicArr[][] = Utils.convertTo2DUsingGetRGB(image);
		for (int i = 0; i < PicArr.length; i++) {
			for (int j = 0; j < PicArr[0].length; j++) {

				Color c = new Color(PicArr[i][j]);
				int pixelG = c.getGreen()+a;
				int pixelB = c.getBlue()+a;
				int pixelR = c.getRed()+a;

				pixelG = pixelG < 255 ? pixelG : 255;
				pixelB = pixelB < 255 ? pixelB : 255;
				pixelR = pixelR < 255 ? pixelR : 255;
				pixelG = pixelG > 0 ? pixelG : 0;
				pixelB = pixelB > 0 ? pixelB : 0;
				pixelR = pixelR > 0 ? pixelR : 0;
				
				c = new Color(pixelR, pixelG, pixelB);
				int p = c.getRGB();
				PicArr[i][j] = p;
			}
		}

		return PicArr;

	}

	/*---------------------- Inversion ------------------------*/
	public int[][] NegativeFilter() throws IOException {

		BufferedImage image = new BufferedImage(scaledImage.getWidth(null),
				scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bGr = image.createGraphics();
		bGr.drawImage(scaledImage, 0, 0, null);
		bGr.dispose();
		int PicArr[][] = Utils.convertTo2DUsingGetRGB(image);

		for (int i = 0; i < PicArr.length; i++) {
			for (int j = 0; j < PicArr[0].length; j++) {
				Color c = new Color(PicArr[i][j]);
				int pixelG = 255 - c.getGreen();
				int pixelB = 255 - c.getBlue();
				int pixelR = 255 - c.getRed();
				pixelG = pixelG < 255 ? pixelG : 255;
				pixelB = pixelB < 255 ? pixelB : 255;
				pixelR = pixelR < 255 ? pixelR : 255;
				pixelG = pixelG > 0 ? pixelG : 0;
				pixelB = pixelB > 0 ? pixelB : 0;
				pixelR = pixelR > 0 ? pixelR : 0;
				c = new Color(pixelR, pixelG, pixelB);
				int p = c.getRGB();

				PicArr[i][j] = p;

			}
		}
		return PicArr;
	}

	/*---------------------- Contrast ------------------------*/
	public int[][] ContrastFilter(int m) throws IOException {

		BufferedImage image = new BufferedImage(scaledImage.getWidth(null),
				scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bGr = image.createGraphics();
		bGr.drawImage(scaledImage, 0, 0, null);
		bGr.dispose();
		int PicArr[][] = Utils.convertTo2DUsingGetRGB(image);
		//int m=((func[2].x - func[1].x) / (func[2].y - func[1].y));
		if (m==0) m=1;
		for (int i = 0; i < PicArr.length; i++) {
			for (int j = 0; j < PicArr[0].length; j++) {
				Color c = new Color(PicArr[i][j]);
				int pixelG = (m) * c.getGreen();
				int pixelB = (m) * c.getBlue();
				int pixelR = (m) * c.getRed();
				pixelG = pixelG < 255 ? pixelG : 255;
				pixelB = pixelB < 255 ? pixelB : 255;
				pixelR = pixelR < 255 ? pixelR : 255;
				pixelG = pixelG > 0 ? pixelG : 0;
				pixelB = pixelB > 0 ? pixelB : 0;
				pixelR = pixelR > 0 ? pixelR : 0;
				c = new Color(pixelR, pixelG, pixelB);
				int p = c.getRGB();

				PicArr[i][j] = p;
			}
		}
		return PicArr;
	}

	/*------------------------ Conv Blure -----------------------*/

	public int[][] blureFilter() throws IOException {

		BufferedImage image = new BufferedImage(scaledImage.getWidth(null),
				scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bGr = image.createGraphics();
		bGr.drawImage(scaledImage, 0, 0, null);
		bGr.dispose();
		int PicArr[][] = Utils.convertTo2DUsingGetRGB(image);
		int kernel[][] = { { -1, 1, 0 }, { -7, 0, 10 }, { -6, -3, 9 } };
		int fpic[][] = new int[PicArr.length][PicArr[0].length];
		int pixelG = 0;
		int pixelB = 0;
		int pixelR = 0;
		int spixelG = 0;
		int spixelB = 0;
		int spixelR = 0;
		int row;
		int col;
		Color c;
		for (int i = 0; i < PicArr.length ; i++) {
			for (int j = 0; j < PicArr[0].length ; j++) {

				spixelG = 0;
				spixelB = 0;
				spixelR = 0;
				
				for (int k = -1; k < kernel.length - 1; k++) {
					for (int t = -1; t < kernel[0].length - 1; t++) {
						
						col = j+k;
						row = i+k;
						if(row<0) 
							row = PicArr.length-1;
						else if(row>PicArr.length-1)
							row=0;
						if (col<0)
							col =PicArr[0].length-1;
						else if (col >PicArr[0].length-1)
							col = 0;
							
						
						//System.out.println(row+"  "+col);
						c = new Color(PicArr[row][col]);
						pixelG = c.getGreen();
						pixelB = c.getBlue();
						pixelR = c.getRed();
						spixelG += pixelG * kernel[k + 1][t + 1];
						spixelB += pixelB * kernel[k + 1][t + 1];
						spixelR += pixelR * kernel[k + 1][t + 1];
					}
				}
				spixelG /= 3;
				spixelB /= 3;
				spixelR /= 3;
				
				spixelG = spixelG < 255 ? spixelG : 255;
				spixelB = spixelB < 255 ? spixelB : 255;
				spixelR = spixelR < 255 ? spixelR : 255;
				
				spixelG = spixelG > 0 ? spixelG : 0;
				spixelB = spixelB > 0 ? spixelB : 0;
				spixelR = spixelR > 0 ? spixelR : 0;
				
				System.out.println(spixelG + "   " + spixelB + "  " + spixelR);
				c = new Color(spixelR, spixelG, spixelB);
				fpic[i][j] = c.getRGB();
			}
		}

		return fpic;

	}

	public int[][] funcFilter(Point[] func) throws IOException {

		BufferedImage image = new BufferedImage(scaledImage.getWidth(null),
				scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bGr = image.createGraphics();
		bGr.drawImage(scaledImage, 0, 0, null);
		bGr.dispose();

		int PicArr[][] = Utils.convertTo2DUsingGetRGB(image);

		int k = 0;
		for (int i = 0; i < PicArr.length; i++) {
			for (int j = 0; j < PicArr[0].length; j++) {
				Color c = new Color(PicArr[i][j]);
				int pixelG = c.getGreen();
				int pixelB = c.getBlue();
				int pixelR = c.getRed();
				for (k = 0; k < func.length; k++) {
					if (func[k].x > pixelG) {
						break;
					}

				}
				if ((func[k].y - func[k - 1].y)==0)
					System.out.println("infinite sloope!");
					
				if (k == func.length)
					k--;
				pixelG = ((func[k].x - func[k - 1].x) / (func[k].y - func[k - 1].y))
						* (pixelG - func[k - 1].y) + func[k - 1].x;
				System.out.println(pixelG);
				for (k = 0; k < func.length; k++) {
					if (func[k].x > pixelB) {
						break;
					}
				}
				if (k == func.length)
					k--;
				pixelB = ((func[k].x - func[k - 1].x) / (func[k].y - func[k - 1].y))
						* (pixelB - func[k - 1].y) + func[k - 1].x;
				System.out.println(pixelG);
				for (k = 0; k < func.length; k++) {
					if (func[k].x > pixelR) {
						break;
					}
				}
				if (k == func.length)
					k--;
				pixelR = ((func[k].x - func[k - 1].x) / (func[k].y - func[k - 1].y))
						* (pixelR - func[k - 1].y) + func[k - 1].x;
				
				
				pixelG = pixelG < 255 ? pixelG : 255;
				pixelB = pixelB < 255 ? pixelB : 255;
				pixelR = pixelR < 255 ? pixelR : 255;
				pixelG = pixelG > 0 ? pixelG : 0;
				pixelB = pixelB > 0 ? pixelB : 0;
				pixelR = pixelR > 0 ? pixelR : 0;
				
				c = new Color(pixelR, pixelG, pixelB);
				PicArr[i][j] = c.getRGB();

			}
		}
		return PicArr;
	}

	public int[][] sharpenFilter() throws IOException {

		BufferedImage image = new BufferedImage(scaledImage.getWidth(null),
				scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D bGr = image.createGraphics();
		bGr.drawImage(scaledImage, 0, 0, null);
		bGr.dispose();
		int PicArr[][] = Utils.convertTo2DUsingGetRGB(image);
		int kernel[][] = { { 0, -1, 0 }, { -1, 5, -1 }, { 0, -1, 0 } };
		int fpic[][] = new int[PicArr.length][PicArr[0].length];
		int spixelG = 0;
		int spixelB = 0;
		int spixelR = 0;
		int pixelG = 0;
		int pixelB = 0;
		int pixelR = 0;
		int  row,col;
		Color c;
		for (int i = 0; i < PicArr.length ; i++) {
			for (int j = 0; j < PicArr[0].length ; j++) {

				spixelG = 0;
				spixelB = 0;
				spixelR = 0;
				
				for (int k = -1; k < kernel.length - 1; k++) {
					for (int t = -1; t < kernel[0].length - 1; t++) {
						col = j+k;
						row = i+k;
						if(row<0) 
							row = PicArr.length-1;
						else if(row>PicArr.length-1)
							row=0;
						if (col<0)
							col =PicArr[0].length-1;
						else if (col >PicArr[0].length-1)
							col = 0;
						c = new Color(PicArr[row][col]);
						pixelG = c.getGreen();
						pixelB = c.getBlue();
						pixelR = c.getRed();
						spixelG += pixelG * kernel[k + 1][t + 1];
						spixelB += pixelB * kernel[k + 1][t + 1];
						spixelR += pixelR * kernel[k + 1][t + 1];
					}
				}
				spixelR = spixelR > 255 ? 255 : spixelR;
				spixelB = spixelB > 255 ? 255 : spixelB;
				spixelG = spixelG > 255 ? 255 : spixelG;
				spixelR = spixelR < 0 ? 0 : spixelR;
				spixelB = spixelB < 0 ? 0 : spixelB;
				spixelG = spixelG < 0 ? 0 : spixelG;
				c = new Color(spixelR, spixelG, spixelB);
				fpic[i][j] = c.getRGB();
			}

		}

		return fpic;

	}
	public int[][] guassionFilter() throws IOException { 
		BufferedImage image = new BufferedImage(scaledImage.getWidth(null),
				scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D bGr = image.createGraphics();
		bGr.drawImage(scaledImage, 0, 0, null);
		bGr.dispose();
		int PicArr[][] = Utils.convertTo2DUsingGetRGB(image);
		int kernel[][] = { { 0, 1, 0 }, { 1, 4, 1 }, { 0, 1, 0 } };
		int fpic[][] = new int[PicArr.length][PicArr[0].length];
		int spixelG = 0;
		int spixelB = 0;
		int spixelR = 0;
		int pixelG = 0;
		int pixelB = 0;
		int pixelR = 0;
		int  row,col;
		Color c;
		for (int i = 0; i < PicArr.length ; i++) {
			for (int j = 0; j < PicArr[0].length ; j++) {

				spixelG = 0;
				spixelB = 0;
				spixelR = 0;
				
				for (int k = -1; k < kernel.length - 1; k++) {
					for (int t = -1; t < kernel[0].length - 1; t++) {
						col = j+k;
						row = i+k;
						if(row<0) 
							row = PicArr.length-1;
						else if(row>PicArr.length-1)
							row=0;
						if (col<0)
							col =PicArr[0].length-1;
						else if (col >PicArr[0].length-1)
							col = 0;
						c = new Color(PicArr[row][col]);
						pixelG = c.getGreen();
						pixelB = c.getBlue();
						pixelR = c.getRed();
						spixelG += pixelG * kernel[k + 1][t + 1];
						spixelB += pixelB * kernel[k + 1][t + 1];
						spixelR += pixelR * kernel[k + 1][t + 1];
					}
				}
				spixelR /=8;
				spixelB /=8;
				spixelG /=8;
				
				spixelR = spixelR > 255 ? 255 : spixelR;
				spixelB = spixelB > 255 ? 255 : spixelB;
				spixelG = spixelG > 255 ? 255 : spixelG;
				spixelR = spixelR < 0 ? 0 : spixelR;
				spixelB = spixelB < 0 ? 0 : spixelB;
				spixelG = spixelG < 0 ? 0 : spixelG;
				c = new Color(spixelR, spixelG, spixelB);
				fpic[i][j] = c.getRGB();
			}

		}

		return fpic;


	}
	public int[][] edgeHFilter() throws IOException { 
		BufferedImage image = new BufferedImage(scaledImage.getWidth(null),
				scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D bGr = image.createGraphics();
		bGr.drawImage(scaledImage, 0, 0, null);
		bGr.dispose();
		int PicArr[][] = Utils.convertTo2DUsingGetRGB(image);
		
		int kernel[][] = { { 0, -1, 0 }, { 0, 1, 0 }, {0, 0, 0 } };
		
		int fpic[][] = new int[PicArr.length][PicArr[0].length];
		int spixelG = 0;
		int spixelB = 0;
		int spixelR = 0;
		int pixelG = 0;
		int pixelB = 0;
		int pixelR = 0;
		int  row,col;
		Color c;
		for (int i = 0; i < PicArr.length ; i++) {
			for (int j = 0; j < PicArr[0].length ; j++) {

				spixelG = 0;
				spixelB = 0;
				spixelR = 0;
				
				for (int k = -1; k < kernel.length - 1; k++) {
					for (int t = -1; t < kernel[0].length - 1; t++) {
						col = j+k;
						row = i+k;
						if(row<0) 
							row = PicArr.length-1;
						else if(row>PicArr.length-1)
							row=0;
						if (col<0)
							col =PicArr[0].length-1;
						else if (col >PicArr[0].length-1)
							col = 0;
						c = new Color(PicArr[row][col]);
						pixelG = c.getGreen();
						pixelB = c.getBlue();
						pixelR = c.getRed();
						spixelG += pixelG * kernel[k + 1][t + 1];
						spixelB += pixelB * kernel[k + 1][t + 1];
						spixelR += pixelR * kernel[k + 1][t + 1];
					}
				}
				spixelR += 127;
				spixelB += 127;
				spixelG += 127;
				
				
				spixelR = spixelR > 255 ? 255 : spixelR;
				spixelB = spixelB > 255 ? 255 : spixelB;
				spixelG = spixelG > 255 ? 255 : spixelG;
				spixelR = spixelR < 0 ? 0 : spixelR;
				spixelB = spixelB < 0 ? 0 : spixelB;
				spixelG = spixelG < 0 ? 0 : spixelG;
				c = new Color(spixelR, spixelG, spixelB);
				fpic[i][j] = c.getRGB();
			}

		}

		return fpic;


	}

	public int[][] edgeVFilter() throws IOException { 
		BufferedImage image = new BufferedImage(scaledImage.getWidth(null),
				scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D bGr = image.createGraphics();
		bGr.drawImage(scaledImage, 0, 0, null);
		bGr.dispose();
		int PicArr[][] = Utils.convertTo2DUsingGetRGB(image);
		
		int kernel[][] = { { 0, 0, 0 }, { -1, 1, 0 }, {0, 0, 0 } };
		
		int fpic[][] = new int[PicArr.length][PicArr[0].length];
		int spixelG = 0;
		int spixelB = 0;
		int spixelR = 0;
		int pixelG = 0;
		int pixelB = 0;
		int pixelR = 0;
		int  row,col;
		Color c;
		for (int i = 0; i < PicArr.length ; i++) {
			for (int j = 0; j < PicArr[0].length ; j++) {

				spixelG = 0;
				spixelB = 0;
				spixelR = 0;
				
				for (int k = -1; k < kernel.length - 1; k++) {
					for (int t = -1; t < kernel[0].length - 1; t++) {
						col = j+k;
						row = i+k;
						if(row<0) 
							row = PicArr.length-1;
						else if(row>PicArr.length-1)
							row=0;
						if (col<0)
							col =PicArr[0].length-1;
						else if (col >PicArr[0].length-1)
							col = 0;
						c = new Color(PicArr[row][col]);
						pixelG = c.getGreen();
						pixelB = c.getBlue();
						pixelR = c.getRed();
						spixelG += pixelG * kernel[k + 1][t + 1];
						spixelB += pixelB * kernel[k + 1][t + 1];
						spixelR += pixelR * kernel[k + 1][t + 1];
					}
				}
				spixelR += 127;
				spixelB += 127;
				spixelG += 127;
				
				spixelR = spixelR > 255 ? 255 : spixelR;
				spixelB = spixelB > 255 ? 255 : spixelB;
				spixelG = spixelG > 255 ? 255 : spixelG;
				spixelR = spixelR < 0 ? 0 : spixelR;
				spixelB = spixelB < 0 ? 0 : spixelB;
				spixelG = spixelG < 0 ? 0 : spixelG;
				c = new Color(spixelR, spixelG, spixelB);
				fpic[i][j] = c.getRGB();
			}

		}

		return fpic;


	}

	
	public int[][] edgeAFilter() throws IOException { 
		BufferedImage image = new BufferedImage(scaledImage.getWidth(null),
				scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D bGr = image.createGraphics();
		bGr.drawImage(scaledImage, 0, 0, null);
		bGr.dispose();
		int PicArr[][] = Utils.convertTo2DUsingGetRGB(image);
		
		int kernel[][] = { { -1, 0, 0 }, { 0, 1, 0 }, {0, 0, 0 } };
		
		int fpic[][] = new int[PicArr.length][PicArr[0].length];
		int spixelG = 0;
		int spixelB = 0;
		int spixelR = 0;
		int pixelG = 0;
		int pixelB = 0;
		int pixelR = 0;
		int  row,col;
		Color c;
		for (int i = 0; i < PicArr.length ; i++) {
			for (int j = 0; j < PicArr[0].length ; j++) {

				spixelG = 0;
				spixelB = 0;
				spixelR = 0;
				
				for (int k = -1; k < kernel.length - 1; k++) {
					for (int t = -1; t < kernel[0].length - 1; t++) {
						col = j+k;
						row = i+k;
						if(row<0) 
							row = PicArr.length-1;
						else if(row>PicArr.length-1)
							row=0;
						if (col<0)
							col =PicArr[0].length-1;
						else if (col >PicArr[0].length-1)
							col = 0;
						c = new Color(PicArr[row][col]);
						pixelG = c.getGreen();
						pixelB = c.getBlue();
						pixelR = c.getRed();
						spixelG += pixelG * kernel[k + 1][t + 1];
						spixelB += pixelB * kernel[k + 1][t + 1];
						spixelR += pixelR * kernel[k + 1][t + 1];
					}
				}
				spixelR += 127;
				spixelB += 127;
				spixelG += 127;
				
				spixelR = spixelR > 255 ? 255 : spixelR;
				spixelB = spixelB > 255 ? 255 : spixelB;
				spixelG = spixelG > 255 ? 255 : spixelG;
				spixelR = spixelR < 0 ? 0 : spixelR;
				spixelB = spixelB < 0 ? 0 : spixelB;
				spixelG = spixelG < 0 ? 0 : spixelG;
				c = new Color(spixelR, spixelG, spixelB);
				fpic[i][j] = c.getRGB();
			}

		}

		return fpic;


	}

	
	public int[][] edgeLFilter() throws IOException { 
		BufferedImage image = new BufferedImage(scaledImage.getWidth(null),
				scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D bGr = image.createGraphics();
		bGr.drawImage(scaledImage, 0, 0, null);
		bGr.dispose();
		int PicArr[][] = Utils.convertTo2DUsingGetRGB(image);
		
		int kernel[][] = { { 0, -1, 0 }, { -1, 4, -1 }, {0, -1, 0 } };
		
		int fpic[][] = new int[PicArr.length][PicArr[0].length];
		int spixelG = 0;
		int spixelB = 0;
		int spixelR = 0;
		int pixelG = 0;
		int pixelB = 0;
		int pixelR = 0;
		int  row,col;
		Color c;
		for (int i = 0; i < PicArr.length ; i++) {
			for (int j = 0; j < PicArr[0].length ; j++) {

				spixelG = 0;
				spixelB = 0;
				spixelR = 0;
				
				for (int k = -1; k < kernel.length - 1; k++) {
					for (int t = -1; t < kernel[0].length - 1; t++) {
						col = j+k;
						row = i+k;
						if(row<0) 
							row = PicArr.length-1;
						else if(row>PicArr.length-1)
							row=0;
						if (col<0)
							col =PicArr[0].length-1;
						else if (col >PicArr[0].length-1)
							col = 0;
						c = new Color(PicArr[row][col]);
						pixelG = c.getGreen();
						pixelB = c.getBlue();
						pixelR = c.getRed();
						spixelG += pixelG * kernel[k + 1][t + 1];
						spixelB += pixelB * kernel[k + 1][t + 1];
						spixelR += pixelR * kernel[k + 1][t + 1];
					}
				}
				
				spixelR += 127;
				spixelB += 127;
				spixelG += 127;
				
				spixelR = spixelR > 255 ? 255 : spixelR;
				spixelB = spixelB > 255 ? 255 : spixelB;
				spixelG = spixelG > 255 ? 255 : spixelG;
				spixelR = spixelR < 0 ? 0 : spixelR;
				spixelB = spixelB < 0 ? 0 : spixelB;
				spixelG = spixelG < 0 ? 0 : spixelG;
				c = new Color(spixelR, spixelG, spixelB);
				fpic[i][j] = c.getRGB();
			}

		}

		return fpic;


	}

	public int[][] embossSFilter() throws IOException { 
		BufferedImage image = new BufferedImage(scaledImage.getWidth(null),
				scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D bGr = image.createGraphics();
		bGr.drawImage(scaledImage, 0, 0, null);
		bGr.dispose();
		int PicArr[][] = Utils.convertTo2DUsingGetRGB(image);
		
		int kernel[][] = { { -1, -1, -1 }, { 0, 1, 0 }, {1, 1, 1 } };
		
		int fpic[][] = new int[PicArr.length][PicArr[0].length];
		int spixelG = 0;
		int spixelB = 0;
		int spixelR = 0;
		int pixelG = 0;
		int pixelB = 0;
		int pixelR = 0;
		int  row,col;
		Color c;
		for (int i = 0; i < PicArr.length ; i++) {
			for (int j = 0; j < PicArr[0].length ; j++) {

				spixelG = 0;
				spixelB = 0;
				spixelR = 0;
				
				for (int k = -1; k < kernel.length - 1; k++) {
					for (int t = -1; t < kernel[0].length - 1; t++) {
						col = j+k;
						row = i+k;
						if(row<0) 
							row = PicArr.length-1;
						else if(row>PicArr.length-1)
							row=0;
						if (col<0)
							col =PicArr[0].length-1;
						else if (col >PicArr[0].length-1)
							col = 0;
						c = new Color(PicArr[row][col]);
						pixelG = c.getGreen();
						pixelB = c.getBlue();
						pixelR = c.getRed();
						spixelG += pixelG * kernel[k + 1][t + 1];
						spixelB += pixelB * kernel[k + 1][t + 1];
						spixelR += pixelR * kernel[k + 1][t + 1];
					}
				}
				
				
				
				spixelR = spixelR > 255 ? 255 : spixelR;
				spixelB = spixelB > 255 ? 255 : spixelB;
				spixelG = spixelG > 255 ? 255 : spixelG;
				spixelR = spixelR < 0 ? 0 : spixelR;
				spixelB = spixelB < 0 ? 0 : spixelB;
				spixelG = spixelG < 0 ? 0 : spixelG;
				c = new Color(spixelR, spixelG, spixelB);
				fpic[i][j] = c.getRGB();
			}

		}

		return fpic;


	}
	public int[][] embossEFilter() throws IOException { 
		BufferedImage image = new BufferedImage(scaledImage.getWidth(null),
				scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D bGr = image.createGraphics();
		bGr.drawImage(scaledImage, 0, 0, null);
		bGr.dispose();
		int PicArr[][] = Utils.convertTo2DUsingGetRGB(image);
		
		int kernel[][] = { { -1, 0, 1 }, { -1, 1, 1 }, {-1, 0, 1 } };
		
		int fpic[][] = new int[PicArr.length][PicArr[0].length];
		int spixelG = 0;
		int spixelB = 0;
		int spixelR = 0;
		int pixelG = 0;
		int pixelB = 0;
		int pixelR = 0;
		int  row,col;
		Color c;
		for (int i = 0; i < PicArr.length ; i++) {
			for (int j = 0; j < PicArr[0].length ; j++) {

				spixelG = 0;
				spixelB = 0;
				spixelR = 0;
				
				for (int k = -1; k < kernel.length - 1; k++) {
					for (int t = -1; t < kernel[0].length - 1; t++) {
						col = j+k;
						row = i+k;
						if(row<0) 
							row = PicArr.length-1;
						else if(row>PicArr.length-1)
							row=0;
						if (col<0)
							col =PicArr[0].length-1;
						else if (col >PicArr[0].length-1)
							col = 0;
						c = new Color(PicArr[row][col]);
						pixelG = c.getGreen();
						pixelB = c.getBlue();
						pixelR = c.getRed();
						spixelG += pixelG * kernel[k + 1][t + 1];
						spixelB += pixelB * kernel[k + 1][t + 1];
						spixelR += pixelR * kernel[k + 1][t + 1];
					}
				}
				
				
				
				spixelR = spixelR > 255 ? 255 : spixelR;
				spixelB = spixelB > 255 ? 255 : spixelB;
				spixelG = spixelG > 255 ? 255 : spixelG;
				spixelR = spixelR < 0 ? 0 : spixelR;
				spixelB = spixelB < 0 ? 0 : spixelB;
				spixelG = spixelG < 0 ? 0 : spixelG;
				c = new Color(spixelR, spixelG, spixelB);
				fpic[i][j] = c.getRGB();
			}

		}

		return fpic;


	}

	public int[][] embossSEFilter() throws IOException { 
		BufferedImage image = new BufferedImage(scaledImage.getWidth(null),
				scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D bGr = image.createGraphics();
		bGr.drawImage(scaledImage, 0, 0, null);
		bGr.dispose();
		int PicArr[][] = Utils.convertTo2DUsingGetRGB(image);
		
		int kernel[][] = { { -1, -1, 0 }, { -1, 1, 1 }, {0, 1, 1 } };
		
		int fpic[][] = new int[PicArr.length][PicArr[0].length];
		int spixelG = 0;
		int spixelB = 0;
		int spixelR = 0;
		int pixelG = 0;
		int pixelB = 0;
		int pixelR = 0;
		int  row,col;
		Color c;
		for (int i = 0; i < PicArr.length ; i++) {
			for (int j = 0; j < PicArr[0].length ; j++) {

				spixelG = 0;
				spixelB = 0;
				spixelR = 0;
				
				for (int k = -1; k < kernel.length - 1; k++) {
					for (int t = -1; t < kernel[0].length - 1; t++) {
						col = j+k;
						row = i+k;
						if(row<0) 
							row = PicArr.length-1;
						else if(row>PicArr.length-1)
							row=0;
						if (col<0)
							col =PicArr[0].length-1;
						else if (col >PicArr[0].length-1)
							col = 0;
						c = new Color(PicArr[row][col]);
						pixelG = c.getGreen();
						pixelB = c.getBlue();
						pixelR = c.getRed();
						spixelG += pixelG * kernel[k + 1][t + 1];
						spixelB += pixelB * kernel[k + 1][t + 1];
						spixelR += pixelR * kernel[k + 1][t + 1];
					}
				}
				
				
				
				spixelR = spixelR > 255 ? 255 : spixelR;
				spixelB = spixelB > 255 ? 255 : spixelB;
				spixelG = spixelG > 255 ? 255 : spixelG;
				spixelR = spixelR < 0 ? 0 : spixelR;
				spixelB = spixelB < 0 ? 0 : spixelB;
				spixelG = spixelG < 0 ? 0 : spixelG;
				c = new Color(spixelR, spixelG, spixelB);
				fpic[i][j] = c.getRGB();
			}

		}

		return fpic;


	}
	
	
	/*======================== Gamma currection ================================*/
	public int[][] gammaFilter(float b) throws IOException {

		BufferedImage image = new BufferedImage(scaledImage.getWidth(null),
				scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bGr = image.createGraphics();
		bGr.drawImage(scaledImage, 0, 0, null);
		bGr.dispose();

		int PicArr[][] = Utils.convertTo2DUsingGetRGB(image);

		
		for (int i = 0; i < PicArr.length; i++) {
			for (int j = 0; j < PicArr[0].length; j++) {
				Color c = new Color(PicArr[i][j]);
				int pixelG = c.getGreen();
				int pixelB = c.getBlue();
				int pixelR = c.getRed();
				
				float pixelGf = (float)pixelG / (float)255;
				float pixelBf = (float)pixelB / (float)255;
				float pixelRf = (float)pixelR / (float)255;
					
				
				pixelGf = (float) Math.pow(pixelGf, b);
				pixelBf = (float) Math.pow(pixelBf, b);
				pixelRf = (float) Math.pow(pixelRf, b);
				
				pixelGf *= 255;
				pixelBf *= 255;
				pixelRf *= 255;
				
				pixelG = (int)pixelGf;
				pixelB = (int)pixelBf;
				pixelR = (int)pixelRf;
				
				System.out.println(pixelG+"  "+(int)pixelBf+"  "+(int)pixelRf);
				
				/*
				pixelG = pixelG < 255 ? pixelG : 255;
				pixelB = pixelB < 255 ? pixelB : 255;
				pixelR = pixelR < 255 ? pixelR : 255;
				pixelG = pixelG > 0 ? pixelG : 0;
				pixelB = pixelB > 0 ? pixelB : 0;
				pixelR = pixelR > 0 ? pixelR : 0;
				*/
				
				
				c = new Color(pixelR, pixelG, pixelB);
				PicArr[i][j] = c.getRGB();

			}
		}
		return PicArr;
	}

	
}
