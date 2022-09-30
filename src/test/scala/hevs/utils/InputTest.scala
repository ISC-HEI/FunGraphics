package hevs.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

import java.io.{ByteArrayOutputStream, PrintStream}

class InputTest {
  val ls = System.lineSeparator()

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
  def minimalTestAll() = {
    assertEquals('$', redirect(Input.readChar, "$"))
    assertEquals("Hello World", redirect(Input.readString, "Hello World"))
    assertEquals(123, redirect(Input.readInt, "123" + ls))
    assertEquals(1.23, redirect(Input.readDouble, "1.23"))
    assertEquals(true, redirect(Input.readBoolean, "true"))
  }
  /*
    @Test
    def readString() = {
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
    def readInt() = {
      val outIn = Seq(
        ("abc", 0, "that is not a number and the line separator is missing"),
        ("abc" + ls, 0, "that is not a number"),
        ("123", 123, "the line separator is missing"),
        ("456" + ls, 456, "should just work"),
        ("123,456" + ls, 0, "wow, not sure about that one"),
        ("12" + ls + "13" + ls + "14", 12, "should only get the first number"),
        ("0x12", 18, "0x+number should be decoded as hex"),
        ("0X13", 19, "0X+number should be decoded as hex"),
      )
      assertNotEquals(123, redirect(Input.readInt, "456" + System.lineSeparator()), "That one should really not match")
      outIn.foreach(
        x =>
          assertEquals(x._2, redirect(Input.readInt, x._1), x._3)
      )
    }
  */
}