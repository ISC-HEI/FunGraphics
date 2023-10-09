package hevs.graphics.advanced

/**
 * Interface for objects that could be drawn within the a graphical application
 *
 * @version 1.0
 * @author Pierre-André Mudry
 */
trait Drawable {
  def draw(g: ListGraphics): Unit
}
