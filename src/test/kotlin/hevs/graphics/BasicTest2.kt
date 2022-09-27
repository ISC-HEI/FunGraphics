package hevs.graphics

import org.assertj.swing.fixture.FrameFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assumptions.assumeFalse
import org.junit.jupiter.api.Test
import java.awt.GraphicsEnvironment
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class BasicTest {
    private val headless = GraphicsEnvironment.isHeadless()

    @Test
    fun testWindowSize() {
        assumeFalse(headless)
        val w = 12
        val h = 20
        val f = FunGraphics(w, h)
        assertEquals(w, f.frameWidth)
        assertEquals(h, f.frameHeight)
    }

    @Test
    fun testClick() {
        assumeFalse(headless)
        val w = 200
        val h = 150
        val f = FunGraphics(w, h, 0, 0, "hello", true)
        var clicked = false
        f.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(x: MouseEvent?) {
                println("mouseClicked x:${x?.x} y:${x?.y}")
                clicked = true
            }
        })


        val x = FrameFixture(f.mainFrame())
        x.robot().click(f.mainFrame().contentPane, Point(w / 2, h / 2))
        x.robot().click(f.mainFrame().contentPane, Point(0, 0))

        println("clicked:$clicked")
        assertTrue(clicked)
    }
}