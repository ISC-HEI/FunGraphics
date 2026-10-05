package hevs.graphics.utils

import org.junit.jupiter.api.Assertions.{assertEquals, assertTrue}
import org.junit.jupiter.api.Test

import java.awt.AWTError

class DisplaySetupTest {
	@Test
	def localDisplaysMapToTheirSocket(): Unit = {
		assertEquals(Some("/tmp/.X11-unix/X0"), DisplaySetup.x11SocketPath(":0"))
		assertEquals(Some("/tmp/.X11-unix/X1"), DisplaySetup.x11SocketPath(":1.0"))
		assertEquals(Some("/tmp/.X11-unix/X99"), DisplaySetup.x11SocketPath("unix:99"))
		assertEquals(Some("/tmp/.X11-unix/X0"), DisplaySetup.x11SocketPath(" :0 "))
	}

	@Test
	def remoteOrOddDisplaysHaveNoSocket(): Unit = {
		assertEquals(None, DisplaySetup.x11SocketPath("localhost:10.0"))
		assertEquals(None, DisplaySetup.x11SocketPath("192.168.1.2:0"))
		assertEquals(None, DisplaySetup.x11SocketPath(""))
		assertEquals(None, DisplaySetup.x11SocketPath("garbage"))
	}

	@Test
	def diagnosisMentionsTheErrorAndTheEnvironment(): Unit = {
		val text = DisplaySetup.diagnosis(new AWTError("Can't connect to X11 window server using ':0' as the value of the DISPLAY variable."))
		assertTrue(text.contains("Can't connect to X11 window server"), text)
		assertTrue(text.contains("DISPLAY="), text)
		assertTrue(text.contains("Probable cause and fix"), text)
	}
}
