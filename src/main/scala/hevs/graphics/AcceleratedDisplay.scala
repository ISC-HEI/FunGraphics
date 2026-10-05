package hevs.graphics

import hevs.graphics.utils.{DisplaySetup, GraphicTimer, GraphicsBitmap}

import java.awt._
import java.awt.image.{BufferStrategy, BufferedImage}
import java.io.File
import javax.imageio.ImageIO
import javax.swing.{JFrame, JPanel, SwingUtilities, SwingWorker, UIManager}


object AcceleratedDisplay {
	protected val VERBOSE = true
	private val numBuffers = 2

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
	//		if (os.contains("win"))
	//			System.setProperty("sun.java2d.d3d", "true");
	//		else if (os.contains("nix"))
	//			System.setProperty("sun.java2d.opengl", "true");
}

/**
 * Base class for every display window, see for instance [[FunGraphics]]
 *
 * @author Pierre-André Mudry
 */
abstract class AcceleratedDisplay {

	/**
	 * The subclass which create the windows frame
	 */
	var mainFrame: JFrame = _
	protected var fWidth = 0
	protected var fHeight = 0
	protected var DISPLAY_FPS = false

	/**
	 * Buffer and g2d stuff
	 */
	protected var bufferStrategy: BufferStrategy = _
	protected var enableRenderingHints = false
	protected var checkBorders = true
	protected var g2d: Graphics2D = _
	protected var frontg2d: Graphics2D = _
	protected var backg2d: Graphics2D = _
	var frontBuffer: BufferedImage = _
	protected var backBuffer: BufferedImage = _
	/**
	 * Set when the window is painted through Swing instead of a [[BufferStrategy]] (Wayland toolkit)
	 */
	private var renderPanel: JPanel = _
	protected var TRANSPARENT = new Color(0, 0, 0, 0)
	// Frame updates per second with rendering thread
	protected var target_fps = 0
	protected var current_fps = .0

	/**
	 * Selects if the FPS should be printed
	 *
	 * @param x FPS will be printed when set to true
	 */
	def displayFPS(x: Boolean): Unit = {
		DISPLAY_FPS = x
	}

	/**
	 * @see AcceleratedDisplay(Int, Int, Int, Int, String, Boolean)
	 * @param width        the width of the window (in pixels)
	 * @param height       the height of the window (in pixels)
	 * @param title        the title of the window
	 * @param high_quality whether to enable antialiasing or not
	 */
	def this(width: Int, height: Int, title: String, high_quality: Boolean) = {
		this()

		enableRenderingHints = high_quality
		initFrame(title, width, height, -1, -1)
	}

	/**
	 *
	 * @param width        the width of the window (in pixels)
	 * @param height       the height of the window (in pixels)
	 * @param xPos         the x offset position of the window on the screen, -1 for
	 *                     centered
	 * @param yPos         the y offset position of the window on the screen, -1 for
	 *                     centered
	 * @param title        the title of the window
	 * @param high_quality whether to enable antialiasing or not
	 */
	def this(width: Int, height: Int, xPos: Int, yPos: Int, title: String, high_quality: Boolean) = {
		this()

		enableRenderingHints = high_quality

		initFrame(title, width, height, xPos, yPos)
	}

