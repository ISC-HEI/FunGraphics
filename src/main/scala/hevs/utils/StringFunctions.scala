package hevs.utils

/**
 * Utility class with useful methods for string manipulation
 */
object StringFunctions {
  /**
   * Get the number of characters in a [[String]]
   *
   * @param s the string
   * @return The length (number of characters) of the [[String]] s
   */
  def stringLength(s: String): Int = s.length

  /**
   * Get one character from a [[String]]
   *
   * Example :
   *  - `stringCharAt("hello", 0)` returns 'h'
   *  - `stringCharAt("hello", 1)` returns 'e'
   *
   * @param s the [[String]]
   * @param pos the position of the character
   * @return The [[Char]] at position `pos` in `s`
   */
  def stringCharAt(s: String, pos: Int): Char = s.charAt(pos)
}
