package hevs.graphics.advanced.samples;

import hevs.graphics.advanced.ListGraphics;

import java.awt.Color;
import java.util.Random;

/**
 * Performance test application for {@link ListGraphics}
 * 
 * @author Pierre-Andre Mudry
 * @date 2012
 * @version 1.0
 */
public class ListGraphicsTester {
	ListGraphics g;

	Color[] COLORS = new Color[] { Color.red, Color.blue, Color.green, Color.black, Color.yellow, new Color(100, 100, 255), Color.cyan, Color.pink,
			Color.lightGray, Color.magenta, Color.orange, Color.darkGray };

	ListGraphicsTester() {
		g = new ListGraphics(300, 300, "ComplexTester", true);
		Random rrand = new Random(12);
		final int NCIRCLES = 100;

		DrawableCircle[] rects = new DrawableCircle[NCIRCLES];
		int[] directions = new int[NCIRCLES];

		/**
		 * Generate some objects to be drawn
		 * randomly
		 */
		for (int i = 0; i < NCIRCLES; i++) {
			int width = 30 + rrand.nextInt(20);
			rects[i] = new DrawableCircle(width, width, 50 + rrand.nextInt(200), 25 + rrand.nextInt(200), COLORS[rrand.nextInt(COLORS.length)]);
			g.addDrawableObject(rects[i]);
			directions[i] = rrand.nextInt(6) + 1;
			directions[i] = rrand.nextBoolean() ? directions[i] : -directions[i];
		}

		/**
		 * Drawing loop
		 */
		while (true) {
			for (int i = 0; i < NCIRCLES; i++) {
				DrawableCircle r = rects[i];

				if (r.x > 280 || r.x < 0) {
					directions[i] *= -1;
				}

				r.x += directions[i];
			}

			g.repaint();
			g.syncGameLogic(60);
		}
	}

	public static void main(String args[]) {
		new ListGraphicsTester();
	}
}
