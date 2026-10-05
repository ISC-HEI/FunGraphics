package hevs.graphics.utils

import java.awt.{AWTError, HeadlessException}
import java.nio.file.{Files, Paths}

/**
 * Checks that a graphical display can be reached before the first window is created
 * and explains what to do when it cannot.
 *
 * On Linux, the standard JDK only talks to X11. On a Wayland desktop (Fedora, Ubuntu, ...)
 * that works through Xwayland, except when the program runs in a sandbox that was not
 * given X11 access. The typical case is IntelliJ IDEA installed as a Flatpak: the IDE
 * itself runs natively on Wayland, but the student's program then fails with
 * `java.awt.AWTError: Can't connect to X11 window server`.
 *
 * Two things are done here:
 *   - if the JDK ships a Wayland toolkit (the JetBrains Runtime does) and X11 is clearly
 *     unavailable, that toolkit is selected so that the program works anyway;
 *   - if opening the display fails, a diagnosis with the probable cause and the fix is
 *     printed on the standard error output before the error is rethrown.
 *
 * @author Pierre-André Mudry
 */
object DisplaySetup {
	/** Name of the Wayland toolkit shipped with the JetBrains Runtime */
	private val WaylandToolkitClass = "sun.awt.wl.WLToolkit"
	private val WaylandToolkitName = "WLToolkit"

	private var prepared = false

	/**
	 * Must be called before any AWT or Swing class is touched. Selects the Wayland
	 * toolkit when X11 cannot be used and the running JDK has one. Safe to call several times.
	 */
	def prepare(): Unit = synchronized {
		if (prepared) return
		prepared = true

		if (!isLinux) return
		// Already on the Wayland toolkit, or headless mode was chosen explicitly: nothing to do.
		// Note that the JetBrains Runtime presets awt.toolkit.name to XToolkit, so a value there is not a user choice.
		if (WaylandToolkitName == System.getProperty("awt.toolkit.name") || System.getProperty("java.awt.headless") != null) return

		val wayland = env("WAYLAND_DISPLAY")
		if (wayland.isEmpty || !x11LooksUnavailable) return

		if (classExists(WaylandToolkitClass)) {
			System.setProperty("awt.toolkit.name", WaylandToolkitName)
			System.err.println("[FunGraphics] No usable X11 display, using the Wayland toolkit of this JDK instead")
		}
	}

	/**
	 * Runs `op`, which is expected to be the first call that opens the display. When it
	 * fails because no display can be reached, prints a diagnosis and rethrows.
	 *
	 * @param op the code opening the display
	 * @tparam A the result type of `op`
	 * @return the result of `op`
	 */
	def openingDisplay[A](op: => A): A = {
		try op
		catch {
			case e: AWTError if isDisplayError(e) =>
				System.err.println(diagnosis(e))
				throw e
			case e: HeadlessException =>
				System.err.println(diagnosis(e))
				throw e
		}
	}

	/**
	 * Path of the Unix socket used by Xlib for a given `DISPLAY` value, when it is a local one
	 *
	 * @param display the content of the `DISPLAY` variable, e.g. `:0`, `:1.0` or `unix:0`
	 * @return the socket path, or `None` for a remote (TCP) display or an unparsable value
	 */
	private[utils] def x11SocketPath(display: String): Option[String] = {
		val Local = """^(?:unix)?:(\d+)(?:\.\d+)?$""".r
		display.trim match {
			case Local(number) => Some(s"/tmp/.X11-unix/X$number")
			case _ => None
		}
	}

	/**
	 * True when we are confident that the X11 toolkit cannot work: `DISPLAY` is not set, or it
	 * names a local display whose socket does not exist (the usual situation inside a Flatpak
	 * sandbox that only has `fallback-x11` access on a Wayland session).
	 */
	private def x11LooksUnavailable: Boolean = env("DISPLAY") match {
		case None => true
		case Some(display) => x11SocketPath(display).exists(p => !Files.exists(Paths.get(p)))
	}

	private def isDisplayError(e: AWTError): Boolean = {
		val msg = Option(e.getMessage).getOrElse("")
		msg.contains("X11") || msg.contains("DISPLAY") || msg.contains("display")
	}

