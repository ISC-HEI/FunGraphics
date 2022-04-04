package hevs.graphics.interfaces;

import hevs.graphics.FunGraphics;
import hevs.graphics.utils.GraphicsBitmap;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;

/**
 * An interface that every graphic application
 * should have (common between {@link FunGraphics} and Gdx2d).
 * 
 * @author Pierre-André Mudry
 * @version 1.0 
 */
public interface Graphics {

	/**
	 * Method which cleans up the display. Everything becomes the background
	 * again
	 */
	public abstract void clear();

	/**
	 * Method which cleans up the display. Everything becomes the background
	 * again.
	 */
	public abstract void clear(Color c);

	/**
	 * Set the color of the future drawings
	 * 
	 * @param c
	 *            Selected color for drawing
	 */
	public abstract void setColor(Color c);

	/**
	 * Draw the selected pixel with the color selected with setColor.
	 * 
	 * @param x
	 *            X coordinate of the pixel
	 * @param y
	 *            Y coordinate of the pixel
	 */
	public abstract void setPixel(int x, int y);

	/**
	 * Draws a pixel with a given color. Does not change the current color.
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @param c
	 *            Color to use for this pixel (this pixel only, see {@link #setColor(Color)}
	 */
	public abstract void setPixel(int x, int y, Color c);

	/**
	 * Draws a pixel with a given color. Does not change the current color
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @param c
	 *            Color to use (RGB coded)
	 */
	public abstract void setPixel(int x, int y, int c);

	/**
	 * Sets the width of the pen
	 * 
	 * @param width
	 *            The new width of the pen
	 */
	public abstract void setPenWidth(float width);

	/**
	 * Draw a line from P1 to P2 in the color selected with setColor.
	 * 
	 * @param p1x
	 *            X coordinate of P1
	 * @param p1y
	 *            Y coordinate of P1
	 * @param p2x
	 *            X coordinate of P2
	 * @param p2y
	 *            Y coordinate of P2
	 */
	public abstract void drawLine(int p1x, int p1y, int p2x, int p2y);

	/**
	 * Draw a filled polygon
	 * 
	 * @param p
	 * @param c
	 */
	public abstract void drawFilledPolygon(Polygon p, Color c);

	/**
	 * Draw an empty rectangle in the color selected with setColor().
	 * 
	 * @param posX
	 *            X coordinate of the top left corner of the rectangle
	 * @param posY
	 *            Y coordinate of the top left corner of the rectangle
	 * @param width
	 *            Width of the rectangle
	 * @param height
	 *            Height of the rectangle
	 */
	public abstract void drawRect(int posX, int posY, int width, int height);

	/**
	 * Draw a filled rectangle in the color selected with setColor.
	 * 
	 * @param posX
	 *            X coordinate of the top left corner of the rectangle
	 * @param posY
	 *            Y coordinate of the top left corner of the rectangle
	 * @param width
	 *            Width of the rectangle
	 * @param height
	 *            Height of the rectangle
	 */
	public abstract void drawFillRect(int posX, int posY, int width, int height);

	/**
	 * @param rect
	 *            rectangle to draw
	 */
	public abstract void drawRect(Rectangle rect);

	/**
	 * Draw a filled rectangle in the color selected with setColor.
	 * 
	 * @param rect
	 *            rectangle to draw
	 */
	public abstract void drawFillRect(Rectangle rect);

	/**
	 * Draws a circle starting from <code>(Top left X, Top left Y)</code>
	 * 
	 * @param posX
	 *            X top-left position of the circle
	 * @param posY
	 *            Y top-left position of the circle
	 * @param f
	 *            Diameter of the drawn circle
	 */
	public abstract void drawCircle(int posX, int posY, int f);

	/**
	 * Draws a circle starting from <code>(Top left X, Top left Y)</code>
	 * 
	 * @param posX
	 *            X top-left position of the circle
	 * @param posY
	 *            Y top-left position of the circle
	 * @param radius
	 *            Radius of the drawn circle
	 */
	public abstract void drawFilledCircle(int posX, int posY, int radius);

	/**
	 * Draws an oval starting from <code>(Top left X, Top left Y)</code>
	 * 
	 * @param posX
	 *            X top-left position of the circle
	 * @param posY
	 *            Y top-left position of the circle
	 * @param width
	 *            Width of the drawn oval
	 * @param height
	 *            Height of the drawn oval
	 */
	public abstract void drawFilledOval(int posX, int posY, int width, int height);

	/**
	 * Draws a string at a given location. Note that the boundaries are not
	 * checked and text may be painted outside the window
	 * 
	 * @param posX
	 *            X position of string
	 * @param posY
	 *            Y position of string
	 * @param str
	 *            the string to write
	 */
	public abstract void drawString(int posX, int posY, String str);

	/**
	 * Write the given string at <code>posX, posY</code>
	 * 
	 * @param posX
	 *            Position x of the string
	 * @param posY
	 *            Position y of the string
	 * @param str
	 *            The string to be drawn
	 * @param color
	 *            The color of the string
	 * @param size
	 *            The size of the font
	 */
	public abstract void drawString(int posX, int posY, String str, Color color, int size);

	/**
	 * Draws a text with a shadow
	 * 
	 * @param posX
	 * @param posY
	 * @param str
	 * @param color
	 * @param size
	 */
	public abstract void drawFancyString(int posX, int posY, String str, Color color, int size);

	/**
	 * Draw a centered picture from a file (gif, jpg, png) to <code>(posX, posY)</code>
	 * 
	 * @param posX
	 *            X position of the image
	 * @param posY
	 *            Y position of the image
	 * @param filename
	 *            path of the image file
	 */
	public abstract void drawPicture(int posX, int posY, GraphicsBitmap bitmap);

	/**
	 * Draw a centered picture from a file (gif, jpg, png) to <code>(posX, posY)</code>. Warning,
	 * very slow because the image has to be
	 * reloaded
	 * 
	 * @param posX
	 *            X position of the image
	 * @param posY
	 *            Y position of the image
	 * @param angle
	 *            The rotation angle of the image to be drawn
	 * @param imageName
	 *            path of the image file
	 */
	public abstract void drawTransformedPicture(int posX, int posY, double angle, double scale, String imageName);

	/**
	 * Draw a centered picture from a file (gif, jpg, png) to <code>(posX, posY)</code>
	 * 
	 * @param posX
	 *            X position of the image
	 * @param posY
	 *            Y position of the image
	 * @param angle
	 *            The rotation angle of the image to be drawn
	 * @param bitmap
	 *            A {@link #SimpleGraphicsBitmap()} bitmap
	 */
	public abstract void drawTransformedPicture(int posX, int posY, double angle, double scale, GraphicsBitmap bitmap);

	/**
	 * Draw a mirrored centered picture from a file (gif, jpg, png) to <code>(posX, posY)</code>
	 * 
	 * @param posX
	 *            X position of the image
	 * @param posY
	 *            Y position of the image
	 * @param angle
	 *            The rotation angle of the image to be drawn
	 * @param bitmap
	 *            A {@link #SimpleGraphicsBitmap()} bitmap
	 */
	public abstract void drawMirroredPicture(int posX, int posY, double angle, GraphicsBitmap bitmap);

	/**
	 * Getters and setters
	 */
	public abstract int getFrameWidth();

	public abstract int getFrameHeight();

}