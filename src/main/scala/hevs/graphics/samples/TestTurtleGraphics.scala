package hevs.graphics.samples

import hevs.graphics.TurtleGraphics

/**
 * Sample for [[TurtleGraphics]] class
 *
 * @author Pierre-André Mudry
 */
object TestTurtleGraphics extends App {
    val t = new TurtleGraphics(500, 500, "Test of Turtle Graphics")
    t.penDown()
    t.forward(100)
    for (i <- 0 until 180) {
      t.forward(10)
      t.turn((5.0 + i / 10.0) % 180)
    }
}
