package hevs.graphics.samples.animations;

import hevs.graphics.FunGraphics;

import java.awt.Color;

/**
 * Demonstrates how to use two-layered 
 * drawing in {@link FunGraphics} where there
 * is a background which is drawn once and another
 * layer that is used as the foreground.
 * 
 * @author Pierre-Andre Mudry, HES-SO Valais 2012
 * @version 1.0
 *
 */
public class TwoLayerAnimation {
	static Color[] COLORS = new Color[] { Color.red, Color.blue, Color.green, Color.white, Color.black, Color.yellow,
			new Color(100, 100, 255), Color.cyan, Color.pink, Color.lightGray, Color.magenta, Color.orange, Color.darkGray };
	
	static void gameloopSample() {
		FunGraphics fg = new FunGraphics(300, 300, "With thread", true);
		int step = 1;
		int direction = 2;
				
		/**
		 * Draw the background, only once
		 */
		fg.drawBackground();
			for(int j = 0; j < fg.getFrameHeight(); j++){
				fg.setColor(COLORS[j % COLORS.length]);
				fg.drawLine(0, j, fg.getFrameWidth(), j);
			}
		fg.drawForeground();
		
		/**
		 * We now only render the foreground (as before)
		 */
		while (true) {
			// Rendering (note the synchronized)
			synchronized (fg.frontBuffer) {					
				fg.clear();				
				fg.setColor(Color.red);
				fg.drawFilledOval(10 + step, 10 + step, 100, 100);
			}

			/**
			 * Pause for constant frame rate
			 **/  
			fg.syncGameLogic(50);

			// Logic
			step += direction;
			if (step > 100 || step <= 0) {
				direction *= -1;
			}
		}
	}

	public static void main(String[] args) {
		gameloopSample();
	}
}
