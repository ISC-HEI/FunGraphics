package hevs.graphics;

import hevs.graphics.utils.GraphicTimer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

/**
 * Base class for every display window. Developed for the INF1 course given at
 * HES-SO Valais by Pierre-Andre Mudry, see for instance {@link FunGraphics}
 * 
 * @author Pierre-André Mudry <a href='mailto:pandre.mudry&#64;hevs.ch'></a>
 * @version 1.35
 * @date October 2012
 * @date January 2015
 * @date October 2018
 * @date October 2020
 */
public abstract class AcceleratedDisplay {
	/**
	 * The subclass which create the windows frame
	 */
	protected JFrame mainFrame;
	protected int fWidth;
	protected int fHeight;
	protected static final boolean VERBOSE = true;
	protected boolean DISPLAY_FPS = false;

	/**
	 * Buffer and g2d stuff
	 */
	protected BufferStrategy bufferStrategy = null;
	protected static final int numBuffers = 2;
	protected boolean enableRenderingHints = false;
	protected boolean checkBorders = true;
	protected Graphics2D g2d = null;
	protected Graphics2D frontg2d = null;
	protected Graphics2D backg2d = null;
	public BufferedImage frontBuffer = null;
	protected BufferedImage backBuffer = null;
	protected Color TRANSPARENT = new Color(0, 0, 0, 0);

	// Frame updates per second with rendering thread
	protected int target_fps;
	protected float current_fps;

	/**
	 * @param x Selects if the FPS should be printed or not
	 */
	public void displayFPS(boolean x) {
		DISPLAY_FPS = x;
	}

	/**
	 * @see #AcceleratedDisplay(int, int, int, int, String, boolean)
	 * @param width
	 * @param height
	 * @param title
	 * @param high_quality
	 */
	public AcceleratedDisplay(int width, int height, String title, boolean high_quality) {
		enableRenderingHints = high_quality;
		initFrame(title, width, height, -1, -1);
	}

	/**
	 * 
	 * @param width
	 * @param height
	 * @param xPos         x offset position of the window on the screen, -1 for
	 *                     centered
	 * @param yPos         y offset position of the window on the screen, -1 for
	 *                     centered
	 * @param title
	 * @param high_quality
	 */
	public AcceleratedDisplay(int width, int height, int xPos, int yPos, String title, boolean high_quality) {
		enableRenderingHints = high_quality;
		initFrame(title, width, height, xPos, yPos);
	}

	/**
	 * @param title
	 * @param width
	 * @param height
	 * @param xOffset The x offset of the window on the screen, -1 if centered
	 * @param yOffset The y offset of the window on the screen, -1 if centered
	 */
	private void initFrame(String title, int width, int height, int xOffset, int yOffset) {
		// Shall we try a different look for the window ?
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e1) {
		}

		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = env.getDefaultScreenDevice();
		GraphicsConfiguration gc = device.getDefaultConfiguration();

		/**
		 * Get refresh rate and align FPS to it
		 */
		DisplayMode dm = device.getDisplayMode();
		int refreshRate = dm.getRefreshRate();

		if (refreshRate == DisplayMode.REFRESH_RATE_UNKNOWN) {
			System.out.println("[AccDisplay] Could not detect frame-rate, using 50 FPS");
			this.target_fps = 50;
		} else
			target_fps = refreshRate;

		// Get optimized image
		frontBuffer = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		backBuffer = gc.createCompatibleImage(width, height, Transparency.OPAQUE);

		frontg2d = (Graphics2D) frontBuffer.getGraphics();
		backg2d = (Graphics2D) backBuffer.getGraphics();

		// Sets active g2d to front and make the front layer transparent
		frontg2d.setBackground(TRANSPARENT);
		frontg2d.clearRect(0, 0, width, height);
		g2d = frontg2d;

		if (enableRenderingHints) {
			// Enable anti-aliasing for shapes
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// Anti-alias for text
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
					RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		}

