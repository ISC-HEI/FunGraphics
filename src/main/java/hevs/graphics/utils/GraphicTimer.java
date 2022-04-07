package hevs.graphics.utils;


/**
 * Provides a way to get a fixed framerate when calling the 
 * <code>sync</code> method in the game loop with the desired
 * frame-rate
 * 
 * Adapted from http://www.java-gaming.org/index.php?topic=22933.5
 * 
 * @author Pierre-Andre Mudry
 * @date March 2012 
 */
public class GraphicTimer {
	private long t1;
	private int missed;
	
	public GraphicTimer() {
		t1 = System.nanoTime();
	}

	/**
	 * Periodic call to this method in the main loop 
	 * insures a constant frame-rate. This is achieved in the
	 * method itself by waiting a variable amount of time to get
	 * a constant frame-rate
	 * @param fps The desired frame-rate
	 */
	public void sync(int fps) {
		Thread.yield();
		
		long t2 = 1000000000L / fps + t1;
		long now = System.nanoTime();
		
		/**
		if(gapTo < now)
			missed++;
		
		if(missed > 1){
			System.out.println("[GraphicTimer] Missed frame (frame-rate too high)");
			missed = 0;
		}**/
		
		try {
			while (t2 > now) {
				Thread.sleep((t2 - now) / 10000000);								
				now = System.nanoTime();				
			}
		} catch (Exception e) {
		}

		t1 = now;
	}
}