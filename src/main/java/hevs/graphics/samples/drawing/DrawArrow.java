package hevs.graphics.samples.drawing;

import hevs.graphics.FunGraphics;

import java.awt.Color;

/**
 * Class using FunGraphics class to draw a simple arrow
 * 
 * @since 2012
 * @author Pierre-Andre Mudry
 * @version 1.0
 * 
 */
public class DrawArrow {
	private static int startingPointX = 100;
	private static int startingPointY = 200;
	private static int headSize = 20;
	private static int shaftLength = 100;

	public static void main(String[] args) {

		// Create and sets-up the window
		FunGraphics display = new FunGraphics(400, 400);
		
		// Sets the paint color
		display.setColor(Color.blue);
		
		// Draw the arrow
		for (int i = 0; i < shaftLength; i++) {
			// Draw the shaft
			display.setPixel(startingPointX + i, startingPointY);

			// Compute the head position
			int headPosition = shaftLength - i;
			
			if (headPosition <= headSize) {
				display.setPixel(startingPointX + i, startingPointY + headPosition);
				display.setPixel(startingPointX + i, startingPointY - headPosition);
			}
		}
			
	}
}