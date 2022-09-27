package hevs.graphics.interfaces

trait DualLayerGraphics {
  /**
   * Call this method to modify only the background
   */
  def drawBackground(): Unit

  /**
   * Call this method to modify only the foreground
   */
  def drawForeground(): Unit
}