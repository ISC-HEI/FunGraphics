package hevs.graphics

import hevs.graphics.interfaces.DualLayerGraphics
import hevs.graphics.interfaces.Graphics
import hevs.graphics.utils.GraphicsBitmap
import hevs.graphics.utils.RepeatingReleasedEventsFixer

import javax.imageio.ImageIO
import javax.imageio.ImageWriter
import javax.imageio.stream.ImageOutputStream
import java.awt._
import java.awt.event._
import java.awt.font.TextLayout
import java.awt.geom.AffineTransform
import java.io._
import java.util
import java.util.Iterator


/**
 * A graphics framework for games and experiments. Developed for the INF1 course
 * given at HES-SO Valais.
 *
 * @author Pierre-André Mudry <a href='mailto:pandre.mudry&#64;hevs.ch'></a>
 */
object FunGraphics {
  var major = 0
  var minor = 0
  private var versionString = "ERROR:NO_VERSION"

  /**
   * Get the version of the library
   *
   * @return A String containing the version
   */
  def version: String = versionString

  /**
   * Initialize the members
   */
  private def init(): Unit = {
    val f = "/res/generated/version.txt"
    var v:String = null
    try {
      val in = classOf[FunGraphics].getResourceAsStream(f)
      if (in == null) {
        System.err.println(String.format("resource '%s' not found, not using the .jar? version will be wrong", f))
        return
      }
      val b = new BufferedReader(new InputStreamReader(in))
      if (b.ready) v = b.readLine
      if (v.contains("dirty") || v.contains("-")) System.err.println(String.format("WARNING: using non-release version '%s'", v))
      versionString = v
      val e = v.split("[.]")
      major = e(0).toInt
      minor = e(1).toInt
    } catch {
      case e: IOException =>
        e.printStackTrace()
      case e: Exception =>
        System.err.println(String.format("Failed to parse : %s'", v))
        e.printStackTrace()
    }
  }

  def main(args: Array[String]): Unit = { // Testing resources access
    new GraphicsBitmap("/res/img/EN_HEI.png")
    val fg = new FunGraphics(320, 320, "Testing performance of FunGraphics")
    fg.setPixel(10, 10)
    fg.gameloopSample()
  }

  def apply(width: Int, height: Int, xoffset: Int, yoffset: Int, title: String, high_quality: Boolean) : FunGraphics = {
    new FunGraphics(width, height, xoffset, yoffset, title, high_quality)
  }
  def apply(width: Int, height: Int, title: String, high_quality: Boolean): FunGraphics = {
    new FunGraphics(width, height, title, high_quality)
  }
  def apply(width: Int, height: Int, title: String): FunGraphics = {
    new FunGraphics(width, height, title)
  }
  def apply(width: Int, height: Int): FunGraphics = {
    new FunGraphics(width, height)
  }

  init()
}

class FunGraphics(val width: Int, val height: Int, val xoffset: Int, val yoffset: Int, val title: String, val high_quality: Boolean)

