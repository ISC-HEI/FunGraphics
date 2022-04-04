package hevs.graphics.samples.animations;

import hevs.graphics.FunGraphics;

import java.awt.Color;

public class BasicGameLoop {

	static void gameloopSample() {
		FunGraphics fg = new FunGraphics(300, 300);
		int i = 1;
		int direction = 1;

		while (true) {
			// Rendering
			fg.clear(Color.white);
			fg.setColor(Color.red);
			fg.drawFilledOval(10 + i, 10 + i, 100, 100);
			fg.setColor(Color.yellow);
			fg.drawFillRect(50 + i, 50 - i, 100 + i, 100 + i);
			
			// Pause for constant frame rate
			fg.syncGameLogic(120);

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
