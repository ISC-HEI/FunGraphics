package hevs.graphics.advanced.samples;

import hevs.graphics.advanced.Drawable;
import hevs.graphics.advanced.ListGraphics;
import hevs.graphics.utils.GraphicsBitmap;

import java.awt.Color;
import java.util.Random;

/**
 * Performance testers for {@link ListGraphics}
 * 
 * @author Pierre-Andre Mudry
 * @date 2012
 * @version 1.0
 */
class ImageBackground implements Drawable {
	GraphicsBitmap bmp = new GraphicsBitmap("/images/mandrill.jpg");

	double scale = 0.3, rotation = 0;
	double direction = 0.005;

	@Override
	public void draw(ListGraphics g) {
		g.drawTransformedPicture(250, 250, rotation, scale, bmp);
	}
}

public class ComplexTesterWithImage {
	ListGraphics g;
	Color[] COLORS = new Color[] { Color.red, Color.blue, Color.green, Color.black, Color.yellow,
			new Color(100, 100, 255), Color.cyan, Color.pink, Color.lightGray, Color.magenta, Color.orange,
			Color.darkGray };

	ComplexTesterWithImage() {
		g = new ListGraphics(300, 300, "ComplexTester", true);
		Random rrand = new Random(12);
		final int NRECT = 250;

		DrawableCircle[] rects = new DrawableCircle[NRECT];
		int[] directions = new int[NRECT];

		/**
		 * Generate some objects randomly
		 */
		for (int i = 0; i < NRECT; i++) {
			int width = 30 + rrand.nextInt(20);
			rects[i] = new DrawableCircle(width, width, 50 + rrand.nextInt(200), 25 + rrand.nextInt(200),
					COLORS[rrand.nextInt(COLORS.length)]);
			g.addDrawableObject(rects[i]);
			directions[i] = rrand.nextInt(6) + 1;
			directions[i] = rrand.nextBoolean() ? directions[i] : -directions[i];
		}

		/**
		 * Add the bitmap
		 */
		ImageBackground bg = new ImageBackground();
		g.addDrawableObject(bg);

		/**
		 * Drawing loop
		 */
		while (true) {
			for (int i = 0; i < NRECT; i++) {
				DrawableCircle r = rects[i];

				if (r.x > 280 || r.x < 0) {
					directions[i] *= -1;
				}

				r.x += directions[i];
			}

			bg.direction = bg.scale > 0.5 || bg.scale < 0.2 ? bg.direction * -1 : bg.direction;
			bg.scale += bg.direction;

			g.repaint();
			g.syncGameLogic(30);			
		}
	}

	public static void main(String args[]) {
		new ComplexTesterWithImage();
	}
}
