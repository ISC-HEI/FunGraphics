package hevs.graphics.advanced;

import hevs.graphics.FunGraphics;

import java.awt.Color;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;

/**
 * Extension of {@link hevs.graphics.FunGraphics} that manages a list of {@link hevs.graphics.advanced.Drawable}
 * objects that are displayed using the {@link #repaint()} method.
 */
public class ListGraphics extends FunGraphics {
	MouseListener mouseListener;
	Color backgroundColor = Color.white;

	public ListGraphics(int width, int height, String title) {
		this(width, height, title, true);
	}

	public ListGraphics(int width, int height, String title, boolean highQuality) {
		super(width, height, title, highQuality);
	}

	private List<Drawable> objectsToBeDrawn = Collections.synchronizedList(new CopyOnWriteArrayList<Drawable>());

	public void setMouseListener(MouseListener mouseListener) {
		this.mouseListener = mouseListener;
		mainFrame.addMouseListener(mouseListener);
	}

	public void setBackgroundColor(Color c) {
		g2d.setBackground(c);
	}

	/**
	 * register a new keyboard listener to main window
	 * 
	 * @param listener
	 */
	public void registerKeyListener(KeyListener listener) {
		mainFrame.addKeyListener(listener);
	}

	/**
	 * Add a new object that will be drawn
	 * 
	 * @param d
	 *            The object to draw
	 */
	public void addDrawableObject(Drawable d) {

		objectsToBeDrawn.add(d);
	}

	/**
	 * Erases all drawable objects in the list
	 */
	public void removeAllDrawableObjets() {

		objectsToBeDrawn.clear();
	}

	/**
	 * Remove an object to the list
	 * 
	 * @param d
	 *            The object to remove
	 */
	public void removeDrawableObjects(Drawable d) {
		synchronized (objectsToBeDrawn) {
			objectsToBeDrawn.remove(d);
		}
	}

	public JFrame getDisplayFrame() {
		return this.mainFrame;
	}

	/**
	 * Clears the screen and repaints everything
	 */
	public void repaint() {
		/**
		 * List has to be synchronized to enable access during drawing
		 * (concurrent access)
		 */
		synchronized (frontBuffer) {
			synchronized (objectsToBeDrawn) {
				clear();
				for (Drawable d : objectsToBeDrawn) {
					d.draw(this);
				}
			}
		}
	}

}