	/**
	 * @param title   the title of the window
	 * @param width   the width of the window (in pixels)
	 * @param height  the height of the window (in pixels)
	 * @param xOffset the x offset of the window on the screen, -1 if centered
	 * @param yOffset the y offset of the window on the screen, -1 if centered
	 */
	private def initFrame(title: String, width: Int, height: Int, xOffset: Int, yOffset: Int, highDPI: Boolean = true): Unit = {
		// Must run before anything from AWT or Swing is touched (toolkit selection on Wayland)
		DisplaySetup.prepare()

		// Fixes strange HIDPI settings on Windows and Mac when using high DPI screens
		System.setProperty("sun.java2d.uiScale.enabled", "true")
		System.setProperty("sun.java2d.uiScale", "1.0")

		// First contact with the display: explain what is wrong if it cannot be opened
		val device = DisplaySetup.openingDisplay {
			// Shall we try a different look for the window ?
			try UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel")
			catch {
				case _: Exception =>
			}
			GraphicsEnvironment.getLocalGraphicsEnvironment.getDefaultScreenDevice
		}
		val gc = device.getDefaultConfiguration
		val tk = Toolkit.getDefaultToolkit

		/**
		 * Get refresh rate and align FPS to it
		 */
		val dm = device.getDisplayMode
		val refreshRate = dm.getRefreshRate
		if (refreshRate == DisplayMode.REFRESH_RATE_UNKNOWN) {
			System.out.println("[AccDisplay] Could not detect frame-rate, using 50 FPS")
			this.target_fps = 50
		}
		else target_fps = refreshRate

		// Get optimized image
		frontBuffer = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT)
		backBuffer = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT)
		frontg2d = frontBuffer.getGraphics.asInstanceOf[Graphics2D]
		backg2d = backBuffer.getGraphics.asInstanceOf[Graphics2D]

		// Sets active g2d to front and make the front layer transparent
		frontg2d.setBackground(TRANSPARENT)
		frontg2d.clearRect(0, 0, width, height)
		g2d = frontg2d

		if (enableRenderingHints) { // Enable anti-aliasing for shapes
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

			// Anti-alias for text
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
			g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY)
		}

		// The Wayland toolkit of the JetBrains Runtime has no page flipping, so no BufferStrategy:
		// the window is then painted through the regular Swing mechanism
		val swingPainting = tk.getClass.getName.endsWith("WLToolkit")

		SwingUtilities.invokeLater _ -> {
			mainFrame = new JFrame(title, gc)
			mainFrame.setResizable(false)
			mainFrame.setIgnoreRepaint(!swingPainting)
			mainFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
			if (swingPainting) installRenderPanel(width, height)
			else mainFrame.getContentPane.setPreferredSize(new Dimension(width, height))
			mainFrame.setIconImage(ImageIO.read(classOf[ImageGraphics].getResource("/res/img/isc_icon.png")))
			// mainFrame.setUndecorated(!hasDecoration);
			mainFrame.pack()
		}

		// Get the size of the screen
		val dim = Toolkit.getDefaultToolkit.getScreenSize

		// Determine the new location of the window
		fWidth = frontBuffer.getWidth
		fHeight = frontBuffer.getHeight
		var x = (dim.width - fWidth) / 2
		var y = (dim.height - fHeight) / 2
		if (xOffset != -1) x = xOffset
		if (yOffset != -1) y = yOffset

		// Move the window to the center of the screen
		mainFrame.setLocation(x, y)
		mainFrame.setVisible(true)

		if (!swingPainting) {
			try {
				mainFrame.createBufferStrategy(AcceleratedDisplay.numBuffers)
				while (bufferStrategy == null) bufferStrategy = mainFrame.getBufferStrategy
			} catch {
				case e: RuntimeException =>
					// Toolkit without buffer strategies, fall back to Swing painting
					if (AcceleratedDisplay.VERBOSE) System.out.println("[AccDisplay] No buffer strategy available (" + e + "), painting through Swing")
					mainFrame.setIgnoreRepaint(false)
					installRenderPanel(width, height)
					mainFrame.pack()
			}
		}

		/**
		 * Rendering thread
		 */
		class RenderThread extends SwingWorker[String, AnyRef] {
			private[graphics] val gt = new GraphicTimer
			private[graphics] var lastRender = 0L

			override def doInBackground(): String = {
				if (AcceleratedDisplay.VERBOSE) System.out.println("[AccDisplay] Rendering thread launched")
				try while ( {
					true
				}) {
					internalRender()
					gt.sync(target_fps)
					val now = System.nanoTime

					/**
					 * FPS calculation
					 */
					if (lastRender != 0 && DISPLAY_FPS) { // System.out.println(((now - lastRender) / 1000) + " uSec"); // Frame
						// rate is constant !
						current_fps = 1000000000.0f / (now - lastRender)
						current_fps = current_fps.round.toDouble
					}
					// FPS_COUNTER++;
					lastRender = now
				}
				catch {
					case e: Exception =>
						e.printStackTrace()
				}
				"Nothing !"
			}

			override protected def done(): Unit = {
			}
		}
		// Launch the rendering thread
		new RenderThread().execute()
	}

	/**
	 * Replaces the content pane by a panel that paints the two buffers in the Swing painting
	 * cycle. Used when the toolkit offers no [[BufferStrategy]] (Wayland). Swing's own double
	 * buffering avoids flickering.
	 *
	 * @param width  the width of the drawing area (in pixels)
	 * @param height the height of the drawing area (in pixels)
	 */
	private def installRenderPanel(width: Int, height: Int): Unit = {
		renderPanel = new JPanel {
			setOpaque(true)
			setBackground(Color.white)

			override def paintComponent(g: Graphics): Unit = {
				super.paintComponent(g)
				g.drawImage(backBuffer, 0, 0, null)
				frontBuffer synchronized g.drawImage(frontBuffer, 0, 0, null)
				if (DISPLAY_FPS) {
					g.setColor(Color.black)
					g.drawString("FPS - " + current_fps, (backBuffer.getWidth * 0.05).toInt, backBuffer.getHeight)
				}
			}
		}
		renderPanel.setPreferredSize(new Dimension(width, height))
		mainFrame.setContentPane(renderPanel)
	}

	/**
	 * This rendering method is called by the rendering thread, always
	 */
	private def internalRender(): Unit = {
		if (renderPanel != null) {
			// Swing painting: ask for a repaint, the panel composes the buffers on the event thread
			renderPanel.repaint()
			return
		}

		var g: Graphics2D = null
		try {
			g = bufferStrategy.getDrawGraphics.asInstanceOf[Graphics2D]
			// Copy the image buffer to the active buffer strategy
			// and make sure that the image is not modified during that time
			g.drawImage(backBuffer, mainFrame.getInsets.left, mainFrame.getInsets.top, null)
			frontBuffer synchronized g.drawImage(frontBuffer, mainFrame.getInsets.left, mainFrame.getInsets.top, null)
			g.setColor(Color.black)
			if (DISPLAY_FPS) g.drawString("FPS - " + current_fps, (backBuffer.getWidth * 0.05).toInt, backBuffer.getHeight)

			// Shows the contents of the back buffer on the screen.
			bufferStrategy.show()
			Toolkit.getDefaultToolkit.sync()
		} catch {
			case e: Exception =>

		} finally if (g != null) g.dispose()
	}

	/**
	 * Used for game loop synchronization
	 */
	private val gt = new GraphicTimer

	/**
	 * Call this method periodically to have a constant frame rate
	 *
	 * @param FPS the target frame rate
	 */
	def syncGameLogic(FPS: Int): Unit = {
		gt.sync(FPS)
	}
}
