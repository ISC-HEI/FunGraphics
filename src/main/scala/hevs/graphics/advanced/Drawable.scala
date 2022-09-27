package hevs.graphics.advanced

/**
 * Interface for objects that could be drawn within the a graphical application
 *
 * @version 1.0, January 2010
 * @author <a href='mailto:pandre.mudry&#64;hevs.ch'> Pierre-André Mudry</a>
 */
trait Drawable {
  def draw(g: ListGraphics): Unit
}
