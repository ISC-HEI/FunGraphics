package hevs.utils

import org.junit.jupiter.api.Assertions.{assertEquals,assertNotEquals}
import org.junit.jupiter.api.Test

import java.io.ByteArrayOutputStream
import java.io.PrintStream

class TextToolsTest {
  val ls = System.lineSeparator()

  def redirect[A](op: () => A, in:String) : A = {
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

    assertEquals("abc", redirect(TextTools.readText, "abc"))
    assertEquals(123,redirect(TextTools.readInt,"123"))
    assertEquals(12.3,redirect(TextTools.readDouble,"12.3"))
    assertEquals(true,redirect(TextTools.readBoolean,"true"))
    assertEquals('€',redirect(TextTools.readChar,"€"))
    assertEquals("321",TextTools.reverse("123"))
    assertEquals("ABC",TextTools.toUpperCase("abC"))
    assertEquals("abc",TextTools.toLowerCase("AbC"))
    assertEquals("Once upon",TextTools.capitalize("once upon"))
    assertEquals("abc",TextTools.invert("cba"))
    assertEquals("m rc",TextTools.deleteVowels("marc"))
    assertEquals(" a  ",TextTools.deleteConsonants("marc"))
    assertEquals("Hello world",TextTools.encrypt(TextTools.decrypt("Hello world")))
  }

  @Test
  def readText() = {
    val outIn = Seq(
      ("hello", "hello"),
      ("abc"+ls, "abc"),
      ("a bc"+ls, "a bc"),
      ("titi"+ls+"toto"+ls+"tutu", "titi"),
    )
    assertNotEquals("abc", redirect(TextTools.readText, "cde"+System.lineSeparator()), "That one should really not match")
    outIn.foreach(
      x =>
      assertEquals(x._2, redirect(TextTools.readText, x._1))
    )
  }

  @Test
  def readInt() = {
    val outIn = Seq(
      ("abc", 0, "that is not a number and the line separator is missing"),
      ("abc"+ls, 0, "that is not a number"),
      ("123", 123, "the line separator is missing"),
      ("456" + ls, 456, "should just work"),
      ("123,456" + ls, 0, "wow, not sure about that one"),
      ("12" + ls + "13" + ls + "14", 12, "should only get the first number"),
      ("0x12", 18, "0x+number should be decoded as hex"),
      ("0X13", 19, "0X+number should be decoded as hex"),
    )
    assertNotEquals(123, redirect(TextTools.readInt, "456" + System.lineSeparator()), "That one should really not match")
    outIn.foreach(
      x =>
        assertEquals(x._2, redirect(TextTools.readInt, x._1), x._3)
    )
  }

}
