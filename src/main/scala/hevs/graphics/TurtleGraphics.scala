package hevs.graphics

import java.awt.{BasicStroke, Color, Point}
import java.awt.event.MouseMotionListener

/**
 * Graphics class that emulates the tortoise in the Logo programming language
 *
 * The turtle starts at the center of the window, looking up with a black color and pen down
 *
 * Basic port implementation by Pierre Roduit, rewritten for use with [[FunGraphics]]
 * by Pierre-André Mudry.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Turtle_graphics">Wikipedia
 *      description of Turtle Graphics</a>
 * @author <a href='mailto:pandre.mudry&#64;hevs.ch'> Pierre-Andre Mudry</a>
 * @version 2.01 Using [[FunGraphics]] as main class
 *
 * @constructor Creates a turtle window
 * @param width        Width of the window
 * @param height       Height of the window
 * @param windowName   Title of the window
 */
class TurtleGraphics(width: Int, height: Int, windowName: String) extends FunGraphics(width, height, windowName, true) {
  this.checkBorders = false
  private var x = fWidth / 2.0
  private var y = fHeight / 2.0
  private var pDown = true
  private var angle = -Math.PI / 2.0 // Current rotation

  /**
   * Creates a turtle window
   *
   * @param width  Width of the window
   * @param height Height of the window
   */
  def this(width: Int, height: Int) = {
    this(width, height, null)
  }

  /**
   * Rounds a Double value and converts it to Int
   * @param a the value to round
   * @return the rounded value
   */
  private def round(a: Double) = a.round.toInt

  /**
   * Start the writing
   */
  def penDown(): Unit = {
    pDown = true
    // Write the pixel corresponding to the position
    setPixel(round(x), round(y))
  }

  /**
   * Change the color of drawing
   *
   * @param color the new color
   */
  def changeColor(color: Color): Unit = {
    setColor(color)
  }

  /**
   * Stop the drawing
   */
  def penUp(): Unit = {
    pDown = false
  }

  /**
   * Go forward the specified distance (writing if the pen is down)
   *
   * @param distance
   * The distance to move
   */
  def forward(distance: Double): Unit = { // Compute new position
    val newX = x + Math.cos(angle) * distance
    val newY = y + Math.sin(angle) * distance
    // Write if the pen is down
    if (pDown) drawLine(round(x), round(y), round(newX), round(newY))
    x = newX
    y = newY
  }

  /**
   * Jump to a specific location (without drawing)
   *
   * @param x
   * X coordinate of the destination
   * @param y
   * Y coordinate of the destination
   */
  def jump(x: Int, y: Int): Unit = {
    this.x = x
    this.y = y
    // If the pen is down, draw a point at destination
    if (pDown) setPixel(x, y)
  }

  /**
   * Gets the turtle's current position
   * @return The location of the turtle
   */
  def getPosition(): Point = new Point(round(x), round(y))

  /**
   * Sets the width of the pen
   *
   * @param w the new width
   */
  def setWidth(w: Float): Unit = {
    g2d.setStroke(new BasicStroke(w))
  }

  /**
   * Gets the turtle's current angle
   * @return The current turtle angle (in degrees)
   *         Angle 0 is east (right). A positive angle is clockwise.
   */
  def getTurtleAngle(): Double = this.angle * 180.0 / Math.PI

  /**
   * Turn the direction of writing with by specified angle
   * A positive angle is clockwise.
   *
   * @param angle specified angle in degrees
   */
  def turn(angle: Double): Unit = {
    this.angle += angle * Math.PI / 180
  }

  /**
   * Set the direction of writing to the specified angle.
   * Angle 0 is east (right). A positive angle is clockwise.
   *
   * @param angle
   * specified angle in degrees
   */
  def setAngle(angle: Double): Unit = {
    this.angle = angle * Math.PI / 180
  }

  /**
   * Turn the direction of writing by the specified angle.
   * A positive angle is clockwise.
   *
   * @param angle
   * specified angle in radians
   */
  def turnRad(angle: Double): Unit = {
    this.angle += angle
  }

  /**
   * Set the direction of writing to the specified angle.
   * Angle 0 is east (right). A positive angle is clockwise.
   *
   * @param angle
   * specified angle in radians
   */
  def setAngleRad(angle: Double): Unit = {
    this.angle = angle
  }

  /**
   * Get the turtle angle.
   * Angle 0 is east (right). A positive angle is clockwise.
   *
   * @return The current turtle angle in radians
   */
  def getTurtleAngleRad(): Double = this.angle

  /**
   * Adds a [[MouseMotionListener]] to the window to react on mouse movements
   * @param mouseMotionListener the [[MouseMotionListener]]
   */
  def setMouseMotionManager(mouseMotionListener: MouseMotionListener): Unit = {
    this.mainFrame.addMouseMotionListener(mouseMotionListener)
  }
}