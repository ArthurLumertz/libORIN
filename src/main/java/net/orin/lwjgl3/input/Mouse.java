package net.orin.lwjgl3.input;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import net.orin.lwjgl3.Display;

public class Mouse extends GLFWMouseButtonCallback {

	private static boolean[] buttonsDown = new boolean[GLFW_MOUSE_BUTTON_LAST];
	private static boolean[] buttonsReleased = new boolean[GLFW_MOUSE_BUTTON_LAST];

	private static float xo;
	private static float yo;

	private static float x;
	private static float y;

	private static float dx;
	private static float dy;

	@Override
	public void invoke(long window, int button, int action, int mods) {
		if (button < 0 || button > GLFW_MOUSE_BUTTON_LAST) {
			return;
		}
		buttonsDown[button] = action != GLFW_RELEASE;
		buttonsReleased[button] = action == GLFW_PRESS;
	}

	public static boolean isGrabbed() {
		int mode = glfwGetInputMode(Display.getWindow(), GLFW_CURSOR);
		return mode == GLFW_CURSOR_DISABLED;
	}

	public static void setGrabbed(boolean grabbed) {
		glfwSetInputMode(Display.getWindow(), GLFW_CURSOR, grabbed ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
	}

	public static boolean isButtonDown(int button) {
		return buttonsDown[button];
	}

	public static boolean isButtonReleased(int button) {
		boolean result = buttonsReleased[button];
		buttonsReleased[button] = false;
		return result;
	}

	public static float getX() {
		return x;
	}

	public static float getY() {
		return y;
	}

	public static float getDX() {
		float result = dx;
		dx = 0f;
		return result;
	}

	public static float getDY() {
		float result = dy;
		dy = 0f;
		return result;
	}

	public static class MousePosition extends GLFWCursorPosCallback {

		@Override
		public void invoke(long window, double xpos, double ypos) {
			xo = x;
			yo = y;

			x = (float) xpos;
			y = Display.getHeight() - (float) ypos;

			float rdx = x - xo;
			float rdy = y - yo;
			dx += (rdx - dx) * 0.2f;
			dy += (rdy - dy) * 0.2f;
		}

	}

}
