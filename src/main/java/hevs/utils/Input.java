package hevs.utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * * The Class Input is here to enter data with the keyboard.<br>
 * The types below are supported by the Input class. <br>
 * <br>
 * - String <br>
 * - Integer (int) <br>
 * - Double (double) - Boolean (boolean) <br>
 * - Character (char) <br>
 * <br>
 * <br>
 *
 * @author Patrice Rudaz (patrice.rudaz@hevs.ch)
 * @author Cathy Berthouzoz (cathy.berthouzoz@hevs.ch)
 * @modified Pierre-André Mudry
 * @see #readString()
 * @see #readLong()
 * @see #readLong()
 * @see #readDouble()
 * @see #readBoolean()
 * @see #readChar()
 */
public class Input {
	/**
	 * * Reads a valid char value from the console.
	 *
	 * @return The typed char
	 * @see java.lang.Character
	 */
	public static char readChar() {
		boolean ok = false;
		int res = -1;
		while (!ok) {
			try {
				BufferedReader stdin = new BufferedReader(
						new InputStreamReader(System.in));
				res = stdin.read();
				ok = Character.isDefined(res);
			} catch (Exception ex) {
				System.out.println("This is not a valid character. Try again");
			}
		}
		return (char) res;
	}

	/**
	 * * Reads a String from the console.
	 *
	 * @return The typed string
	 * @see java.lang.String
	 */
	public static String readString() {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			return stdin.readLine();
		} catch (Exception ex) {
			return "There is a problem. Try again.";
		}
	}

	/**
	 * * Reads a valid integer value from the console.
	 *
	 * @return The typed value
	 * @see java.lang.Integer
	 */
	public static int readInt() {
		boolean ok = false;
		int res = -1;
		while (!ok) {
			try {
				BufferedReader stdin = new BufferedReader(
						new InputStreamReader(System.in));
				String s = stdin.readLine();
				if (s.startsWith("0x") || s.startsWith("0X")) {
					res = Integer.parseInt(s.substring(2), 16);
				} else {
					res = Integer.parseInt(s, 10);
				}
				ok = true;
			} catch (Exception ex) {
				System.out.println("This is not a valid number. Try again");
			}
		}
		return res;
	}

	/**
	 * * Reads a valid double value from the console.
	 *
	 * @return The typed double value
	 * @see java.lang.Double
	 */
	public static double readDouble() {
		boolean ok = false;
		double res = -1;
		while (!ok) {
			try {
				BufferedReader stdin = new BufferedReader(
						new InputStreamReader(System.in));
				res = Double.parseDouble(stdin.readLine());
				ok = true;
			} catch (Exception ex) {
				System.out.println("This is not a valid number. Try again");
			}
		}
		return res;
	}

	/**
	 * * Reads a valid boolean value from the console.
	 *
	 * @return the value true if the typed value is true, false otherwise.
	 * @see java.lang.Boolean
	 */
	public static boolean readBoolean() {
		boolean ok = false;
		boolean res = false;
		while (!ok) {
			try {
				BufferedReader stdin = new BufferedReader(
						new InputStreamReader(System.in));
				res = Boolean.parseBoolean(stdin.readLine());
				ok = true;
			} catch (Exception ex) {
				System.out.println("This is not a valid boolean. Try again");
			}
		}
		return res;
	}
}
