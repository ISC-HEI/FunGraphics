package hevs.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseMotionListener;

/**
 * Graphics class that emulates the tortoise in the Logo programming language
 * The turtle starts looking up with a black color, pen up
 * 
 * Basic port implementation by Pierre Roduit, rewritten for use with {@link FunGraphics}
 * by Pierre-André Mudry.
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Turtle_graphics">Wikipedia
 *      description of Turtle Graphics</a>
 * @author <a href='mailto:pandre.mudry&#64;hevs.ch'> Pierre-Andre Mudry</a>
 * @version 2.01 Using {@link FunGraphics} as main class
 */
public class TurtleGraphics extends FunGraphics {

	private int x = fWidth / 2;
	private int y = fHeight / 2;
	private boolean penDown = false;
	private double angle = -Math.PI / 2.0; // Current rotation
	private Color color = Color.black;

	public TurtleGraphics(int width, int height, String windowName) {
		super(width, height, windowName, true);
		this.checkBorders = false;
	}

	public TurtleGraphics(int width, int height) {
		this(width, height, null);
	}

	/**
	 * Start the writing
	 */
	public void penDown() {
		penDown = true;
		// Write the pixel corresponding to the position
		Color oldColor = g2d.getColor();
		setColor(color);
		setPixel(x, y);
		setColor(oldColor);
	}

	/**
	 * Change the color of drawing
	 * 
	 * @param color
	 */
	public void changeColor(Color color) {
		this.color = color;
	}

	/**
	 * Stop the drawing
	 */
	public void penUp() {
		penDown = false;
	}

	/**
	 * Go forward the specified distance (writing if the pen is down)
	 * 
	 * @param distance
	 *            The distance to move
	 */
	public void forward(double distance) {
		// Compute new position
		int newX = x + (int) Math.round(Math.cos(angle) * distance);
		int newY = y + (int) Math.round(Math.sin(angle) * distance);

		// Write if the pen is down
		if (penDown) {
			Color oldColor = g2d.getColor();
			setColor(color);
			drawLine(x, y, newX, newY);
			setColor(oldColor);
		}
		x = newX;
		y = newY;
	}

	/**
	 * Jump to a specific location (without drawing)
	 * 
	 * @param x
	 *            X coordinate of the destination
	 * @param y
	 *            Y coordinate of the destination
	 */
	public void jump(int x, int y) {
		this.x = x;
		this.y = y;

		// If the pen is down, draw a point at destination
		if (penDown) {
			Color oldColor = g2d.getColor();
			setColor(color);
			setPixel(x, y);
			setColor(oldColor);
		}
	}

	/**
	 * @return The location of the turtle
	 */
	public Point getPosition() {
		return new Point(x, y);
	}

	/**
	 * Sets the width of the pen
	 * 
	 * @param w
	 */
	public void setWidth(float w) {
		g2d.setStroke(new BasicStroke(w));
	}

	/**
	 * @return The current turtle angle (in degrees)
	 */
	public double getTurtleAngle() {
		return (this.angle * 180.0 / Math.PI);
	}

	/**
	 * Turn the direction of writing with the specified angle
	 * 
	 * @param angle
	 *            specified angle in degrees
	 */
	public void turn(double angle) {
		this.angle += angle * Math.PI / 180;
	}

	/**
	 * Set the direction of writing to the specified angle
	 * 
	 * @param angle
	 *            specified angle in degrees
	 */
	public void setAngle(double angle) {
		this.angle = angle * Math.PI / 180;
	}

	/**
	 * Turn the direction of writing with the specified angle
	 * 
	 * @param angle
	 *            specified angle in radians
	 */
	public void turnRad(double angle) {
		this.angle += angle;
	}

	/**
	 * Set the direction of writing to the specified angle
	 * 
	 * @param angle
	 *            specified angle in radians
	 */
	public void setAngleRad(double angle) {
		this.angle = angle;
	}

	/** 
	 * @return The current turtle angle in radians
	 */
	public double getTurtleAngleRad() {
		return this.angle;
	}

	public void setMouseMotionManager(MouseMotionListener mouseMotionListener) {
		this.mainFrame.addMouseMotionListener(mouseMotionListener);
	}

}