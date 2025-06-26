package net.orin.opengl;

import static org.lwjgl.opengl.GL11.*;

import net.orin.graphics.Color;

public class GLUtils {

	public static final int COLOR = GL_COLOR_BUFFER_BIT;
	public static final int DEPTH = GL_DEPTH_BUFFER_BIT;

	public static void clear(int mask, float r, float g, float b, float a) {
		glClear(mask);
		glClearColor(r, g, b, a);
	}

	public static void clear(int mask, Color color) {
		clear(mask, color.r, color.g, color.b, color.a);
	}
	
	public static void clear(int mask, float[] colors) {
		clear(mask, colors[0], colors[1], colors[2], colors[3]);
	}

	public static void clear(float r, float g, float b, float a) {
		clear(GL_COLOR_BUFFER_BIT, r, g, b, a);
	}
	
	public static void clear(Color color) {
		clear(GL_COLOR_BUFFER_BIT, color);
	}
	
	public static void clear(float[] colors) {
		clear(GL_COLOR_BUFFER_BIT, colors[0], colors[1], colors[2], colors[3]);
	}

	public static void viewport(int x0, int y0, int x1, int y1) {
		glViewport(x0, y0, x1, y1);
	}
	
}