		mainFrame = new JFrame(title, gc);
		mainFrame.setResizable(false);
		mainFrame.setIgnoreRepaint(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setPreferredSize(new Dimension(width, height));

		// mainFrame.setUndecorated(!hasDecoration);
		mainFrame.pack();

		// Get the size of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		// Determine the new location of the window

		fWidth = mainFrame.getSize().width;
		fHeight = mainFrame.getSize().height;

		int x = (dim.width - fWidth) / 2;
		int y = (dim.height - fHeight) / 2;

		if (xOffset != -1)
			x = xOffset;
		if (yOffset != -1)
			y = yOffset;

		// Move the window to the center of the screen
		mainFrame.setLocation(x, y);
		mainFrame.setVisible(true);
		mainFrame.createBufferStrategy(numBuffers);

		while (bufferStrategy == null)
			bufferStrategy = mainFrame.getBufferStrategy();

		/**
		 * Rendering thread
		 */
		class RenderThread extends SwingWorker<String, Object> {
			GraphicTimer gt = new GraphicTimer();
			long lastRender = 0;

			@Override
			public String doInBackground() {
				if (VERBOSE)
					System.out.println("[AccDisplay] Rendering thread launched");

				try {
					while (true) {
						internalRender();
						gt.sync(target_fps);

						long now = System.nanoTime();

						/**
						 * FPS calculation
						 */
						if (lastRender != 0 && DISPLAY_FPS) {
							// System.out.println(((now - lastRender) / 1000) + " uSec"); // Frame
							// rate is constant !
							current_fps = 1000000000.0f / (now - lastRender);
							current_fps = Math.round(current_fps);
						}

						// FPS_COUNTER++;
						lastRender = now;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return "Nothing !";
			}

			@Override
			protected void done() {
			}
		}

		// Launch the rendering thread
		new RenderThread().execute();
	}

	/**
	 * This rendering method is called by the rendering thread, always
	 */
	private void internalRender() {
		Graphics2D g = null;

		try {
			g = (Graphics2D) bufferStrategy.getDrawGraphics();

			// Copy the image buffer to the active buffer strategy
			// and make sure that the image is not modified during that time
			g.drawImage(backBuffer, mainFrame.getInsets().left, mainFrame.getInsets().top, null);

			synchronized (frontBuffer) {
				g.drawImage(frontBuffer, mainFrame.getInsets().left, mainFrame.getInsets().top, null);
				g.setColor(Color.black);

				if (DISPLAY_FPS == true)
					g.drawString("FPS - " + current_fps, (int) (backBuffer.getWidth() * 0.05), backBuffer.getHeight());
			}

			// Shows the contents of the back buffer on the screen.
			bufferStrategy.show();

			Toolkit.getDefaultToolkit().sync();

		} catch (Exception e) {
		} finally {
			g.dispose();
		}
	}

	/**
	 * Used for game loop synchronization
	 */
	private GraphicTimer gt = new GraphicTimer();

	/**
	 * Call this method periodically to have a constant frame rate
	 * 
	 * @param FPS
	 */
	public void syncGameLogic(int FPS) {
		gt.sync(FPS);
	}

	/**
	 * Sets graphic acceleration if available
	 */
	static {
//		System.setProperty("sun.java2d.transaccel", "true");
//		System.setProperty("sun.java2d.ddforcevram", "true");
//		System.setProperty("sun.java2d.ddscale", "true");
//		// System.setProperty("sun.java2d.trace", "timestamp,log,count");
//
//		/**
//		 * Try to enable acceleration depending on the OS
//		 */
//		String os = System.getProperty("os.name").toLowerCase();
//		String c = VERBOSE ? "True" : "true";
//
//		if (os.contains("win"))
//			System.setProperty("sun.java2d.d3d", "true");
//		else if (os.contains("nix"))
//			System.setProperty("sun.java2d.opengl", "true");
	}
}
