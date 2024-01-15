package hevs.graphics

import org.assertj.swing.fixture.FrameFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assumptions.assumeFalse
import org.junit.jupiter.api.Test

import java.awt.{Color, GraphicsEnvironment, Point}
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class BasicTest {
  private val headless = GraphicsEnvironment.isHeadless

  @Test
  def testWindowSize(): Unit = {
    assumeFalse(headless)
    val w = 12
    val h = 20
    val f = FunGraphics(w, h)
    assertEquals(w, f.getFrameWidth())
    assertEquals(h, f.getFrameHeight())
  }

  @Test
  def testBasicDrawing(): Unit = {
    assumeFalse(headless)
    val w = 2
    val h = 3
    val f = FunGraphics(w, h)
    for (x <- 0 until f.getFrameWidth()) {
      for (y <- 0 until f.getFrameHeight()) {
        assertEquals(Color.WHITE, new Color(f.frontBuffer.getRGB(x,y)))
      }
    }
    f.setPixel(1,1, Color.BLUE)
    for (x <- 0 until f.getFrameWidth()) {
      for (y <- 0 until f.getFrameHeight()) {
        if (x == 1 && y == 1) {
          assertEquals(Color.BLUE, new Color(f.frontBuffer.getRGB(x, y)))
        } else {
          assertEquals(Color.WHITE, new Color(f.frontBuffer.getRGB(x, y)))
        }
      }
    }
  }

}
