package hevs.utils

import java.io.BufferedReader
import java.io.InputStreamReader


/**
 * A class with functions to manipulate text strings. Based on an old
 * <code>TextTools</code> version that hid all the strings in static variables
 * inside the class. This has all been removed and now everything is explicit
 * and exposed.
 *
 * @author Pierre-André Mudry, pierre-andre.mudry@hevs.ch
 */
object TextTools {
  /**
   * Get a line of text from the console.
   */
  def readText(): String = {
    val stdin = new BufferedReader(new InputStreamReader(System.in))
    System.out.print("Enter a text : ")
    try stdin.readLine
    catch {
      case ex: Exception =>
        ""
    }
  }

  /**
   * Get an integer value from the console. Can read octal and hexadecimal
   * formats as well
   */
  def readInt(): Int = {
    System.out.print("Enter an integer value in decimal, octal or hexadecimal format : ")
    val stdin = new BufferedReader(new InputStreamReader(System.in))
    try {
      val s = stdin.readLine
      if (s.startsWith("0x") || s.startsWith("0X")) Integer.parseInt(s.substring(2), 16)
      else if (s.startsWith("0")) Integer.parseInt(s.substring(1), 8)
      else Integer.parseInt(s, 10)
    } catch {
      case ex: Exception =>
        0
    }
  }

  /**
   * Get a double value from the console.
   */
  def readDouble(): Double = {
    System.out.print("Enter a double value : ")
    val stdin = new BufferedReader(new InputStreamReader(System.in))
    try stdin.readLine.toDouble
    catch {
      case ex: Exception =>
        0
    }
  }

  /**
   * Read a boolean value (true, false) from the console.
   */
  def readBoolean(): Boolean = {
    System.out.print("Enter a boolean value : ")
    val stdin = new BufferedReader(new InputStreamReader(System.in))
    try java.lang.Boolean.valueOf(stdin.readLine)
    catch {
      case ex: Exception =>
        false
    }
  }

  /**
   * Get a char from the console.
   */
  def readChar(): Char = {
    System.out.print("Enter a character : ")
    val stdin = new BufferedReader(new InputStreamReader(System.in))
    try stdin.readLine.charAt(0)
    catch {
      case ex: Exception =>
        '0'
    }
  }

  /**
   * Reverse the given string <code>input</code> ...
   *
   * @param input
   * as string : the given string
   */
  def reverse(input: String): String = {
    var output = new String("")
    for (i <- 0 until input.length) {
      output = output + input.charAt(input.length - (i + 1))
    }
    output
  }

  /**
   * Converts all of the characters in this String to upper case.
   *
   * @param input
   * as string : the given string
   */
  def toUpperCase(input: String): String = input.toUpperCase

  /**
   * Converts all of the characters in this String to lower case.
   *
   * @param input
   * as string : the given string
   */
  def toLowerCase(input: String): String = input.toLowerCase

  /**
   * Converts the first character of this String to upper case.
   *
   * @param input
   * as string : the given string
   */
  def capitalize(input: String): String = {
    var output = String.valueOf(input.charAt(0)).toUpperCase
    for (i <- 1 until input.length) {
      output = output + input.charAt(i)
    }
    output
  }

  /**
   * Inverts a complete String
   *
   * @param input
   * @return the inverted String
   */
  def invert(input: String): String = invert(input, input.length)

  /**
   * Inverts the characters of the input string by group of
   * <code>number</code> characters
   *
   * @param input
   * as string : the given string
   * @param number
   * as int : the number of charaters to invert
   */
  private def invert(input: String, number: Int) = {
    var i = 0
    var output = new String("")
    if (input.length >= number) {
      i = 0
      while ( {
        i <= input.length - number
      }) {
        for (j <- number until 0 by -1) {
          output = output + input.charAt(i + j - 1)
        }

        i = i + number
      }
      for (j <- input.length until i by -1) {
        output = output + input.charAt(j - 1)
      }
    }
    else for (j <- input.length until 0 by -1) {
      output = output + input.charAt(j - 1)
    }
    output
  }

  /**
   * Hide any vowel in a string
   *
   * @param input
   * as string : the given string
   */
  def deleteVowels(input: String): String = {
    var ASCII = 0
    var output = new String("")
    for (i <- 0 until input.length) {
      ASCII = input.codePointAt(i)
      if (ASCII == 97 || ASCII == 65 || /* 'a' & 'A' */ ASCII == 101 || ASCII == 69 || /* 'e' & 'E' */ ASCII == 105 || ASCII == 73 || /* 'i' & 'I' */ ASCII == 111 || ASCII == 79 || /* 'o' & 'O' */ ASCII == 117 || ASCII == 85 || /* 'u' & 'U' */ ASCII == 121 || ASCII == 89 || /* 'y' & 'Y' */ (ASCII >= 192 && ASCII <= 207) || (ASCII >= 210 && ASCII <= 214) || (ASCII >= 217 && ASCII <= 221) || (ASCII >= 224 && ASCII <= 230) || (ASCII >= 232 && ASCII <= 239) || (ASCII >= 242 && ASCII <= 246) || (ASCII >= 249 && ASCII <= 253) || ASCII == 255) output = output + " "
      else output = output + input.charAt(i)
    }
    output
  }

  /**
   * Deletes each consonant in a string
   *
   * @param input
   * as string : the given string
   */
  def deleteConsonants(input: String): String = {
    var ASCII = 0
    var output = new String("")
    for (i <- 0 until input.length) {
      ASCII = input.codePointAt(i)
      if (ASCII == 97 || ASCII == 65 || ASCII == 101 || ASCII == 69 || ASCII == 105 || ASCII == 73 || ASCII == 111 || ASCII == 79 || ASCII == 117 || ASCII == 85 || ASCII == 121 || ASCII == 89 || (ASCII >= 192 && ASCII <= 207) || (ASCII >= 210 && ASCII <= 214) || (ASCII >= 217 && ASCII <= 221) || (ASCII >= 224 && ASCII <= 230) || (ASCII >= 232 && ASCII <= 239) || (ASCII >= 242 && ASCII <= 246) || (ASCII >= 249 && ASCII <= 253) || (ASCII >= 33 && ASCII <= 64) || (ASCII >= 91 && ASCII <= 96) || (ASCII >= 123 && ASCII <= 191) || (ASCII >= 208 && ASCII <= 209) || (ASCII >= 215 && ASCII <= 216) || (ASCII >= 222 && ASCII <= 223) || (ASCII >= 240 && ASCII <= 241) || (ASCII >= 247 && ASCII <= 248) || (ASCII >= 254 && ASCII <= 255)) output = output + input.charAt(i)
      else output = output + " "
    }
    output
  }

  /**
   * Encrypts a string
   *
   * @param input
   * as string : the given string
   */
  def encrypt(input: String): String = {
    var output = new String("")
    for (i <- 0 until input.length) {
      output = output + ((input.charAt(i) + 5) % 256).toChar
    }
    output
  }

  /**
   * Decrypts an encrypted string
   *
   * @param input
   * as string : the given string
   */
  def decrypt(input: String): String = {
    var output = new String("")
    for (i <- 0 until input.length) {
      output = output + ((input.charAt(i) - 5) % 256).toChar
    }
    output
  }
}