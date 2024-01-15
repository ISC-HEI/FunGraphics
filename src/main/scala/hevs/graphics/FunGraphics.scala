package hevs.graphics

import hevs.graphics.interfaces.{DualLayerGraphics, Graphics}
import hevs.graphics.utils.{GraphicsBitmap, RepeatingReleasedEventsFixer}

import java.awt._
import java.awt.event._
import java.awt.font.LineMetrics
import java.awt.geom.{AffineTransform, Rectangle2D}
import java.io._
import javax.imageio.ImageIO
import javax.swing.SwingConstants

/**
 * Factory for [[FunGraphics]].
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

  /**
   * Graphical demo/test of the library.
   * @param args unused
   */
  def main(args: Array[String]): Unit = { // Testing resources access
    new GraphicsBitmap("/res/img/EN_HEI.png")
    val fg = new FunGraphics(320, 320, "Testing performance of FunGraphics")
    fg.setPixel(10, 10)
    fg.gameloopSample()
  }

  /**
   * Creates a FunGraphics window
   *
   * @param width        Width of the window
   * @param height       Height of the window
   * @param xoffset      X-Position of the window on the screen
   * @param yoffset      Y-Position of the window on the screen
   * @param title        Title of the window
   * @param high_quality Use high quality rendering
   * @return the window
   */
  def apply(width: Int, height: Int, xoffset: Int, yoffset: Int, title: String, high_quality: Boolean) : FunGraphics = {
    new FunGraphics(width, height, xoffset, yoffset, title, high_quality)
  }

  /**
   * Creates a FunGraphics window
   *
   * @param width        Width of the window
   * @param height       Height of the window
   * @param title        Title of the window
   * @param high_quality Use high quality rendering
   * @return the window
   */
  def apply(width: Int, height: Int, title: String, high_quality: Boolean): FunGraphics = {
    new FunGraphics(width, height, title, high_quality)
  }

  /**
   * Creates a FunGraphics window
   *
   * @param width        Width of the window
   * @param height       Height of the window
   * @param title        Title of the window
   * @return the window
   */
  def apply(width: Int, height: Int, title: String): FunGraphics = {
    new FunGraphics(width, height, title)
  }

  /**
   * Creates a FunGraphics window
   * @param width        Width of the window
   * @param height       Height of the window
   * @return the window
   */
  def apply(width: Int, height: Int): FunGraphics = {
    new FunGraphics(width, height)
  }

  init()
}

/**
 * A graphics framework for games and experiments. Developed for the PImp and INF1 courses given at HES-SO Valais.
 *
 * @author Pierre-André Mudry <a href='mailto:pandre.mudry&#64;hevs.ch'></a>
 *
 * @constructor Creates a graphic window to draw onto.
 * @param width        Width of the window
 * @param height       Height of the window
 * @param xoffset      X-Position of the window on the screen
 * @param yoffset      Y-Position of the window on the screen
 * @param title        Title of the window
 * @param high_quality Use high quality rendering
 */
