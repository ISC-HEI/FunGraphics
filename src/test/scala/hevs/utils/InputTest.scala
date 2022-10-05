package hevs.utils

import org.junit.jupiter.api.Assertions.{assertEquals,assertNotEquals}
import org.junit.jupiter.api.Test

import java.io.{ByteArrayOutputStream, PrintStream}

class InputTest {
  private val ls = System.lineSeparator()

  def redirect[A](op: () => A, in: String): A = {
    val backup = (System.in, System.out, System.err)
    val stdin = new java.io.ByteArrayInputStream(in.getBytes())
    val stdout = new ByteArrayOutputStream
    val stderr = new ByteArrayOutputStream
    System.setOut(new PrintStream(stdout))
    System.setErr(new PrintStream(stderr))
    System.setIn(stdin)

    val res = op()

    System.setIn(backup._1)
    System.setOut(backup._2)
    System.setErr(backup._3)

    res
  }

  @Test
  def minimalTestAll(): Unit = {
    assertEquals('$', redirect(Input.readChar, "$"))
    assertEquals("Hello World", redirect(Input.readString, "Hello World"))
    assertEquals(123, redirect(Input.readInt, "123" + ls))
    assertEquals(1.23, redirect(Input.readDouble, "1.23"))
    assertEquals(true, redirect(Input.readBoolean, "true"))
  }
    @Test
    def readString(): Unit = {
      val outIn = Seq(
        ("hello", "hello"),
        ("abc" + ls, "abc"),
        ("a bc" + ls, "a bc"),
        ("titi" + ls + "toto" + ls + "tutu", "titi"),
      )
      assertNotEquals("abc", redirect(Input.readString, "cde" + System.lineSeparator()), "That one should really not match")
      outIn.foreach(
        x =>
          assertEquals(x._2, redirect(Input.readString, x._1))
      )
    }

  @Test
  def readInt(): Unit = {
    // FIXME : Input.readInt will loop, how to test that?
  }
}
