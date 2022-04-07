package hevs.graphics

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.Test
import java.awt.GraphicsEnvironment

class BasicTest {
    var headless = GraphicsEnvironment.isHeadless()
    @Test
    fun testWindowSize() {
        Assumptions.assumeFalse(headless)
        val w = 12
        val h = 20
        val f = FunGraphics(w, h)
        Assertions.assertEquals(w, f.frameWidth)
        Assertions.assertEquals(h, f.frameHeight)
    }
}