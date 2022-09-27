package hevs.graphics.advanced.samples

import hevs.graphics.advanced.Drawable
import hevs.graphics.advanced.ListGraphics
import java.awt.Color


class DrawableCircle private[samples](var width: Int, var height: Int, var x: Int, var y: Int, var c: Color) extends Drawable {
  override def draw(g: ListGraphics): Unit = {
    g.setColor(c)
    g.drawFilledCircle(x, y, width)
  }
}
