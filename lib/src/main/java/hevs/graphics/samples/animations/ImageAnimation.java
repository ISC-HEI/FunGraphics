package hevs.graphics.samples.animations;

import java.awt.Color;

import hevs.graphics.FunGraphics;
import hevs.graphics.utils.GraphicsBitmap;

public class ImageAnimation {

	static void gameloopSample() {
		FunGraphics fg = new FunGraphics(300, 300, "With thread", true);
		GraphicsBitmap bm = new GraphicsBitmap("/mandrill.jpg");

		double angle = 0.1;
		double scale = 0.25;
		double offset = 0.01;

		while (true) {
			// Rendering (note the synchronized)
			synchronized (fg.frontBuffer) {
				fg.clear(Color.white);
				int x = fg.getFrameWidth()/2;
				int y = fg.getFrameHeight()/2-25;
				
				// Coordinates X and Y are where to draw the center of the image
				fg.drawTransformedPicture(x,y, angle, scale, bm);
			}

			/**
			 * Pause for constant frame rate Note how with the rendering thread the method
			 * render() should not be called explicitly
			 **/
			fg.syncGameLogic(60);

			// Logic
			angle += offset;
			if (angle > Math.PI / 4 || angle <= -Math.PI / 4) {
				offset *= -1;
			}
		}
	}

	public static void main(String[] args) {
		gameloopSample();
	}
}
