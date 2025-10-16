package hevs.graphics.advanced.samples

import hevs.graphics.advanced.{Drawable, ListGraphics}
import hevs.graphics.utils.GraphicsBitmap

import java.awt.Color
import java.util.Random

/**
 * A simple class representing a drawable image
 */
class ImageBackground extends Drawable {
	private[samples] val bmp = new GraphicsBitmap("/res/img/mandrill.jpg")
	private[samples] var scale = 0.3
	private[samples] val rotation = 0
	private[samples] var direction = 0.005

	override def draw(g: ListGraphics): Unit = {
		g.drawTransformedPicture(250, 250, rotation, scale, bmp)
	}
}

/**
 * Performance testers for [[ListGraphics]]
 *
 * @author Pierre-Andre Mudry
 * @version 1.0
 */
object ComplexTesterWithImage {
	/**
	 * Complex tests with [[ListGraphics]]
	 *
	 * @param args unused
	 */
	def main(args: Array[String]): Unit = {
		new ComplexTesterWithImage
	}
}

class ComplexTesterWithImage private[samples]() {
	val g = new ListGraphics(500, 500, "ComplexTester", true)
	val rrand = new Random(12)
	val NCIRCLES = 100
	val rects = new Array[DrawableCircle](NCIRCLES)
	val directions = new Array[Int](NCIRCLES)
	val COLORS: Array[Color] = Array[Color](Color.red, Color.blue, Color.green, Color.black,
		Color.yellow, new Color(100, 100, 255), Color.cyan, Color.pink,
		Color.lightGray, Color.magenta, Color.orange, Color.darkGray)

	/**
	 * Generate some objects randomly
	 */
	for (i <- 0 until NCIRCLES) {
		val width = 50 + rrand.nextInt(50)
		rects(i) = new DrawableCircle(width, width, 50 + rrand.nextInt(350), rrand.nextInt(450), COLORS(rrand.nextInt(COLORS.length)))
		g.addDrawableObject(rects(i))
		directions(i) = rrand.nextInt(6) + 1
		directions(i) = if (rrand.nextBoolean) directions(i) else -directions(i)
	}
	/**
	 * Add the bitmap
	 */
	val bg = new ImageBackground
	g.addDrawableObject(bg)

	/**
	 * Drawing loop
	 */
	while (true) {
		for (i <- 0 until NCIRCLES) {
			val r = rects(i)
			if (r.x > 480 || r.x < 0) directions(i) *= -1
			r.x += directions(i)
		}
		bg.direction = if (bg.scale > 0.5 || bg.scale < 0.2) bg.direction * -1 else bg.direction
		bg.scale += bg.direction
		g.repaint()
		g.syncGameLogic(60)
	}
}
