package hevs.graphics.utils


/**
 * Provides a way to get a fixed frame rate when calling the
 * [[sync]] method in the game loop with the desired
 * frame rate
 *
 * Adapted from [[http://www.java-gaming.org/index.php?topic=22933.5]]
 *
 * @author Pierre-Andre Mudry
 */
class GraphicTimer() {
  private var t1 = System.nanoTime
  private val missed = 0

  /**
   * Periodic call to this method in the main loop
   * insures a constant frame rate. This is achieved in the
   * method itself by waiting a variable amount of time to get
   * a constant frame rate
   *
   * @param fps The desired frame rate
   */
  def sync(fps: Int): Unit = {
    Thread.`yield`()
    val t2 = 1000000000L / fps + t1
    var now = System.nanoTime

    /**
     * if(gapTo < now)
     * missed++;
     *
     * if(missed > 1){
     * System.out.println("[GraphicTimer] Missed frame (frame-rate too high)");
     * missed = 0;
     * }* */
    try while ( {
      t2 > now
    }) {
      Thread.sleep((t2 - now) / 10000000)
      now = System.nanoTime
    }
    catch {
      case e: Exception =>

    }
    t1 = now
  }
}