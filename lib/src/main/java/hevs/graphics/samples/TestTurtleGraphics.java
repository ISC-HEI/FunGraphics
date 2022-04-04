package hevs.graphics.samples;

import hevs.graphics.TurtleGraphics;

/**
 * Sample for {@link TurtleGraphics} class
 * @author Pierre-André Mudry
 *
 */
public class TestTurtleGraphics {
	public static void main (String[] args){			
		TurtleGraphics t = new TurtleGraphics(500, 500, "Test of Turtle Graphics");
		t.penDown();	
		t.forward(100);		
		
		for(int i = 0; i < 180; i++){
			t.forward(10);
			t.turn((5.0+i/10.0)%180);
		}
	}
	
}
