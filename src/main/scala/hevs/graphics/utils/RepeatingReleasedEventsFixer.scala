package hevs.graphics.utils


import java.awt.{AWTEvent, Component, EventQueue, Toolkit}
import java.awt.event.{AWTEventListener, ActionEvent, ActionListener, KeyEvent}
import java.util
import javax.swing.Timer


/**
 * Fixes the discrepancies between keyboard handling under Windows and linux
 * for repeated keys. Taken from [[http://tech.stolsvik.com/2010/05/linux-java-repeats-released-keyevents.html]]
 *
 * @author Pierre-André Mudry
 */
object RepeatingReleasedEventsFixer {
  /**
   * Marker interface that denotes that the {@link KeyEvent} in question is reposted from some
   * {@link AWTEventListener}, including this. It denotes that the event shall not be "hack processed" by this class
   * again. (The problem is that it is not possible to state "inject this event from this point in the pipeline" - one
   * have to inject it to the event queue directly, thus it will come through this {@link AWTEventListener} too.
   */
  trait Reposted { // marker
  }

  /**
   * Dead simple extension of {@link KeyEvent} that implements {@link Reposted}.
   */
  class RepostedKeyEvent(@SuppressWarnings(Array("hiding")) source: Component, @SuppressWarnings(Array("hiding")) id: Int, when: Long, modifiers: Int, keyCode: Int, keyChar: Char, keyLocation: Int) extends KeyEvent(source, id, when, modifiers, keyCode, keyChar, keyLocation) with RepeatingReleasedEventsFixer.Reposted {
  }

  /**
   * Asserts that the current thread is the event dispatching thread
   * @throws AssertionError the current thread is not the EDT
   * @return `true` if the current thread is the EDT
   */
  private def assertEDT: Boolean = {
    if (!EventQueue.isDispatchThread) throw new AssertionError("Not EDT, but [" + Thread.currentThread + "].")
    true
  }
}

class RepeatingReleasedEventsFixer extends AWTEventListener {
  final private val _map = new util.HashMap[Integer, RepeatingReleasedEventsFixer#ReleasedAction]

  /**
   * Installs the key event listener
   */
  def install(): Unit = {
    Toolkit.getDefaultToolkit.addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK)
  }

  /**
   * Removes the key event listener
   */
  def remove(): Unit = {
    Toolkit.getDefaultToolkit.removeAWTEventListener(this)
  }

  override def eventDispatched(event: AWTEvent): Unit = {
    assert(event.isInstanceOf[KeyEvent], "Shall only listen to KeyEvents, so no other events shall come here")
    assert(RepeatingReleasedEventsFixer.assertEDT) // REMEMBER THAT THIS IS SINGLE THREADED, so no need for synch.

    // ?: Is this one of our synthetic RELEASED events?
    if (event.isInstanceOf[RepeatingReleasedEventsFixer.Reposted]) { // -> Yes, so we shalln't process it again.
      return
    }
    // ?: KEY_TYPED event? (We're only interested in KEY_PRESSED and KEY_RELEASED).
    if (event.getID == KeyEvent.KEY_TYPED) { // -> Yes, TYPED, don't process.
      return
    }
    val keyEvent = event.asInstanceOf[KeyEvent]
    // ?: Is this already consumed?
    // (Note how events are passed on to all AWTEventListeners even though a previous one consumed it)
    if (keyEvent.isConsumed) return
    // ?: Is this RELEASED? (the problem we're trying to fix!)
    if (keyEvent.getID == KeyEvent.KEY_RELEASED) { // -> Yes, so stick in wait
      /*
                   * Really just wait until "immediately", as the point is that the subsequent PRESSED shall already have been
                   * posted on the event queue, and shall thus be the direct next event no matter which events are posted
                   * afterwards. The code with the ReleasedAction handles if the Timer thread actually fires the action due to
                   * lags, by cancelling the action itself upon the PRESSED.
                   */ val timer = new Timer(2, null)
      val action = new ReleasedAction(keyEvent, timer)
      timer.addActionListener(action)
      timer.start()
      _map.put(Integer.valueOf(keyEvent.getKeyCode), action)
      // Consume the original
      keyEvent.consume()
    }
    else if (keyEvent.getID == KeyEvent.KEY_PRESSED) { // Remember that this is single threaded (EDT), so we can't have races.
      val action = _map.remove(Integer.valueOf(keyEvent.getKeyCode))
      // ?: Do we have a corresponding RELEASED waiting?
      if (action != null) { // -> Yes, so dump it
        action.cancel()
      }
      // System.out.println("PRESSED: [" + keyEvent + "]");
    }
    else throw new AssertionError("All IDs should be covered.")
  }

  /**
   * The [[ActionListener]] that posts the [[KeyEvent.KEY_RELEASED]] [[RepeatingReleasedEventsFixer.RepostedKeyEvent]] if the [[Timer]] times out (and hence the
   * repeat-action was over).
   */
  private class ReleasedAction private[utils](val _originalKeyEvent: KeyEvent, var _timer: Timer) extends ActionListener {
    /**
     * Stops the timer and removes the the key event from the map of repeating actions
     */
    private[utils] def cancel(): Unit = {
      assert(RepeatingReleasedEventsFixer.assertEDT)
      _timer.stop()
      _timer = null
      _map.remove(Integer.valueOf(_originalKeyEvent.getKeyCode))
    }

    override def actionPerformed(@SuppressWarnings(Array("unused")) e: ActionEvent): Unit = {
      assert(RepeatingReleasedEventsFixer.assertEDT)
      // ?: Are we already cancelled?
      // (Judging by Timer and TimerQueue code, we can theoretically be raced to be posted onto EDT by TimerQueue,
      // due to some lag, unfair scheduling)
      if (_timer == null) { // -> Yes, so don't post the new RELEASED event.
        return
      }
      // Stop Timer and clean.
      cancel()
      // Creating new KeyEvent (we've consumed the original).
      val newEvent = new RepeatingReleasedEventsFixer.RepostedKeyEvent(_originalKeyEvent.getSource.asInstanceOf[Component], _originalKeyEvent.getID, _originalKeyEvent.getWhen, _originalKeyEvent.getModifiers, _originalKeyEvent.getKeyCode, _originalKeyEvent.getKeyChar, _originalKeyEvent.getKeyLocation)
      // Posting to EventQueue.
      Toolkit.getDefaultToolkit.getSystemEventQueue.postEvent(newEvent)
      // System.out.println("Posted synthetic RELEASED [" + newEvent + "].");
    }
  }
}
