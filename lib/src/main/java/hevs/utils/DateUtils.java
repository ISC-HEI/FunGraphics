package hevs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Pierre-André Mudry, HES-SO Valais 
 * @version 1.0
 */
public class DateUtils {

	private static final long msPerHour = 60 * 60 * 1000;
	private static final long msPerDay = 24 * msPerHour;

	/**
	 * Creates a date from a text representation of this date
	 * 
	 * @param s
	 *            The text representation, formatted as "dd/MM/yyyy"
	 * @return The corresponding date
	 */
	static public Date createDate(String s) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		Date theDate = null;

		try {
			theDate = sdf.parse(s);
		} catch (ParseException ex) {
			System.err.println("Invalid date format specified !");
			ex.printStackTrace();
		}

		return theDate;
	}

	static private long nMsec(Date a, Date b) {
		if (b.after(a)) {
			return b.getTime() - a.getTime();
		} else {
			return a.getTime() - b.getTime();
		}
	}

	/**
	 * Computes the number of hours between two dates
	 * 
	 * @param a
	 *            The first date
	 * @param b
	 *            The second date
	 * @return The number of hours between the two dates
	 */
	static public int nHours(Date a, Date b) {
		int n = Math.round(nMsec(a, b) / msPerHour);
		return n;
	}

	/**
	 * Computes the number of day between two dates
	 * 
	 * @param a
	 *            The first date
	 * @param b
	 *            The second date
	 * @return The number of days between the two dates
	 */
	static public int nDays(Date a, Date b) {
		int nDays = Math.round(nMsec(a, b) / msPerDay);
		return nDays;
	}

	// Some samples
	public static void main(String args[]) {
		Date first = DateUtils.createDate("1/1/2000");
		Date second = DateUtils.createDate("1/1/2001");

		// It also takes into account leap years (2000)
		System.out.println("There were " + DateUtils.nDays(first, second) + " days in 2000");

		// How old are you in days ?
		Date birthdate = DateUtils.createDate("12/10/1977");
		Date now = new Date();
		System.out.println("You are " + DateUtils.nDays(now, birthdate) + " days old");
	}

}
