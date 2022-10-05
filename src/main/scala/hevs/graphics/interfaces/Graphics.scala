package hevs.graphics.interfaces

import hevs.graphics.FunGraphics
import hevs.graphics.utils.GraphicsBitmap
import java.awt.Color
import java.awt.Polygon
import java.awt.Rectangle


/**
 * An interface that every graphic application
 * should have (common between {@link FunGraphics} and Gdx2d).
 *
 * @author Pierre-André Mudry
 * @version 1.0
 */
trait Graphics {
  /**
   * Method which cleans up the display. Everything becomes the background
   * again
   */
  def clear(): Unit

  /**
   * Method which cleans up the display. Everything becomes the background
   * again.
   */
  def clear(c: Color): Unit

  /**
   * Set the color of the future drawings
   *
   * @param c
   * Selected color for drawing
   */
  def setColor(c: Color): Unit

  /**
   * Draw the selected pixel with the color selected with setColor.
   *
   * @param x
   * X coordinate of the pixel
   * @param y
   * Y coordinate of the pixel
   */
  def setPixel(x: Int, y: Int): Unit

  /**
   * Draws a pixel with a given color. Does not change the current color.
   *
   * @param x
   * X coordinate
   * @param y
   * Y coordinate
   * @param c
   * Color to use for this pixel (this pixel only, see {@link # setColor ( Color )}
   */
  def setPixel(x: Int, y: Int, c: Color): Unit

  /**
   * Draws a pixel with a given color. Does not change the current color
   *
   * @param x
   * X coordinate
   * @param y
   * Y coordinate
   * @param c
   * Color to use (RGB coded)
   */
  def setPixel(x: Int, y: Int, c: Int): Unit

  /**
   * Sets the width of the pen
   *
   * @param width
   * The new width of the pen
   */
  def setPenWidth(width: Float): Unit

  /**
   * Draw a line from P1 to P2 in the color selected with setColor.
   *
   * @param p1x
   * X coordinate of P1
   * @param p1y
   * Y coordinate of P1
   * @param p2x
   * X coordinate of P2
   * @param p2y
   * Y coordinate of P2
   */
  def drawLine(p1x: Int, p1y: Int, p2x: Int, p2y: Int): Unit

  /**
   * Draw a filled polygon
   *
   * @param p
   * @param c
   */
  def drawFilledPolygon(p: Polygon, c: Color): Unit

  /**
   * Draw an empty rectangle in the color selected with setColor().
   *
   * @param posX
   * X coordinate of the top left corner of the rectangle
   * @param posY
   * Y coordinate of the top left corner of the rectangle
   * @param width
   * Width of the rectangle
   * @param height
   * Height of the rectangle
   */
  def drawRect(posX: Int, posY: Int, width: Int, height: Int): Unit

  /**
   * Draw a filled rectangle in the color selected with setColor.
   *
   * @param posX
   * X coordinate of the top left corner of the rectangle
   * @param posY
   * Y coordinate of the top left corner of the rectangle
   * @param width
   * Width of the rectangle
   * @param height
   * Height of the rectangle
   */
  def drawFillRect(posX: Int, posY: Int, width: Int, height: Int): Unit

  /**
   * Draw an empty rectangle in the color selected with setColor
   *
   * @param rect
   * rectangle to draw
   */
  def drawRect(rect: Rectangle): Unit

  /**
   * Draw a filled rectangle in the color selected with setColor.
   *
   * @param rect
   * rectangle to draw
   */
  def drawFillRect(rect: Rectangle): Unit

  /**
   * Draws a circle starting from <code>(Top left X, Top left Y)</code>
   *
   * @param posX
   * X top-left position of the circle
   * @param posY
   * Y top-left position of the circle
   * @param f
   * Diameter of the drawn circle
   */
  def drawCircle(posX: Int, posY: Int, f: Int): Unit

  /**
   * Draws a circle starting from <code>(Top left X, Top left Y)</code>
   *
   * @param posX
   * X top-left position of the circle
   * @param posY
   * Y top-left position of the circle
   * @param radius
   * Radius of the drawn circle
   */
  def drawFilledCircle(posX: Int, posY: Int, radius: Int): Unit

  /**
   * Draws an oval starting from <code>(Top left X, Top left Y)</code>
   *
   * @param posX
   * X top-left position of the circle
   * @param posY
   * Y top-left position of the circle
   * @param width
   * Width of the drawn oval
   * @param height
   * Height of the drawn oval
   */
  def drawFilledOval(posX: Int, posY: Int, width: Int, height: Int): Unit

  /**
   * Draws a string at a given location. Note that the boundaries are not
   * checked and text may be painted outside the window
   *
   * @param posX
   * X position of string
   * @param posY
   * Y position of string
   * @param str
   * the string to write
   */
  def drawString(posX: Int, posY: Int, str: String): Unit

  /**
   * Write the given string at <code>posX, posY</code>
   *
   * @param posX
   * Position x of the string
   * @param posY
   * Position y of the string
   * @param str
   * The string to be drawn
   * @param color
   * The color of the string
   * @param size
   * The size of the font
   */
  def drawString(posX: Int, posY: Int, str: String, color: Color, size: Int): Unit

  /**
   * Draws a text with a shadow
   *
   * @param posX
   * @param posY
   * @param str
   * @param color
   * @param size
   */
  def drawFancyString(posX: Int, posY: Int, str: String, color: Color, size: Int): Unit

  /**
   * Draw a centered picture from a file (gif, jpg, png) to <code>(posX, posY)</code>
   *
   * @param posX
   * X position of the image
   * @param posY
   * Y position of the image
   * @param bitmap
   * A bitmap
   */
  def drawPicture(posX: Int, posY: Int, bitmap: GraphicsBitmap): Unit

  /**
   * Draw a centered picture from a file (gif, jpg, png) to <code>(posX, posY)</code>. Warning,
   * very slow because the image has to be
   * reloaded
   *
   * @param posX
   * X position of the image
   * @param posY
   * Y position of the image
   * @param angle
   * The rotation angle of the image to be drawn
   * @param imageName
   * path of the image file
   */
  def drawTransformedPicture(posX: Int, posY: Int, angle: Double, scale: Double, imageName: String): Unit

  /**
   * Draw a centered picture from a file (gif, jpg, png) to <code>(posX, posY)</code>
   *
   * @param posX
   * X position of the image
   * @param posY
   * Y position of the image
   * @param angle
   * The rotation angle of the image to be drawn
   * @param bitmap
   * A bitmap
   */
  def drawTransformedPicture(posX: Int, posY: Int, angle: Double, scale: Double, bitmap: GraphicsBitmap): Unit

  /**
   * Draw a mirrored centered picture from a file (gif, jpg, png) to <code>(posX, posY)</code>
   *
   * @param posX
   * X position of the image
   * @param posY
   * Y position of the image
   * @param angle
   * The rotation angle of the image to be drawn
   * @param bitmap
   * A bitmap
   */
  def drawMirroredPicture(posX: Int, posY: Int, angle: Double, bitmap: GraphicsBitmap): Unit

  /**
   * Get the frame width
   * @return the frame width
   */
  def getFrameWidth(): Int

  /**
   * Get the frame height
   *
   * @return the frame height
   */
  def getFrameHeight(): Int
}