package hevs.graphics.advanced.samples

import hevs.graphics.advanced.{Drawable, ListGraphics}

import java.awt.Color

/**
 * A simple class representing a drawable circle
 * @param width the width of the circle (diameter)
 * @param height the height of the circle (unused)
 * @param x the X position of the top-left corner
 * @param y the Y position of the top-left corner
 * @param c the color of the circle
 */
class DrawableCircle private[samples](var width: Int, var height: Int, var x: Int, var y: Int, var c: Color) extends Drawable {
  override def draw(g: ListGraphics): Unit = {
    g.setColor(c)
    g.drawFilledCircle(x, y, width)
  }
}
