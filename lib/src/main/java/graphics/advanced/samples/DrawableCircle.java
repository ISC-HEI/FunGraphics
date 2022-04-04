package hevs.graphics.advanced.samples;

import hevs.graphics.advanced.Drawable;
import hevs.graphics.advanced.ListGraphics;

import java.awt.Color;

public class DrawableCircle implements Drawable {

	int width, height;
	int x, y;
	Color c;
	
	DrawableCircle(int w, int h, int x, int y, Color c){
		this.width = w;
		this.height = h;
		this.x = x;
		this.y = y;
		this.c = c;
	}
		
	@Override
	public void draw(ListGraphics g) {
		g.setColor(c);
		g.drawFilledCircle(x, y, width);
	}
}
