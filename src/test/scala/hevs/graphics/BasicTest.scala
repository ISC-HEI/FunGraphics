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

  @Test
  def testClick(): Unit = {
    assumeFalse(headless)
    val w = 200
    val h = 150
    val f = FunGraphics(w, h, 0, 0, "hello", true)
    var clicked = false
    f.addMouseListener(new MouseAdapter() {
      override def mouseClicked(x: MouseEvent) : Unit = {
        println(f"mouseClicked x:${x.getX} y:${x.getY}")
        clicked = true
      }
    })

    val x = new FrameFixture(f.mainFrame)
    x.robot().click(f.mainFrame.getContentPane, new Point(w / 2, h / 2))
    x.robot().click(f.mainFrame.getContentPane, new Point(0, 0))

    println(s"clicked:$clicked")
    assertTrue(clicked)
  }
}