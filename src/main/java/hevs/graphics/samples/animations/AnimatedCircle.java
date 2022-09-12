package hevs.graphics.samples.animations;

import hevs.graphics.FunGraphics;

import java.awt.Color;

/**
 * Draws an animated circle with text, used as a sample
 * application for {@link FunGraphics}
 * 
 * @author Pierre-Andre Mudry
 */
public class AnimatedCircle {

	public static void main(String[] args) {
		FunGraphics display = new FunGraphics(800, 600, "Animate Circle");

		for (int i = 0; i < 200; i += 1) {
			synchronized (display.frontBuffer) {
				// Erase previous content
				display.clear(Color.white);

				// Display new frame
				display.setColor(Color.blue);
				display.drawString(100, 100 + i, "Hello");
				display.setColor(Color.yellow);
				display.drawFilledCircle(100 + i, 100 + i, 100 + i);
				display.setColor(Color.black);
				display.setPenWidth(4.0f);
				display.drawCircle(100 + i, 100 + i, 100 + i);
			}
			// Insert a small timed pause so it doesn't go too fast
			display.syncGameLogic(60);
		}
	}
}