class FunGraphics(val width: Int, val height: Int, val xoffset: Int, val yoffset: Int, val title: String, val high_quality: Boolean)
  extends AcceleratedDisplay(width, height, xoffset, yoffset, title, high_quality) with Graphics with DualLayerGraphics {
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
   * @param width        Width of the display window
   * @param height       Height of the display window
   * @param title        Title of the display window
   *
   * @see #FunGraphics(int, int, String, boolean)
   */
  def this(width: Int, height: Int, title: String) = {
    this(width, height, title, true)
  }

  /**
   * Creates a graphic window to draw onto with a given title
   *
   * @param width  Width of the display window
   * @param height Height of the display window
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
   * Adds a [[MouseListener]] to the window to react on mouse events
   *
   * @param l The [[MouseListener]]
   */
  def addMouseListener(l: MouseListener): Unit = {
    mainFrame.getContentPane.addMouseListener(l)
  }

  /**
   * Adds a [[MouseMotionListener]] to the window to react on mouse movements
   *
   * @param m The [[MouseMotionListener]]
   */
  def addMouseMotionListener(m: MouseMotionListener): Unit = {
    mainFrame.getContentPane.addMouseMotionListener(m)
  }

  override def clear(): Unit = {
    g2d.clearRect(0, 0, fWidth, fHeight)
  }

  override def clear(c: Color): Unit = {
    val old = g2d.getBackground
    g2d.setBackground(c)
    g2d.clearRect(0, 0, fWidth, fHeight)
    g2d.setBackground(old)
  }

  override def setColor(c: Color): Unit = {
    g2d.setColor(c)
  }

  override def setPixel(x: Int, y: Int): Unit = { // Test that the pixel to set is in the frame
    if ((x < 0) || (y < 0) || (x >= getFrameWidth()) || (y >= getFrameHeight())) {
      if (checkBorders) System.out.println("[FunGraphics] Coordinates out of frame")
    }
    else frontBuffer.setRGB(x, y, g2d.getColor.getRGB)
  }

  override def setPixel(x: Int, y: Int, c: Color): Unit = {
    val oldColor = g2d.getColor
    setColor(c)
    setPixel(x, y)
    setColor(oldColor)
  }

  override def setPixel(x: Int, y: Int, c: Int): Unit = {
    setPixel(x, y, new Color(c))
  }

  override def setPenWidth(width: Float): Unit = {
    g2d.setStroke(new BasicStroke(width))
  }

  override def drawLine(p1x: Int, p1y: Int, p2x: Int, p2y: Int): Unit = {
    g2d.drawLine(p1x, p1y, p2x, p2y)
  }

  override def drawFilledPolygon(p: Polygon, c: Color): Unit = {
    val oldColor = g2d.getColor
    setColor(c)
    g2d.fill(p)
    g2d.drawPolygon(p)
    setColor(oldColor)
  }

  override def drawRect(posX: Int, posY: Int, width: Int, height: Int): Unit = {
    g2d.drawRect(posX, posY, width, height)
  }

  override def drawFillRect(posX: Int, posY: Int, width: Int, height: Int): Unit = {
    g2d.fillRect(posX, posY, width, height)
  }

  override def drawRect(rect: Rectangle): Unit = {
    g2d.drawRect(rect.getX.toInt, rect.y, rect.getWidth.toInt, rect.getHeight.toInt)
  }

  override def drawFillRect(rect: Rectangle): Unit = {
    g2d.drawRect(rect.getX.toInt, rect.y, rect.getWidth.toInt, rect.getHeight.toInt)
  }

  override def drawCircle(posX: Int, posY: Int, f: Int): Unit = {
    g2d.drawOval(posX, posY, f, f)
  }

  override def drawFilledCircle(posX: Int, posY: Int, diameter: Int): Unit = {
    g2d.fillOval(posX, posY, diameter, diameter)
  }

  override def drawFilledOval(posX: Int, posY: Int, width: Int, height: Int): Unit = {
    g2d.fillOval(posX, posY, width, height)
  }

  override def getStringSize(str: String, font: Font): Rectangle2D = {
    val metrics: LineMetrics = font.getLineMetrics(str, g2d.getFontRenderContext)
    val rect: Rectangle2D = font.getStringBounds(str, g2d.getFontRenderContext)
    val height: Double = rect.getHeight - metrics.getDescent - metrics.getLeading
    new Rectangle(rect.getWidth.toInt, height.toInt)
  }

  override def getStringSize(str: String): Rectangle2D = getStringSize(str, g2d.getFont)

  override def drawString(posX: Int, posY: Int, str: String, font: Font, color: Color): Unit = {
    val oldFont = g2d.getFont
    val oldColor = g2d.getColor

    g2d.setFont(font)
    g2d.setColor(color)
    g2d.drawString(str, posX, posY)
    g2d.setFont(oldFont)
    g2d.setColor(oldColor)
  }

  override def drawString(posX: Int,
                          posY: Int,
                          str: String,
                          font: Font,
                          color: Color,
                          halign: Int,
                          valign: Int): Unit = {

    val bounds: Rectangle2D = getStringSize(str, font)
    val w: Double = bounds.getWidth
    val h: Double = bounds.getHeight

    var x: Double = posX
    var y: Double = posY

    if (halign == SwingConstants.CENTER) {
      x -= w / 2.0
    } else if (halign == SwingConstants.RIGHT) {
      x -= w
    }

    if (valign == SwingConstants.CENTER) {
      y += h / 2.0
    } else if (valign == SwingConstants.TOP) {
      y += h
    }

    drawString(math.round(x).toInt, math.round(y).toInt, str, font, color)
  }

  override def drawString(posX: Int,
                          posY: Int,
                          str: String,
                          fontFamily: String = "SansSerif",
                          fontStyle: Int = Font.PLAIN,
                          fontSize: Int = 20,
                          color: Color = Color.BLACK,
                          halign: Int = SwingConstants.LEFT,
                          valign: Int = SwingConstants.BOTTOM): Unit = {

    val font = new Font(fontFamily, fontStyle, fontSize)
    drawString(posX, posY, str, font, color, halign, valign)
  }

  override def drawString(posX: Int, posY: Int, str: String, color: Color, size: Int): Unit = {
    drawString(posX, posY, str, fontSize = size, color = color)
  }

  override def drawFancyString(posX: Int, posY: Int, str: String, color: Color, size: Int): Unit = {
    val font: Font = new Font("Georgia", Font.BOLD, size)
    drawString(posX+2, posY+2, str, font, color = Color.GRAY)
    drawString(posX, posY, str, font, color = color)
  }

  override def drawFancyString(posX: Int,
                               posY: Int,
                               str: String,
                               fontFamily: String = "Georgia",
                               fontStyle: Int = Font.BOLD,
                               fontSize: Int = 20,
                               color: Color = Color.BLACK,
                               halign: Int = SwingConstants.LEFT,
                               valign: Int = SwingConstants.BOTTOM,
                               shadowX: Int = 0,
                               shadowY: Int = 0,
                               shadowColor: Color = Color.GRAY,
                               shadowThickness: Int = 0,
                               outlineColor: Color = Color.WHITE,
                               outlineThickness: Int = 0): Unit = {

    val font: Font = new Font(fontFamily, fontStyle, fontSize)

    if (shadowThickness > 0) {
      val bounds: Rectangle2D = getStringSize(str, font)
      val w: Double = bounds.getWidth
      val h: Double = bounds.getHeight

      var cx: Double = posX
      var cy: Double = posY

      if (halign == SwingConstants.LEFT) {
        cx += w / 2.0
      } else if (halign == SwingConstants.RIGHT) {
        cx -= w / 2.0
      }

      if (valign == SwingConstants.TOP) {
        cy += h / 2.0
      } else if (valign == SwingConstants.BOTTOM) {
        cy -= h / 2.0
      }

      val font2: Font = new Font(fontFamily, fontStyle, fontSize+shadowThickness)
      drawString(
        math.round(cx + shadowX).toInt,
        math.round(cy + shadowY).toInt,
        str,
        font2,
        shadowColor,
        SwingConstants.CENTER,
        SwingConstants.CENTER)
    }

    if (outlineThickness > 0) {
      for (dy: Int <- -outlineThickness to outlineThickness) {
        for (dx: Int <- -outlineThickness to outlineThickness) {
          drawString(posX + dx, posY + dy, str, font, outlineColor, halign, valign)
        }
      }
    }

    drawString(posX, posY, str, font, color, halign, valign)
  }

  override def drawPicture(posX: Int, posY: Int, bitmap: GraphicsBitmap): Unit = {
    g2d.drawImage(bitmap.mBitmap, posX - bitmap.getWidth / 2, posY - bitmap.getHeight / 2, null)
  }

  override def drawTransformedPicture(posX: Int, posY: Int, angle: Double, scale: Double, imageName: String): Unit = {
    drawTransformedPicture(posX, posY, angle, scale, new GraphicsBitmap(imageName))
  }

  override def drawTransformedPicture(posX: Int, posY: Int, angle: Double, scale: Double, bitmap: GraphicsBitmap): Unit = {
    val t = new AffineTransform
    t.rotate(angle, posX, posY)
    t.translate(posX - bitmap.getWidth / 2 * scale, posY - bitmap.getHeight / 2 * scale)
    t.scale(scale, scale)
    g2d.drawImage(bitmap.mBitmap, t, null)
  }

  override def drawMirroredPicture(posX: Int, posY: Int, angle: Double, bitmap: GraphicsBitmap): Unit = {
    val t = new AffineTransform
    t.rotate(angle, posX, posY)
    t.translate(posX + bitmap.getWidth / 2, posY - bitmap.getHeight / 2)
    t.scale(-1, 1)
    g2d.drawImage(bitmap.mBitmap, t, null)
  }

  override def drawBackground(): Unit = {
    g2d.setBackground(TRANSPARENT)
    g2d = backg2d
  }

  override def drawForeground(): Unit = {
    g2d = frontg2d
  }

  override def getFrameWidth(): Int = fWidth
  override def getFrameHeight(): Int = fHeight

  override def getAvailableFonts(): Array[String] = GraphicsEnvironment.getLocalGraphicsEnvironment.getAvailableFontFamilyNames


  private[graphics] var pressedUp = false
  private[graphics] var pressedDown = false
  private[graphics] var size = 1

  /**
   * A sample game loop using explicit synchronization (if display flickers)
   */
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