package hevs.graphics.samples.drawing;

import hevs.graphics.FunGraphics;

import java.awt.Color;

/**
 * Draws a simple circle on the screen * @author Pierre-André Mudry
 */
public class SimpleShapes {
    public static void main(String[] args) {
        // Instantiate a new window
        FunGraphics s = new FunGraphics(300, 300, "Test drawing simple shapes");
        // Draw simple shapes
        s.setColor(Color.black);
        s.drawCircle(100, 100, 10);
        s.setColor(Color.blue);
        s.drawFillRect(20, 20, 10, 10);
    }
}