	/**
	 * Builds a human-readable explanation of why the display could not be opened
	 *
	 * @param e the error that was thrown
	 * @return the text to print
	 */
	private[utils] def diagnosis(e: Throwable): String = {
		val display = env("DISPLAY")
		val wayland = env("WAYLAND_DISPLAY")
		val flatpak = env("FLATPAK_ID")
		val snap = env("SNAP_NAME")
		val ssh = env("SSH_CONNECTION")
		val socketMissing = display.flatMap(x11SocketPath).exists(p => !Files.exists(Paths.get(p)))

		val sb = new StringBuilder
		sb ++= "\n[FunGraphics] Could not open a window: Java cannot connect to the graphical display.\n"
		sb ++= s"[FunGraphics]   ${e.getClass.getSimpleName}: ${Option(e.getMessage).getOrElse("").trim.replace('\n', ' ')}\n"
		sb ++= s"[FunGraphics]   DISPLAY=${display.getOrElse("(not set)")}  WAYLAND_DISPLAY=${wayland.getOrElse("(not set)")}" +
			s"  XAUTHORITY=${env("XAUTHORITY").getOrElse("(not set)")}\n"
		sb ++= s"[FunGraphics]   Java: ${System.getProperty("java.vendor")} ${System.getProperty("java.version")} in ${System.getProperty("java.home")}\n"
		flatpak.foreach(id => sb ++= s"[FunGraphics]   Running inside the Flatpak sandbox of $id\n")
		snap.foreach(name => sb ++= s"[FunGraphics]   Running inside the Snap sandbox of $name\n")
		sb ++= "[FunGraphics] Probable cause and fix:\n"

		if (flatpak.isDefined && (socketMissing || display.isEmpty)) {
			val id = flatpak.get
			sb ++= s"[FunGraphics]   Your IDE is a Flatpak that runs on Wayland without X11 access, so your program cannot open an X11 window.\n"
			sb ++= s"[FunGraphics]   Give it X11 access once, then restart the IDE:\n"
			sb ++= s"[FunGraphics]       flatpak override --user --socket=x11 $id\n"
			sb ++= s"[FunGraphics]   Alternative: use the JetBrains Runtime bundled with the IDE as the project SDK (JAVA_HOME is " +
				s"${env("JAVA_HOME").getOrElse("/app/jbr")}), FunGraphics then uses Wayland directly.\n"
		} else if (snap.isDefined && (socketMissing || display.isEmpty)) {
			sb ++= s"[FunGraphics]   Your IDE is a Snap without X11 access. Connect the x11 interface (snap connect ${snap.get}:x11) or install the IDE outside the Snap.\n"
		} else if (display.isEmpty && wayland.isDefined) {
			sb ++= "[FunGraphics]   You are on a Wayland desktop but Xwayland is not running, so the Java X11 toolkit has nothing to connect to.\n"
			sb ++= "[FunGraphics]   Install Xwayland (Fedora: xorg-x11-server-Xwayland, Debian/Ubuntu: xwayland), log out and in again, or use an X11 session.\n"
		} else if (display.isEmpty && ssh.isDefined) {
			sb ++= "[FunGraphics]   You are connected through SSH without display forwarding. Reconnect with ssh -X or ssh -Y.\n"
		} else if (display.isEmpty) {
			sb ++= "[FunGraphics]   The DISPLAY variable is not set. Run the program from a graphical session (not from a service, a container or a bare TTY).\n"
		} else if (socketMissing) {
			sb ++= s"[FunGraphics]   DISPLAY points to a local X server but its socket ${display.flatMap(x11SocketPath).get} does not exist.\n"
			sb ++= "[FunGraphics]   The X server is not running or the program runs in a sandbox or container that does not share /tmp/.X11-unix.\n"
		} else {
			sb ++= "[FunGraphics]   The X server is running but refused the connection, which is usually an authorization problem.\n"
			sb ++= "[FunGraphics]   Check that XAUTHORITY is set for the program, or allow your user from a terminal of your desktop session:\n"
			sb ++= "[FunGraphics]       xhost +si:localuser:$USER\n"
		}
		sb.result()
	}

	private def env(name: String): Option[String] = Option(System.getenv(name)).map(_.trim).filter(_.nonEmpty)

	private def isLinux: Boolean = System.getProperty("os.name", "").toLowerCase.contains("linux")

	private def classExists(name: String): Boolean =
		try {
			Class.forName(name, false, getClass.getClassLoader)
			true
		} catch {
			case _: Throwable => false
		}
}
