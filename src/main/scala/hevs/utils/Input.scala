package hevs.utils

import java.io.{BufferedReader, InputStreamReader}

/**
 * The class [[Input]] is here to enter data with the keyboard.<br>
 * The types below are supported by the [[Input]] class. <br>
 * <br>
 * - [[String]] <br>
 * - [[Int]] <br>
 * - [[Double]] <br>
 * - [[Boolean]] <br>
 * - [[Char]] <br>
 * <br>
 *
 * @see [[readString]]
 * @see [[readInt]]
 * @see [[readDouble]]
 * @see [[readBoolean]]
 * @see [[readChar]]
 */
object Input {
  /**
   * Reads a valid [[Char]] value from the console.
   *
   * @return The typed [[Char]]
   * @see [[java.lang.Character]]
   */
  def readChar(): Char = {
    var ok = false
    var res = -1
    while ( {
      !ok
    }) try {
      val stdin = new BufferedReader(new InputStreamReader(System.in))
      res = stdin.read
      ok = Character.isDefined(res)
    } catch {
      case ex: Exception =>
        System.out.println("This is not a valid character. Try again")
    }
    res.toChar
  }

  /**
   * Reads a [[String]] from the console.
   *
   * @return The typed [[String]]
   * @see [[java.lang.String]]
   */
  def readString(): String = {
    val stdin = new BufferedReader(new InputStreamReader(System.in))
    try stdin.readLine
    catch {
      case ex: Exception =>
        "There is a problem. Try again."
    }
  }

  /**
   * Reads a valid integer value from the console.
   *
   * This method also accepts hexadecimal values prefixed by "0x" or "0X"
   *
   * @return The typed [[Int]]
   * @see [[java.lang.Integer]]
   */
  def readInt(): Int = {
    var ok = false
    var res = -1
    while ( {
      !ok
    }) try {
      val stdin = new BufferedReader(new InputStreamReader(System.in))
      val s = stdin.readLine
      if (s.startsWith("0x") || s.startsWith("0X")) res = Integer.parseInt(s.substring(2), 16)
      else res = Integer.parseInt(s, 10)
      ok = true
    } catch {
      case ex: Exception =>
        System.out.println("This is not a valid number. Try again")
    }
    res
  }

  /**
   * Reads a valid [[Double]] value from the console.
   *
   * @return The typed [[Double]] value
   * @see [[java.lang.Double]]
   */
  def readDouble(): Double = {
    var ok = false
    var res = -1.0
    while ( {
      !ok
    }) try {
      val stdin = new BufferedReader(new InputStreamReader(System.in))
      res = stdin.readLine.toDouble
      ok = true
    } catch {
      case ex: Exception =>
        System.out.println("This is not a valid number. Try again")
    }
    res
  }

  /**
   * Reads a valid [[Boolean]] value from the console.
   *
   * @return the value `true` if the typed value is true, `false` otherwise.
   * @see [[java.lang.Boolean]]
   */
  def readBoolean(): Boolean = {
    var ok = false
    var res = false
    while ( {
      !ok
    }) try {
      val stdin = new BufferedReader(new InputStreamReader(System.in))
      res = java.lang.Boolean.parseBoolean(stdin.readLine)
      ok = true
    } catch {
      case ex: Exception =>
        System.out.println("This is not a valid boolean. Try again")
    }
    res
  }
}

class Input {}
