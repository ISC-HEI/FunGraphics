package hevs.graphics.samples

import hevs.graphics.FunGraphics

import java.awt.{Color, Font}
import javax.swing.SwingConstants

/**
 * Sample for `drawString` and `drawFancyString` methods of [[FunGraphics]]
 *
 * @author Louis Heredero
 */
object TestDrawString extends App {
  val HALIGNMENTS: Array[Int] = Array(
    SwingConstants.LEFT,
    SwingConstants.CENTER,
    SwingConstants.RIGHT
  )
  val VALIGNMENTS: Array[Int] = Array(
    SwingConstants.TOP,
    SwingConstants.CENTER,
    SwingConstants.BOTTOM
  )
  val fg: FunGraphics = new FunGraphics(600, 600, "Test of drawString")

  for (y: Int <- 0 until 3) {
    for (x: Int <- 0 until 3) {
      val posX: Int = 25 + x * 100
      val posY: Int = 25 + y * 100

      fg.setColor(Color.BLACK)
      fg.drawString(posX, posY, "Test", halign = HALIGNMENTS(x), valign = VALIGNMENTS(y))
      fg.setColor(Color.RED)
      fg.drawFilledCircle(posX-2, posY-2, 2)
    }
  }

  fg.setColor(Color.BLACK)
  for (y: Int <- 0 until 3) {
    for (x: Int <- 0 until 3) {
      val posX: Int = 350 + x * 100
      val posY: Int = 25 + y * 100

      fg.drawFancyString(
        posX,
        posY,
        "Test",
        halign = SwingConstants.CENTER,
        valign = SwingConstants.CENTER,
        shadowX = (x-1)*2,
        shadowY = (y-1)*2,
        shadowThickness = 1,
        fontFamily = "Arial",
        fontStyle = Font.BOLD
      )
    }
  }


  for (y: Int <- 0 until 3) {
    for (x: Int <- 0 until 3) {
      val posX: Int = 25 + x * 100
      val posY: Int = 350 + y * 100

      fg.drawFancyString(
        posX,
        posY,
        "Test",
        halign = SwingConstants.CENTER,
        valign = SwingConstants.CENTER,
        shadowThickness = x+y*3,
        fontFamily = "Arial",
        fontStyle = Font.BOLD
      )
    }
  }


  for (y: Int <- 0 until 3) {
    for (x: Int <- 0 until 3) {
      val posX: Int = 350 + x * 100
      val posY: Int = 350 + y * 100

      fg.drawFancyString(
        posX,
        posY,
        "Test",
        color = Color.WHITE,
        halign = SwingConstants.CENTER,
        valign = SwingConstants.CENTER,
        outlineThickness = x+y*3,
        outlineColor = Color.BLACK,
        fontFamily = "Arial",
        fontStyle = Font.BOLD
      )
    }
  }
}
