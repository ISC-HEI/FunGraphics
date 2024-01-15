package hevs.graphics.advanced

import hevs.graphics.FunGraphics

import java.awt.Color
import java.awt.event.{KeyListener, MouseListener}
import java.util
import java.util.Collections
import java.util.concurrent.CopyOnWriteArrayList
import javax.swing.JFrame

/**
 * Extension of [[hevs.graphics.FunGraphics]] that manages a list of [[hevs.graphics.advanced.Drawable]]
 * objects that are displayed using the [[repaint]] method.
 *
 * @param width        Width of the window
 * @param height       Height of the window
 * @param title        Title of the window
 * @param highQuality  Use high quality rendering
 */
class ListGraphics(override val width: Int, override val height: Int, override val title: String, val highQuality: Boolean) extends FunGraphics(width, height, title, highQuality) {
  private[advanced] var mouseListener:MouseListener = null
  private[advanced] val backgroundColor = Color.white

  /**
   * Creates a graphic window to draw onto.
   *
   * @param width        Width of the display window
   * @param height       Height of the display window
   * @param title        Title of the display window
   */
  def this(width: Int, height: Int, title: String) = {
    this(width, height, title, true)
  }

  private val objectsToBeDrawn:util.List[Drawable] = Collections.synchronizedList(new CopyOnWriteArrayList[Drawable])

  /**
   * Sets the [[MouseListener]] to the window to react on mouse events
   *
   * @param mouseListener The [[MouseListener]]
   */
  def setMouseListener(mouseListener: MouseListener): Unit = {
    this.mouseListener = mouseListener
    mainFrame.addMouseListener(mouseListener)
  }

  /**
   * Sets the background color used when clearing the window
   * @param c the new background color
   */
  def setBackgroundColor(c: Color): Unit = {
    g2d.setBackground(c)
  }

  /**
   * Register a new keyboard listener to main window
   *
   * @param listener the [[KeyListener]]
   */
  def registerKeyListener(listener: KeyListener): Unit = {
    mainFrame.addKeyListener(listener)
  }

  /**
   * Adds a new object that will be drawn
   *
   * @param d the object to draw
   */
  def addDrawableObject(d: Drawable): Unit = {
    objectsToBeDrawn.add(d)
  }

  /**
   * Erases all drawable objects in the list
   */
  def removeAllDrawableObjets(): Unit = {
    objectsToBeDrawn.clear()
  }

  /**
   * Removes an object from the list
   *
   * @param d the object to remove
   */
  def removeDrawableObjects(d: Drawable): Unit = {
    objectsToBeDrawn synchronized objectsToBeDrawn.remove(d)
  }

  /**
   * Gets the main [[JFrame]]
   * @return the [[JFrame]] of the window
   */
  def getDisplayFrame: JFrame = this.mainFrame

  /**
   * Clears the screen and repaints everything
   */
  def repaint(): Unit = {
    /**
     * List has to be synchronized to enable access during drawing
     * (concurrent access)
     */
    frontBuffer synchronized objectsToBeDrawn synchronized clear()
    objectsToBeDrawn.forEach(
      _.draw(this)
    )
  }
}
