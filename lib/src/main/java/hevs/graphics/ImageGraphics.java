package hevs.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * This class was made to deal with images as multidimensional arrays.
 * Mainly used in the <code>ImageProcessing</code> lab. It expects the images to reside in the <code>src</code> directory
 * 
 * @author Pierre-André Mudry
 * @version 1.0
 */
public class ImageGraphics extends JFrame {
	private static final long serialVersionUID = 6832022057915586803L;

	private BufferedImage backgroundBitmap = null;
	private int w, h;	
	
	public ImageGraphics(String backGroundFilePath, String title, int xPositionOffset, int yPositionOffset) {

		try {
			// Fill the frame content with the image
			try {
				backgroundBitmap = ImageIO.read(ImageGraphics.class.getResource(backGroundFilePath));
				w = backgroundBitmap.getWidth();
				h = backgroundBitmap.getHeight();
			} catch (Exception e) {
				System.out.println("Could not find image " + backGroundFilePath + ", exiting !");
				e.printStackTrace();
				System.exit(-1);
			}

			this.setResizable(false);
			this.setSize(backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
			this.setTitle(title);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			// Get the size of the screen
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

			// Determine the new location of the window
			int w = this.getSize().width;
			int h = this.getSize().height;
			int x = (dim.width - w) / 2 + xPositionOffset;
			int y = (dim.height - h) / 2 + yPositionOffset;

			// Move the window
			this.setLocation(x, y);
			this.setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets a grayscale pixel, does not sets values for invalid pixels
	 * outside the screen. Does not repaint the screen either because it
	 * is slow. If required, please call {@link #repaint()} if needed after
	 * you have updated all the pixels you need.
	 * 
	 * @param x
	 * @param y
	 * @param intensity
	 */
	public void setPixelBW(int x, int y, int intensity) {
		if (!((x < 0) || (y < 0) || (x >= w) || (y >= h))) {
			backgroundBitmap.setRGB(x, y, intensity << 16 | intensity << 8 | intensity);
		}
	}

	/**
	 * Sets an array of grayscale pixels (from 0 to 255) and displays them
	 * 
	 * @param pixels
	 */
	public void setPixelsBW(int[][] pixels) {
		try {
			if (pixels[0].length != h || pixels.length != w) {
				throw new Exception("Invalid size of the pixel array !");
			}

			for (int i = 0; i < w; i++)
				for (int j = 0; j < h; j++) {
					// FIXME this is slow, should use rasters instead
					int c = pixels[i][j] << 16 | pixels[i][j] << 8 | pixels[i][j];
					backgroundBitmap.setRGB(i, j, c);
				}

			this.repaint();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets an array of pixels of Color and displays them
	 * 
	 * @param pixels
	 */
	public void setPixelsColor(Color[][] pixels) {
		try {
			if (pixels[0].length != h || pixels.length != w) {
				throw new Exception("Invalid size of the pixel array !");
			}

			// FIXME this is slow, should use rasters instead
			for (int i = 0; i < w; i++)
				for (int j = 0; j < h; j++) {
					backgroundBitmap.setRGB(i, j, pixels[i][j].getRGB());
				}

			this.repaint();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets a single pixel from the background image and returns its
	 * grayscale value
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int getPixelBW(int x, int y) {
		if ((x < 0) || (y < 0) || (x >= w) || (y >= h)) {
			return 0;
		} else {
			// Inside the image. Make the gray conversion and return the value
			Color c = new Color(backgroundBitmap.getRGB(x, y));
			return (int) (0.3 * c.getRed() + 0.59 * c.getGreen() + 0.11 * c.getBlue());
		}
	}

	/**
	 * Gets the array of the pixels (which have been converted to grayscale
	 * if required)
	 * 
	 * @return The arrays of gray pixels
	 */
	public int[][] getPixelsBW() {
		int[][] values = new int[w][h];

		// FIXME this is slow
		for (int i = 0; i < w; i++)
			for (int j = 0; j < h; j++) {
				Color c = new Color(backgroundBitmap.getRGB(i, j));
				values[i][j] = (int) (0.3 * c.getRed() + 0.59 * c.getGreen() + 0.11 * c.getBlue());
			}

		return values;
	}

	/**
	 * Gets the array of the pixels as Colors (see #Color)
	 * 
	 * @return The arrays of pixels
	 */
	public Color[][] getPixelsColor() {
		Color[][] values = new Color[w][h];

		// FIXME this is slow
		for (int i = 0; i < w; i++)
			for (int j = 0; j < h; j++) {
				values[i][j] = new Color(backgroundBitmap.getRGB(i, j));
			}

		return values;
	}

	/**
	 * Converts a color array to a black-or-white array
	 * 
	 * @param c
	 *            The color array
	 * @return The array converted to BW
	 */
	public static Color[][] convertToGray(Color[][] c) {
		int w = c.length;
		int h = c[0].length;
		Color[][] values = new Color[w][h];

		// FIXME this is slow
		for (int i = 0; i < w; i++)
			for (int j = 0; j < h; j++) {
				Color col = c[i][j];
				int intColor = (int) (0.3 * col.getRed() + 0.59 * col.getGreen() + 0.11 * col.getBlue());
				values[i][j] = new Color(intColor, intColor, intColor);
			}

		return values;
	}

	/**
	 * Converts a color array to a black-or-white array
	 * 
	 * @param c
	 *            The color array
	 * @return The array converted to BW
	 */
	public static int[][] convertToGrayInt(Color[][] c) {
		int w = c.length;
		int h = c[0].length;
		int[][] values = new int[w][h];

		// FIXME this is slow
		for (int i = 0; i < w; i++)
			for (int j = 0; j < h; j++) {
				Color col = c[i][j];
				int intColor = (int) (0.3 * col.getRed() + 0.59 * col.getGreen() + 0.11 * col.getBlue());
				values[i][j] = intColor;
			}

		return values;
	}

	/**
	 * Paint method
	 */
	public void paint(Graphics g) {
		g.drawImage(backgroundBitmap, 0, 0, null);
		g.dispose();
	}

	public static void main(String args[]) {
		final String imageUsed = "/images/lena.bmp";
		ImageGraphics org = new ImageGraphics(imageUsed, "Original", 0, 0);
	}
}
