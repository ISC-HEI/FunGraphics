package hevs.graphics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.*;

public class BasicTest {
    boolean headless = GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadless();
    @Test
    public void testWindowSize() {
        assumeFalse(headless);
        System.out.println("no headless");
        final int w = 12;
        final int h = 20;
        FunGraphics f = new FunGraphics(w, h);

        assertEquals(w, f.getFrameWidth());
        assertEquals(h, f.getFrameHeight());
    }
}
