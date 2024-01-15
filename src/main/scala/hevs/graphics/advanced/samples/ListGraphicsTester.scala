package hevs.graphics.advanced.samples

import hevs.graphics.advanced.ListGraphics

import java.awt.Color
import java.util.Random

/**
 * Performance test application for [[ListGraphics]]
 *
 * @author Pierre-Andre Mudry
 * @version 1.0
 */
object ListGraphicsTester {
  /**
   * Simple tests with [[ListGraphics]]
   * @param args unused
   */
  def main(args: Array[String]): Unit = {
    new ListGraphicsTester
  }
}

class ListGraphicsTester private[samples]() {
  private val g = new ListGraphics(300, 300, "ComplexTester", true)
  val rrand = new Random(12)
  val NCIRCLES = 100
  val rects = new Array[DrawableCircle](NCIRCLES)
  val directions = new Array[Int](NCIRCLES)
  private val COLORS = Array[Color](Color.red, Color.blue, Color.green, Color.black, Color.yellow, new Color(100, 100, 255), Color.cyan, Color.pink, Color.lightGray, Color.magenta, Color.orange, Color.darkGray)
  /**
   * Generate some objects to be drawn
   * randomly
   */
  for (i <- 0 until NCIRCLES) {
    val width = 30 + rrand.nextInt(20)
    rects(i) = new DrawableCircle(width, width, 50 + rrand.nextInt(200), 25 + rrand.nextInt(200), COLORS(rrand.nextInt(COLORS.length)))
    g.addDrawableObject(rects(i))
    directions(i) = rrand.nextInt(6) + 1
    directions(i) = if (rrand.nextBoolean) directions(i)
    else -directions(i)
  }

  /**
   * Drawing loop
   */
  while ( {
    true
  }) {
    for (i <- 0 until NCIRCLES) {
      val r = rects(i)
      if (r.x > 280 || r.x < 0) directions(i) *= -1
      r.x += directions(i)
    }
    g.repaint()
    g.syncGameLogic(60)
  }

}
