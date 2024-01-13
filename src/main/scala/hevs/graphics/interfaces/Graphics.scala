package hevs.graphics.interfaces

import hevs.graphics.FunGraphics
import hevs.graphics.utils.GraphicsBitmap

import java.awt.geom.Rectangle2D
import java.awt.{Color, Font, Polygon, Rectangle}


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
   * Color to use for this pixel (this pixel only, see [[setColor setColor(Color)]]
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
   * Draw a line from `P1` to `P2` in the color selected with [[setColor]].
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
   * @param p the polygon to draw
   * @param c the color of the polygon
   */
  def drawFilledPolygon(p: Polygon, c: Color): Unit

  /**
   * Draw an empty rectangle in the color selected with [[setColor]].
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
   * Draw a filled rectangle in the color selected with [[setColor]].
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
   * Draw an empty rectangle in the color selected with [[setColor]]
   *
   * @param rect
   * rectangle to draw
   */
  def drawRect(rect: Rectangle): Unit

  /**
   * Draw a filled rectangle in the color selected with [[setColor]].
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
   * @param diameter
   * Diameter of the drawn circle
   */
  def drawFilledCircle(posX: Int, posY: Int, diameter: Int): Unit

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
   * Computes the size necessary to render a string with the given font
   * @param str
   * the string
   * @param font
   * the font
   * @return the bounding box of the rendered string
   */
  def getStringSize(str: String, font: Font): Rectangle2D


  /**
   * Computes the size necessary to render a string with the current font
   * @param str
   * the string
   * @return the bounding box of the rendered string
   */
  def getStringSize(str: String): Rectangle2D

  /**
   * Draws a string at a given location with the given font and color. Note that the boundaries are not
   * checked and text may be painted outside the window
   * @param posX
   * X position of string
   * @param posY
   * Y position of string
   * @param str
   * the string to write
   * @param font
   * the font
   * @param color
   * the text color
   */
  def drawString(posX: Int,
                 posY: Int,
                 str: String,
                 font: Font,
                 color: Color): Unit

  /**
   * Draws a string at a given location with the given font, color and alignments. Note that the boundaries are not
   * checked and text may be painted outside the window
   * @param posX
   * X position of string
   * @param posY
   * Y position of string
   * @param str
   * the string to write
   * @param font
   * the font
   * @param color
   * the text color
   * @param halign
   * the horizontal alignment (see [[javax.swing.SwingConstants]])
   * Valid values: LEFT, CENTER and RIGHT
   * @param valign
   * the vertical alignment (see [[javax.swing.SwingConstants]])
   * Valid values: TOP, CENTER and BOTTOM
   */
  def drawString(posX: Int,
                 posY: Int,
                 str: String,
                 font: Font,
                 color: Color,
                 halign: Int,
                 valign: Int): Unit

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
   * @param fontFamily
   * the font family
   * @param fontStyle
   * the font style ([[Font.PLAIN]], [[Font.BOLD]], [[Font.ITALIC]], ...)
   * @param fontSize
   * the font size
   * @param color
   * the text color
   * @param halign
   * the horizontal alignment (see [[javax.swing.SwingConstants]])
   * Valid values: LEFT, CENTER and RIGHT
   * @param valign
   * the vertical alignment (see [[javax.swing.SwingConstants]])
   * Valid values: TOP, CENTER and BOTTOM
   */
  def drawString(posX: Int,
                 posY: Int,
                 str: String,
                 fontFamily: String,
                 fontStyle: Int,
                 fontSize: Int,
                 color: Color,
                 halign: Int,
                 valign: Int): Unit

  /**
   * Write the given string at `(posX, posY)`
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
   * X position of the string
   * @param posY
   * Y position of the string
   * @param str
   * the string to draw
   * @param color
   * the text color
   * @param size
   * the font size
   */
  def drawFancyString(posX: Int, posY: Int, str: String, color: Color, size: Int): Unit

  /**
   * Draws a text with a shadow and/or outline
   * @param posX
   * X position of the string
   * @param posY
   * Y position of the string
   * @param str
   * the string to draw
   * @param fontFamily
   * the font family
   * @param fontStyle
   * the font style ([[Font.PLAIN]], [[Font.BOLD]], [[Font.ITALIC]], ...)
   * @param fontSize
   * the font size
   * @param color
   * the text color
   * @param halign
   * the horizontal alignment (see [[javax.swing.SwingConstants]])
   * Valid values: LEFT, CENTER and RIGHT
   * @param valign
   * the vertical alignment (see [[javax.swing.SwingConstants]])
   * Valid values: TOP, CENTER and BOTTOM
   * @param shadowX
   * the shadow's X offset
   * @param shadowY
   * the shadow's Y offset
   * @param shadowColor
   * the shadow color
   * @param shadowThickness
   * the shadow thickness
   * @param outlineColor
   * the outline color
   * @param outlineThickness
   * the outline thickness
   */
  def drawFancyString(posX: Int, posY: Int, str: String, fontFamily: String, fontStyle: Int, fontSize: Int, color: Color, halign: Int, valign: Int, shadowX: Int, shadowY: Int, shadowColor: Color, shadowThickness: Int, outlineColor: Color, outlineThickness: Int): Unit

  /**
   * Draw a centered picture from a file (gif, jpg, png) to `(posX, posY)`
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
   * Draw a centered picture from a file (gif, jpg, png) to `(posX, posY)`. Warning,
   * very slow because the image has to be
   * reloaded
   *
   * @param posX
   * X position of the image
   * @param posY
   * Y position of the image
   * @param angle
   * The rotation angle of the image to be drawn
   * @param scale
   * The scale factor of the image to be drawn
   * @param imageName
   * path of the image file
   */
  def drawTransformedPicture(posX: Int, posY: Int, angle: Double, scale: Double, imageName: String): Unit

  /**
   * Draw a centered picture from a file (gif, jpg, png) to `(posX, posY)`
   *
   * @param posX
   * X position of the image
   * @param posY
   * Y position of the image
   * @param angle
   * The rotation angle of the image to be drawn
   * @param scale
   * The scale factor of the image to be drawn
   * @param bitmap
   * A bitmap
   */
  def drawTransformedPicture(posX: Int, posY: Int, angle: Double, scale: Double, bitmap: GraphicsBitmap): Unit

  /**
   * Draw a mirrored centered picture from a file (gif, jpg, png) to `(posX, posY)`
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

  /**
   * Returns a list of available font names on the device
   * @return the list of available font names
   */
  def getAvailableFonts(): Array[String]
}