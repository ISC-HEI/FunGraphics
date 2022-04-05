package hevs.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.Manifest;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import hevs.graphics.interfaces.DualLayerGraphics;
import hevs.graphics.interfaces.Graphics;
import hevs.graphics.utils.GraphicsBitmap;
import hevs.graphics.utils.RepeatingReleasedEventsFixer;

/**
 * A graphics framework for games and experiments. Developed for the INF1 course
 * given at HES-SO Valais.
 * 
 * @author Pierre-André Mudry <a href='mailto:pandre.mudry&#64;hevs.ch'></a>
 * @version 1.55
 * @date April 2013-2022
 */
public class FunGraphics extends AcceleratedDisplay implements Graphics, DualLayerGraphics {

	public String versionString() {
		try {
			Enumeration<URL> resources = getClass().getClassLoader()
					.getResources("META-INF/MANIFEST.MF");
			if (!resources.hasMoreElements()) {
				System.err.println("No version information, not using the jar?");
				return "NO_VERSION";
			}

			while (resources.hasMoreElements()) {
				Manifest manifest = new Manifest(resources.nextElement().openStream());
				return manifest.getMainAttributes().getValue("Implementation-Version").toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "NO_VERSION";
	}

	/**
	 * Creates a graphic window to draw onto.
	 * 
	 * @param width        Width of the window
	 * @param height       Height of the window
	 * @param xoffset      X-Position of the window on the screen
	 * @param yoffset      Y-Position of the window on the screen
	 * @param title        Title of the window
	 * @param high_quality Use high quality rendering
	 */
	public FunGraphics(int width, int height, int xoffset, int yoffset, String title, boolean high_quality) {
		super(width, height, xoffset, yoffset, title, high_quality);
		System.out.println("Fungraphics - HES-SO Valais (mui), v" + versionString());

		// Emulates SimpleGraphics default behavior
		this.clear(Color.white);
		g2d.setBackground(Color.white);
		this.setColor(Color.black);

		new RepeatingReleasedEventsFixer().install();
	}

	/**
	 * Creates a graphic window to draw onto.
	 * 
	 * @param width        Width of the display window
	 * @param height       Height of the display window
	 * @param title        Title of the display window
	 * @param high_quality Use high quality rendering (slower)
	 */
	public FunGraphics(int width, int height, String title, boolean high_quality) {
		this(width, height, -1, -1, title, high_quality);
	}

	/**
	 * Creates a graphic window to draw onto with a given title
	 * 
	 * @see #FunGraphics(int, int, String, boolean)
	 */
	public FunGraphics(int width, int height, String title) {
		this(width, height, title, true);
	}

	/**
	 * Creates a graphic window to draw onto
	 * 
	 * @see #FunGraphics(int, int, String, boolean)
	 */
	public FunGraphics(int width, int height) {
		this(width, height, "FunGraphics ");
	}

	/**
	 * Sets a keyboard listener
	 * 
	 * @param k The KeyListener to listen to
	 */
	public void setKeyManager(KeyListener k) {
		mainFrame.addKeyListener(k);
	}

	/**
	 * Adds a {@link MouseListener} to the window to react on mouse events
	 * 
	 * @param l The {@link MouseListener}
	 */
	public void addMouseListener(MouseListener l) {
		mainFrame.getContentPane().addMouseListener(l);
	}

	/**
	 * Adds a {@link MouseMotionListener} to the window to react on mouse movements
	 * 
	 * @param m The {@link MouseMotionListener}
	 */
	public void addMouseMotionListener(MouseMotionListener m) {
		mainFrame.getContentPane().addMouseMotionListener(m);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#clear()
	 */
	@Override
	public void clear() {
		g2d.clearRect(0, 0, fWidth, fHeight);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#clear(java.awt.Color)
	 */
	@Override
	public void clear(Color c) {
		Color old = g2d.getBackground();
		g2d.setBackground(c);
		g2d.clearRect(0, 0, fWidth, fHeight);
		g2d.setBackground(old);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#setColor(java.awt.Color)
	 */
	@Override
	public void setColor(Color c) {
		g2d.setColor(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#setPixel(int, int)
	 */
	@Override
	public void setPixel(int x, int y) {
		// Test that the pixel to set is in the frame
		if ((x < 0) || (y < 0) || (x >= getFrameWidth()) || (y >= getFrameHeight())) {
			if (checkBorders) {
				System.out.println("[FunGraphics] Coordinates out of frame");
			}
		} else {
			frontBuffer.setRGB(x, y, g2d.getColor().getRGB());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#setPixel(int, int, java.awt.Color)
	 */
	@Override
	public void setPixel(int x, int y, Color c) {
		Color oldColor = g2d.getColor();
		setColor(c);
		setPixel(x, y);
		setColor(oldColor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#setPixel(int, int, int)
	 */
	@Override
	public void setPixel(int x, int y, int c) {
		setPixel(x, y, new Color(c));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#setPenWidth(float)
	 */
	@Override
	public void setPenWidth(float width) {
		g2d.setStroke(new BasicStroke(width));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#drawLine(int, int, int, int)
	 */
	@Override
	public void drawLine(int p1x, int p1y, int p2x, int p2y) {
		g2d.drawLine(p1x, p1y, p2x, p2y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#drawFilledPolygon(java.awt.Polygon,
	 * java.awt.Color)
	 */
	@Override
	public void drawFilledPolygon(Polygon p, Color c) {
		Color oldColor = g2d.getColor();
		setColor(c);
		g2d.fill(p);
		g2d.drawPolygon(p);
		setColor(oldColor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#drawRect(int, int, int, int)
	 */
	@Override
	public void drawRect(int posX, int posY, int width, int height) {
		g2d.drawRect(posX, posY, width, height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#drawFillRect(int, int, int, int)
	 */
	@Override
	public void drawFillRect(int posX, int posY, int width, int height) {
		g2d.fillRect(posX, posY, width, height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#drawRect(java.awt.Rectangle)
	 */
	@Override
	public void drawRect(Rectangle rect) {
		g2d.drawRect((int) rect.getX(), (int) rect.y, (int) rect.getWidth(), (int) rect.getHeight());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#drawFillRect(java.awt.Rectangle)
	 */
	@Override
	public void drawFillRect(Rectangle rect) {
		g2d.drawRect((int) rect.getX(), (int) rect.y, (int) rect.getWidth(), (int) rect.getHeight());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#drawCircle(int, int, int)
	 */
	@Override
	public void drawCircle(int posX, int posY, int f) {
		g2d.drawOval(posX, posY, f, f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#drawFilledCircle(int, int, int)
	 */
	@Override
	public void drawFilledCircle(int posX, int posY, int radius) {
		g2d.fillOval(posX, posY, radius, radius);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#drawFilledOval(int, int, int, int)
	 */
	@Override
	public void drawFilledOval(int posX, int posY, int width, int height) {
		g2d.fillOval(posX, posY, width, height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#drawString(int, int, java.lang.String)
	 */
	@Override
	public void drawString(int posX, int posY, String str) {
		g2d.drawString(str, posX, posY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#drawString(int, int, java.lang.String,
	 * java.awt.Color, int)
	 */
	@Override
	public void drawString(int posX, int posY, String str, Color color, int size) {
		Font oldFont = g2d.getFont();
		Color oldColor = g2d.getColor();

		Font font = new Font("SansSerif", Font.PLAIN, size);
		g2d.setFont(font);
		g2d.setColor(color);
		g2d.drawString(str, posX, posY);
		g2d.setFont(oldFont);
		g2d.setColor(oldColor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#drawFancyString(int, int, java.lang.String,
	 * java.awt.Color, int)
	 */
	@Override
	public void drawFancyString(int posX, int posY, String str, Color color, int size) {
		Graphics2D g2 = g2d;

		Font oldFont = g2d.getFont();
		Color oldColor = g2d.getColor();

		Font font = new Font("Georgia", Font.BOLD, size);
		TextLayout textLayout = new TextLayout(str, font, g2.getFontRenderContext());
		g2.setColor(Color.GRAY);
		textLayout.draw(g2, posX + 2, posY + 2);

		g2.setColor(color);
		textLayout.draw(g2, posX, posY);

		g2.setFont(oldFont);
		g2.setColor(oldColor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#drawPicture(int, int,
	 * hevs.graphics.utils.GraphicsBitmap)
	 */
	@Override
	public void drawPicture(int posX, int posY, GraphicsBitmap bitmap) {
		g2d.drawImage(bitmap.mBitmap, posX - bitmap.getWidth() / 2, posY - bitmap.getHeight() / 2, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#drawTransformedPicture(int, int, double, double,
	 * java.lang.String)
	 */
	@Override
	public void drawTransformedPicture(int posX, int posY, double angle, double scale, String imageName) {
		drawTransformedPicture(posX, posY, angle, scale, new GraphicsBitmap(imageName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#drawTransformedPicture(int, int, double, double,
	 * hevs.graphics.utils.GraphicsBitmap)
	 */
	@Override
	public void drawTransformedPicture(int posX, int posY, double angle, double scale, GraphicsBitmap bitmap) {
		AffineTransform t = new AffineTransform();

		t.rotate(angle, posX, posY);
		t.translate(posX - bitmap.getWidth() / 2 * scale, posY - bitmap.getHeight() / 2 * scale);
		t.scale(scale, scale);
		g2d.drawImage(bitmap.mBitmap, t, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#drawMirroredPicture(int, int, double,
	 * hevs.graphics.utils.GraphicsBitmap)
	 */
	@Override
	public void drawMirroredPicture(int posX, int posY, double angle, GraphicsBitmap bitmap) {
		AffineTransform t = new AffineTransform();

		t.rotate(angle, posX, posY);
		t.translate(posX + bitmap.getWidth() / 2, posY - bitmap.getHeight() / 2);
		t.scale(-1, 1);
		g2d.drawImage(bitmap.mBitmap, t, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.DualLayerGraphics#drawBackground()
	 */
	@Override
	public void drawBackground() {
		g2d.setBackground(TRANSPARENT);
		g2d = backg2d;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.DualLayerGraphics#drawForeground()
	 */
	@Override
	public void drawForeground() {
		g2d = frontg2d;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#getFrameWidth()
	 */
	@Override
	public int getFrameWidth() {
		return fWidth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hevs.graphics.Graphics#getFrameHeight()
	 */
	@Override
	public int getFrameHeight() {
		return fHeight;
	}

	/**
	 * A sample game loop using explicit synchronization (if display flickers)
	 */
	boolean pressedUp = false;
	boolean pressedDown = false;
	int size = 1;
	
	void gameloopSample() {
		int i = 1;
		int direction = 1;

		// Do something when a key has been pressed
		this.setKeyManager(new KeyAdapter() {

			// Will be called when a key has been pressed
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_S) {
					System.out.println("Saving file...");
					saveAsPNG("fungraphics_screenshot");
				}
				if(e.getKeyCode() == KeyEvent.VK_LEFT) {
					pressedUp = true;
				}
				if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
					pressedDown = true;
				}
			}
			
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_LEFT) {
					pressedUp = false;
				}
				if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
					pressedDown = false;
				}
			}
		});

		while (true) {
			if(pressedUp)
				size++;
			if(pressedDown) {
				size = size == 0 ? 0: size-1;
			}
	
			synchronized (frontBuffer) {
				clear(Color.white);
				setColor(Color.red);
				drawFilledOval(10, 10, 100+size, 100+size);
				drawString(100, 250, "Hello world");
				setColor(Color.yellow);
				drawFillRect(50 + i, 50 - i, 100 + i, 100 + i);
			}

			i += direction;

			if (i > 100 || i <= 0) {
				direction *= -1;
			}
			syncGameLogic(60);
		}
	}

	/**
	 * Creates a screenshot of the current window on the disk.
	 * 
	 * @param fileName The name of the file
	 */
	void saveAsPNG(String fileName) {
		try {
			Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName("PNG");
			ImageWriter imageWriter = (ImageWriter) imageWriters.next();
			File file = new File(fileName + ".png");

			ImageOutputStream ios = ImageIO.createImageOutputStream(file);
			imageWriter.setOutput(ios);
			imageWriter.write(this.frontBuffer);
			ios.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		FunGraphics fg = new FunGraphics(320, 320, "Testing performance of FunGraphics");

		try {
		} catch (Exception e) {

		}
		fg.setPixel(10, 10);
		fg.gameloopSample();
	}
}