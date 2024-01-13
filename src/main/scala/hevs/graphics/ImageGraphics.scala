package hevs.graphics

import java.awt.image.BufferedImage
import java.awt.{Color, Graphics, Toolkit}
import javax.imageio.ImageIO
import javax.swing.JFrame

/**
 * [[ImageGraphics]] helpers.
 */
@SerialVersionUID(6832022057915586803L)
object ImageGraphics {
  /**
   * Converts a color array to a black-or-white array
   *
   * @param c
   * The color array
   * @return The array converted to BW
   */
  def convertToGray(c: Array[Array[Color]]): Array[Array[Color]] = {
    val w = c.length
    val h = c(0).length
    val values = Array.ofDim[Color](w, h)
    // FIXME this is slow
    for (i <- 0 until w) {
      for (j <- 0 until h) {
        val col = c(i)(j)
        val intColor = (0.3 * col.getRed + 0.59 * col.getGreen + 0.11 * col.getBlue).toInt
        values(i)(j) = new Color(intColor, intColor, intColor)
      }
    }
    values
  }

  /**
   * Converts a color array to a black-or-white array as Int values
   * @param c The color array
   * @return The array converted to BW
   */
  def convertToGrayInt(c: Array[Array[Color]]): Array[Array[Int]] = {
    val w = c.length
    val h = c(0).length
    val values = Array.ofDim[Int](w, h)
    for (i <- 0 until w) {
      for (j <- 0 until h) {
        val col = c(i)(j)
        val intColor = (0.3 * col.getRed + 0.59 * col.getGreen + 0.11 * col.getBlue).toInt
        values(i)(j) = intColor
      }
    }
    values
  }

  /**
   * Test of the class
   * @param args unused
   */
  def main(args: Array[String]): Unit = {
    val imageUsed = "/images/lena.bmp"
    val org = new ImageGraphics(imageUsed, "Original", 0, 0)
  }
}

/**
 * This class was made to deal with images as multidimensional arrays.
 * Mainly used in the `ImageProcessing` lab. It expects the images to reside in the `src` directory
 *
 * @author Pierre-André Mudry
 * @version 1.0
 * @constructor
 * @param backGroundFilePath the path of the file
 * @param windowTitle the title
 * @param xPositionOffset the x offset
 * @param yPositionOffset the y offset
 */
@SerialVersionUID(6832022057915586803L)
class ImageGraphics(val backGroundFilePath: String, val windowTitle: String, val xPositionOffset: Int, val yPositionOffset: Int) extends JFrame {
  private var backgroundBitmap: BufferedImage = null
  private var w = 0
  private var h = 0

    try { // Fill the frame content with the image
      try {
        backgroundBitmap = ImageIO.read(classOf[ImageGraphics].getResource(backGroundFilePath))
        w = backgroundBitmap.getWidth
        h = backgroundBitmap.getHeight
      } catch {
        case e: Exception =>
          System.out.println("Could not find image " + backGroundFilePath + ", exiting !")
          e.printStackTrace()
          System.exit(-1)
      }

      this.setResizable(false)
      this.setSize(backgroundBitmap.getWidth, backgroundBitmap.getHeight)
      this.setTitle(windowTitle)
      this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
      // Get the size of the screen
      val dim = Toolkit.getDefaultToolkit.getScreenSize
      // Determine the new location of the window
      val lw = this.getSize.width
      val lh = this.getSize.height
      val x = (dim.width - lw) / 2 + xPositionOffset
      val y = (dim.height - lh) / 2 + yPositionOffset
      // Move the window
      this.setLocation(x, y)
      this.setVisible(true)
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }

  /**
   * Sets a grayscale pixel, does not sets values for invalid pixels
   * outside the screen. Does not repaint the screen either because it
   * is slow. If required, please call [[java.awt.Component#repaint()]] if needed after
   * you have updated all the pixels you need.
   *
   * @param x X position of the pixel
   * @param y Y position of the pixel
   * @param intensity grayscale value of the pixel
   */
  def setPixelBW(x: Int, y: Int, intensity: Int): Unit = {
    if (!((x < 0) || (y < 0) || (x >= w) || (y >= h))) backgroundBitmap.setRGB(x, y, intensity << 16 | intensity << 8 | intensity)
  }

  /**
   * Sets an array of grayscale pixels (from 0 to 255) and displays them
   *
   * @param pixels the 2D array of grayscale pixels
   */
  def setPixelsBW(pixels: Array[Array[Int]]): Unit = {
    try {
      if (pixels(0).length != h || pixels.length != w) throw new Exception("Invalid size of the pixel array !")
      for (i <- 0 until w) {
        for (j <- 0 until h) { // FIXME this is slow, should use rasters instead
          val c = pixels(i)(j) << 16 | pixels(i)(j) << 8 | pixels(i)(j)
          backgroundBitmap.setRGB(i, j, c)
        }
      }
      this.repaint()
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }
  }

  /**
   * Sets an array of pixels of [[Color]] and displays them
   *
   * @param pixels the 2D of [[Color]] pixels
   */
  def setPixelsColor(pixels: Array[Array[Color]]): Unit = {
    try {
      if (pixels(0).length != h || pixels.length != w) throw new Exception("Invalid size of the pixel array !")
      for (i <- 0 until w) {
        for (j <- 0 until h) {
          backgroundBitmap.setRGB(i, j, pixels(i)(j).getRGB)
        }
      }
      this.repaint()
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }
  }

  /**
   * Gets a single pixel from the background image and returns its
   * grayscale value
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @return the pixel's grayscale value
   */
  def getPixelBW(x: Int, y: Int): Int = if ((x < 0) || (y < 0) || (x >= w) || (y >= h)) 0
  else { // Inside the image. Make the gray conversion and return the value
    val c = new Color(backgroundBitmap.getRGB(x, y))
    (0.3 * c.getRed + 0.59 * c.getGreen + 0.11 * c.getBlue).toInt
  }

  /**
   * Gets the array of the pixels (which have been converted to grayscale
   * if required)
   *
   * @return the 2D array of grayscale pixels
   */
  def getPixelsBW(): Array[Array[Int]] = {
    val values = Array.ofDim[Int](w, h)
    for (i <- 0 until w) {
      for (j <- 0 until h) {
        val c = new Color(backgroundBitmap.getRGB(i, j))
        values(i)(j) = (0.3 * c.getRed + 0.59 * c.getGreen + 0.11 * c.getBlue).toInt
      }
    }
    values
  }

  /**
   * Gets the array of the pixels as [[Color]]s
   *
   * @return the 2D array of [[Color]] pixels
   */
  def getPixelsColor(): Array[Array[Color]] = {
    val values = Array.ofDim[Color](w, h)
    for (i <- 0 until w) {
      for (j <- 0 until h) {
        values(i)(j) = new Color(backgroundBitmap.getRGB(i, j))
      }
    }
    values
  }

  /**
   * Paint method
   */
  override def paint(g: Graphics): Unit = {
    g.drawImage(backgroundBitmap, 0, 0, null)
    g.dispose()
  }
}
