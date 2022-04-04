package hevs.graphics.utils;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * GraphicsBitmap contains the methods required to create a BufferedImage from a
 * String if the file exists
 * 
 * 1.3 : Added acceleration for images using graphics card
 * 
 * @version 1.3, April 2011
 * @author <a href='mailto:pandre.mudry&#64;hevs.ch'> Pierre-Andre Mudry</a>
 */
public class GraphicsBitmap {
	final private int WIDTH, HEIGHT;
	final String name;

	public BufferedImage mBitmap;

	// private Image image;

	public GraphicsBitmap(String imageName) {

		name = imageName;

		// Get optimized image
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

		try {
			URL v = GraphicsBitmap.class.getResource(imageName);
			mBitmap = ImageIO.read(v);

		} catch (Exception e) {
			System.out.println("Could not find image " + imageName + ", exiting !");
			e.printStackTrace();
			System.exit(-1);
		} finally {
			if (mBitmap != null) {
				WIDTH = mBitmap.getWidth();
				HEIGHT = mBitmap.getHeight();
			} else {
				WIDTH = 0;
				HEIGHT = 0;
			}
		}
	}

	/**
	 * @return width of the image
	 */
	public int getWidth() {
		return WIDTH;
	}

	/**
	 * @return height of the image
	 */
	public int getHeight() {
		return HEIGHT;
	}

	/**
	 * @return the {@link BufferedImage} corresponding to the
	 *         {@link GraphicsBitmap}
	 */
	public BufferedImage getBufferedImage() {
		return mBitmap;
	}
}
