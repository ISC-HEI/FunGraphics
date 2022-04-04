package hevs.graphics.interfaces;

public interface DualLayerGraphics {

	/**
	 * Call this method to modify only the background
	 */
	public abstract void drawBackground();

	/**
	 * Call this method to modify only the foreground
	 */
	public abstract void drawForeground();

}