/**
 * Creates a graphic window to draw onto.
 *
 * @param width        Width of the window
 * @param height       Height of the window
 * @param xoffset      X-Position of the window on the screen
 * @param yoffset      Y-Position of the window on the screen
 * @param title        Title of the window
 * @param high_quality Use high quality rendering
 */ extends AcceleratedDisplay(width, height, xoffset, yoffset, title, high_quality) with Graphics with DualLayerGraphics {
  System.out.println("Fungraphics - HES-SO Valais (mui), v" + FunGraphics.version)
  // Emulates SimpleGraphics default behavior
  this.clear(Color.white)
  g2d.setBackground(Color.white)
  this.setColor(Color.black)
  new RepeatingReleasedEventsFixer().install()

  /**
   * Creates a graphic window to draw onto.
   *
   * @param width        Width of the display window
   * @param height       Height of the display window
   * @param title        Title of the display window
   * @param high_quality Use high quality rendering (slower)
   */
  def this(width: Int, height: Int, title: String, high_quality: Boolean) = {
    this(width, height, -1, -1, title, high_quality)
  }

  /**
   * Creates a graphic window to draw onto with a given title
   *
   * @see #FunGraphics(int, int, String, boolean)
   */
  def this(width: Int, height: Int, title: String) = {
    this(width, height, title, true)
  }

  /**
   * Creates a graphic window to draw onto
   *
   * @see #FunGraphics(int, int, String, boolean)
   */
  def this(width: Int, height: Int) = {
    this(width, height, "FunGraphics ")
  }

  /**
   * Sets a keyboard listener
   *
   * @param k The KeyListener to listen to
   */
  def setKeyManager(k: KeyListener): Unit = {
    mainFrame.addKeyListener(k)
  }

  /**
   * Adds a {@link MouseListener} to the window to react on mouse events
   *
   * @param l The {@link MouseListener}
   */
  def addMouseListener(l: MouseListener): Unit = {
    mainFrame.getContentPane.addMouseListener(l)
  }

  /**
   * Adds a {@link MouseMotionListener} to the window to react on mouse movements
   *
   * @param m The {@link MouseMotionListener}
   */
  def addMouseMotionListener(m: MouseMotionListener): Unit = {
    mainFrame.getContentPane.addMouseMotionListener(m)
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#clear()
     */ override def clear(): Unit = {
    g2d.clearRect(0, 0, fWidth, fHeight)
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#clear(java.awt.Color)
     */ override def clear(c: Color): Unit = {
    val old = g2d.getBackground
    g2d.setBackground(c)
    g2d.clearRect(0, 0, fWidth, fHeight)
    g2d.setBackground(old)
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#setColor(java.awt.Color)
     */ override def setColor(c: Color): Unit = {
    g2d.setColor(c)
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#setPixel(int, int)
     */ override def setPixel(x: Int, y: Int): Unit = { // Test that the pixel to set is in the frame
    if ((x < 0) || (y < 0) || (x >= getFrameWidth) || (y >= getFrameHeight)) if (checkBorders) System.out.println("[FunGraphics] Coordinates out of frame")
    else frontBuffer.setRGB(x, y, g2d.getColor.getRGB)
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#setPixel(int, int, java.awt.Color)
     */ override def setPixel(x: Int, y: Int, c: Color): Unit = {
    val oldColor = g2d.getColor
    setColor(c)
    setPixel(x, y)
    setColor(oldColor)
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#setPixel(int, int, int)
     */ override def setPixel(x: Int, y: Int, c: Int): Unit = {
    setPixel(x, y, new Color(c))
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#setPenWidth(float)
     */ override def setPenWidth(width: Float): Unit = {
    g2d.setStroke(new BasicStroke(width))
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#drawLine(int, int, int, int)
     */ override def drawLine(p1x: Int, p1y: Int, p2x: Int, p2y: Int): Unit = {
    g2d.drawLine(p1x, p1y, p2x, p2y)
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#drawFilledPolygon(java.awt.Polygon,
     * java.awt.Color)
     */ override def drawFilledPolygon(p: Polygon, c: Color): Unit = {
    val oldColor = g2d.getColor
    setColor(c)
    g2d.fill(p)
    g2d.drawPolygon(p)
    setColor(oldColor)
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#drawRect(int, int, int, int)
     */ override def drawRect(posX: Int, posY: Int, width: Int, height: Int): Unit = {
    g2d.drawRect(posX, posY, width, height)
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#drawFillRect(int, int, int, int)
     */ override def drawFillRect(posX: Int, posY: Int, width: Int, height: Int): Unit = {
    g2d.fillRect(posX, posY, width, height)
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#drawRect(java.awt.Rectangle)
     */ override def drawRect(rect: Rectangle): Unit = {
    g2d.drawRect(rect.getX.toInt, rect.y, rect.getWidth.toInt, rect.getHeight.toInt)
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#drawFillRect(java.awt.Rectangle)
     */ override def drawFillRect(rect: Rectangle): Unit = {
    g2d.drawRect(rect.getX.toInt, rect.y, rect.getWidth.toInt, rect.getHeight.toInt)
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#drawCircle(int, int, int)
     */ override def drawCircle(posX: Int, posY: Int, f: Int): Unit = {
    g2d.drawOval(posX, posY, f, f)
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#drawFilledCircle(int, int, int)
     */ override def drawFilledCircle(posX: Int, posY: Int, radius: Int): Unit = {
    g2d.fillOval(posX, posY, radius, radius)
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#drawFilledOval(int, int, int, int)
     */ override def drawFilledOval(posX: Int, posY: Int, width: Int, height: Int): Unit = {
    g2d.fillOval(posX, posY, width, height)
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#drawString(int, int, java.lang.String)
     */ override def drawString(posX: Int, posY: Int, str: String): Unit = {
    g2d.drawString(str, posX, posY)
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#drawString(int, int, java.lang.String,
     * java.awt.Color, int)
     */ override def drawString(posX: Int, posY: Int, str: String, color: Color, size: Int): Unit = {
    val oldFont = g2d.getFont
    val oldColor = g2d.getColor
    val font = new Font("SansSerif", Font.PLAIN, size)
    g2d.setFont(font)
    g2d.setColor(color)
    g2d.drawString(str, posX, posY)
    g2d.setFont(oldFont)
    g2d.setColor(oldColor)
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#drawFancyString(int, int, java.lang.String,
     * java.awt.Color, int)
     */ override def drawFancyString(posX: Int, posY: Int, str: String, color: Color, size: Int): Unit = {
    val g2 = g2d
    val oldFont = g2d.getFont
    val oldColor = g2d.getColor
    val font = new Font("Georgia", Font.BOLD, size)
    val textLayout = new TextLayout(str, font, g2.getFontRenderContext)
    g2.setColor(Color.GRAY)
    textLayout.draw(g2, (posX + 2).toFloat, (posY + 2).toFloat)
    g2.setColor(color)
    textLayout.draw(g2, posX.toFloat, posY.toFloat)
    g2.setFont(oldFont)
    g2.setColor(oldColor)
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#drawPicture(int, int,
     * hevs.graphics.utils.GraphicsBitmap)
     */ override def drawPicture(posX: Int, posY: Int, bitmap: GraphicsBitmap): Unit = {
    g2d.drawImage(bitmap.mBitmap, posX - bitmap.getWidth / 2, posY - bitmap.getHeight / 2, null)
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#drawTransformedPicture(int, int, double, double,
     * java.lang.String)
     */ override def drawTransformedPicture(posX: Int, posY: Int, angle: Double, scale: Double, imageName: String): Unit = {
    drawTransformedPicture(posX, posY, angle, scale, new GraphicsBitmap(imageName))
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#drawTransformedPicture(int, int, double, double,
     * hevs.graphics.utils.GraphicsBitmap)
     */ override def drawTransformedPicture(posX: Int, posY: Int, angle: Double, scale: Double, bitmap: GraphicsBitmap): Unit = {
    val t = new AffineTransform
    t.rotate(angle, posX, posY)
    t.translate(posX - bitmap.getWidth / 2 * scale, posY - bitmap.getHeight / 2 * scale)
    t.scale(scale, scale)
    g2d.drawImage(bitmap.mBitmap, t, null)
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#drawMirroredPicture(int, int, double,
     * hevs.graphics.utils.GraphicsBitmap)
     */ override def drawMirroredPicture(posX: Int, posY: Int, angle: Double, bitmap: GraphicsBitmap): Unit = {
    val t = new AffineTransform
    t.rotate(angle, posX, posY)
    t.translate(posX + bitmap.getWidth / 2, posY - bitmap.getHeight / 2)
    t.scale(-1, 1)
    g2d.drawImage(bitmap.mBitmap, t, null)
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.DualLayerGraphics#drawBackground()
     */ override def drawBackground(): Unit = {
    g2d.setBackground(TRANSPARENT)
    g2d = backg2d
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.DualLayerGraphics#drawForeground()
     */ override def drawForeground(): Unit = {
    g2d = frontg2d
  }

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#getFrameWidth()
     */ override def getFrameWidth: Int = fWidth

  /*
     * (non-Javadoc)
     *
     * @see hevs.graphics.Graphics#getFrameHeight()
     */ override def getFrameHeight: Int = fHeight

  /**
   * A sample game loop using explicit synchronization (if display flickers)
   */
  private[graphics] var pressedUp = false
  private[graphics] var pressedDown = false
  private[graphics] var size = 1

  private[graphics] def gameloopSample(): Unit = {
    var i = 1
    var direction = 1
    // Do something when a key has been pressed
    this.setKeyManager(new KeyAdapter() { // Will be called when a key has been pressed
      override def keyPressed(e: KeyEvent): Unit = {
        if (e.getKeyCode == KeyEvent.VK_S) {
          System.out.println("Saving file...")
          saveAsPNG("fungraphics_screenshot")
        }
        if (e.getKeyCode == KeyEvent.VK_LEFT) pressedUp = true
        if (e.getKeyCode == KeyEvent.VK_RIGHT) pressedDown = true
      }

      override def keyReleased(e: KeyEvent): Unit = {
        if (e.getKeyCode == KeyEvent.VK_LEFT) pressedUp = false
        if (e.getKeyCode == KeyEvent.VK_RIGHT) pressedDown = false
      }
    })
    while ( {
      true
    }) {
      if (pressedUp) size += 1
      if (pressedDown) size = if (size == 0) 0
      else size - 1
      frontBuffer synchronized clear(Color.white)
      setColor(Color.red)
      drawFilledOval(10, 10, 100 + size, 100 + size)
      drawString(50, 250, "FunGraphics " + FunGraphics.version)
      setColor(Color.yellow)
      drawFillRect(50 + i, 50 - i, 100 + i, 100 + i)

      i += direction
      if (i > 100 || i <= 0) direction *= -1
      syncGameLogic(60)
    }
  }

  /**
   * Creates a screenshot of the current window on the disk.
   *
   * @param fileName The name of the file
   */
  private[graphics] def saveAsPNG(fileName: String): Unit = {
    try {
      val imageWriters = ImageIO.getImageWritersByFormatName("PNG")
      val imageWriter = imageWriters.next
      val file = new File(fileName + ".png")
      val ios = ImageIO.createImageOutputStream(file)
      imageWriter.setOutput(ios)
      imageWriter.write(this.frontBuffer)
      ios.close()
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }
  }
}