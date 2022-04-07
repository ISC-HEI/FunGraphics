package hevs.graphics.samples.drawing;

import hevs.graphics.FunGraphics;
import hevs.graphics.utils.GraphicsBitmap;

import java.awt.Color;

public class DrawImage {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FunGraphics display = new FunGraphics(20,20);
		GraphicsBitmap bm = new GraphicsBitmap("/images/moire1.png");
		display.drawTransformedPicture(0, 0, Math.PI / 2, 0.5, bm);
		display.setColor(Color.red);
		display.drawFilledCircle(200, 200, 10);
		System.out.println(new Color(display.frontBuffer.getRGB(0, 0)));
		
	}

}
