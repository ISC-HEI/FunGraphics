package hevs.graphics.advanced

import hevs.graphics.FunGraphics

import java.awt.Color
import java.awt.event.KeyListener
import java.awt.event.MouseListener
import java.util.{Collections, List}
import java.util
import java.util.concurrent.CopyOnWriteArrayList
import javax.swing.JFrame


/**
 * Extension of {@link hevs.graphics.FunGraphics} that manages a list of {@link hevs.graphics.advanced.Drawable}
 * objects that are displayed using the {@link # repaint ( )} method.
 */
class ListGraphics(override val width: Int, override val height: Int, override val title: String, val highQuality: Boolean) extends FunGraphics(width, height, title, highQuality) {
  private[advanced] var mouseListener:MouseListener = null
  private[advanced] val backgroundColor = Color.white

  def this(width: Int, height: Int, title: String) = {
    this(width, height, title, true)
  }

  private val objectsToBeDrawn:util.List[Drawable] = Collections.synchronizedList(new CopyOnWriteArrayList[Drawable])

  def setMouseListener(mouseListener: MouseListener): Unit = {
    this.mouseListener = mouseListener
    mainFrame.addMouseListener(mouseListener)
  }

  def setBackgroundColor(c: Color): Unit = {
    g2d.setBackground(c)
  }

  /**
   * register a new keyboard listener to main window
   *
   * @param listener
   */
  def registerKeyListener(listener: KeyListener): Unit = {
    mainFrame.addKeyListener(listener)
  }

  /**
   * Add a new object that will be drawn
   *
   * @param d
   * The object to draw
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
   * Remove an object to the list
   *
   * @param d
   * The object to remove
   */
  def removeDrawableObjects(d: Drawable): Unit = {
    objectsToBeDrawn synchronized objectsToBeDrawn.remove(d)

  }

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
