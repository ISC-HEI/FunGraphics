package hevs.graphics.samples.animations;

import hevs.graphics.FunGraphics;

import java.awt.Color;

/**
 * A sample game loop for {@link FunGraphics} using a rendering thread. In this
 * mode, two different threads are present, the main one and another thread is
 * created for rendering. The display is smoother in this mode because of the
 * decoupling but explicit synchronization has to be done.
 * 
 * @author Pierre-Andre Mudry
 * @date March 2012
 */
public class GameLoopSynchronized {

	static void gameloopSample() {
		FunGraphics fg = new FunGraphics(300, 300, "With thread", true);
		int i = 1;
		int direction = 2;

		while (true) {
			// Rendering (note the synchronized)
			synchronized (fg.frontBuffer) {
				fg.clear(Color.white);
				fg.setColor(Color.red);
				fg.drawFilledOval(10 + i, 10 + i, 100, 100);
				fg.setColor(Color.yellow);
				fg.drawFillRect(50 + i, 50 - i, 100 + i, 100 + i);
			}

			/**
			 * Pause for constant frame rate
			 * Note how with the rendering thread the method render() should not
			 * be called explicitly
			 **/
			fg.syncGameLogic(100);

			// Logic
			i += direction;
			if (i > 100 || i <= 0) {
				direction *= -1;
			}
		}
	}

	public static void main(String[] args) {
		gameloopSample();
	}
}
