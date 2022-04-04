package hevs.graphics.samples.drawing;

import hevs.graphics.FunGraphics;

/**
 * This class, which uses the {@link FunGraphics} class, draw a logarithmic spiral.
 * 
 * @author Pierre Roduit (pierre.roduit@hevs.ch)
 * @version 1.0
 * @date October 6th 2009
 *
 */
public class DrawLogarithmicSpiral {
	private static int graphicsWidth=640;
	private static int graphicsHeight=480;
	private static int startingPointX=graphicsWidth/2;
	private static int startingPointY=graphicsHeight/2;
	private static int stepsPerTurn=3600;
	private static int numberTurns=10;
	private static int gapPerTurn=2;
	private static double exponentialParameter=0.45;
	
	public static void main(String[] args) {
		FunGraphics display = new FunGraphics(graphicsWidth,graphicsHeight);
		for (int i=0;i<stepsPerTurn*numberTurns;i++)
		{
			double radius=(double)Math.exp(exponentialParameter*i/stepsPerTurn)*gapPerTurn;
			int x=(int)Math.round(startingPointX+Math.cos((double)i/stepsPerTurn*2.0*Math.PI)*radius);
			int y=(int)Math.round(startingPointY+Math.sin((double)i/stepsPerTurn*2.0*Math.PI)*radius);
			display.setPixel(x,y);
		}
	}
}