package hevs.graphics.utils

import java.awt.{GraphicsConfiguration, GraphicsEnvironment}
import java.awt.image.BufferedImage
import javax.imageio.ImageIO


/**
 * GraphicsBitmap contains the methods required to create a [[BufferedImage]] from a
 * [[String]] if the file exists
 *
 * 1.3 : Added acceleration for images using graphics card
 *
 * @version 1.3, April 2011
 * @author <a href='mailto:pandre.mudry&#64;hevs.ch'> Pierre-Andre Mudry</a>
 */
class GraphicsBitmap(val name: String) { // Get optimized image
  final private var WIDTH = 0
  final private var HEIGHT = 0
  var mBitmap: BufferedImage = null
  val gc: GraphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment.getDefaultScreenDevice.getDefaultConfiguration
  try {
    val v = classOf[GraphicsBitmap].getResource(name)
    mBitmap = ImageIO.read(v)
  } catch {
    case e: Exception =>
      System.out.println("Could not find image " + name + ", exiting !")
      e.printStackTrace()
      System.exit(-1)
  } finally if (mBitmap != null) {
    WIDTH = mBitmap.getWidth
    HEIGHT = mBitmap.getHeight
  }
  else {
    WIDTH = 0
    HEIGHT = 0
  }
  /**
   * @return width of the image
   */
  def getWidth: Int = WIDTH

  /**
   * @return height of the image
   */
  def getHeight: Int = HEIGHT

  /**
   * @return the [[BufferedImage]] corresponding to the
   *         [[GraphicsBitmap]]
   */
  def getBufferedImage: BufferedImage = mBitmap
}
