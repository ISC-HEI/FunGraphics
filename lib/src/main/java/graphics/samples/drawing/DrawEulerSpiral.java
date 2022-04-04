package hevs.graphics.samples.drawing;

import hevs.graphics.FunGraphics;

/**
 * Draws an Euler spiral using {@link FunGraphics}
 * 
 * @author Pierre-Andre Mudry
 * @author Pierre Roduit (pierre.roduit@hevs.ch)
 * @version 2.0 - March 2012
 * 
 */
public class DrawEulerSpiral {
	private static int graphicsWidth = 640;
	private static int graphicsHeight = 480;
	private static int startingPointX = graphicsWidth / 2;
	private static int startingPointY = graphicsHeight / 2;
	private static int stepsPerTurn = 150;
	private static int numberTurns = 10;
	private static int gapPerTurn = 20;

	public static void main(String[] args) {
		FunGraphics display = new FunGraphics(graphicsWidth, graphicsHeight);

		for (int i = 0; i < stepsPerTurn * numberTurns; i++) {
			double radius = (double) i / stepsPerTurn * gapPerTurn;
			int x = (int) Math.round(startingPointX + Math.cos((double) i / stepsPerTurn * 2.0 * Math.PI) * radius);
			int y = (int) Math.round(startingPointY + Math.sin((double) i / stepsPerTurn * 2.0 * Math.PI) * radius);
			display.setPixel(x, y);
		}		
	}
}