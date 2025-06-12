package net.orin.lwjgl3;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import net.orin.io.FileRef;
import net.orin.lwjgl3.input.Keyboard;
import net.orin.lwjgl3.input.Mouse;
import net.orin.opengl.GLVersion;
import net.orin.system.OSEnum;
import net.orin.system.OperatingSystem;

public class Display {

	private static long window;
	private static int width;
	private static int height;
	private static String title;
	private static boolean isFullscreen;
	private static boolean useVsync;
	private static GLFWImage.Buffer icons;
	private static boolean resizable;
	private static int samples;

	private static int framebufferWidth;
	private static int framebufferHeight;

	private static int windowedX, windowedY;
	private static int windowedWidth = 854, windowedHeight = 480;
	
	public static void setDisplayMode(DisplayMode displayMode) {
		width = displayMode.getWidth();
		height = displayMode.getHeight();
		if (window != NULL) {
			glfwSetWindowSize(window, width, height);
		}
	}

	public static void setIcon(String iconPath) {
		if (iconPath == null)
			return;
		if (!iconPath.startsWith("/")) {
			iconPath = "/" + iconPath;
		}

		FileRef ref = FileRef.internal(iconPath);

		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer width = stack.mallocInt(1);
			IntBuffer height = stack.mallocInt(1);
			IntBuffer channels = stack.mallocInt(1);

			byte[] bytes = ref.readAllBytes();
			ByteBuffer buffer = MemoryUtil.memAlloc(bytes.length);
			buffer.put(bytes).flip();

			ByteBuffer pixels = STBImage.stbi_load_from_memory(buffer, width, height, channels, 0);
			if (pixels == null) {
				throw new RuntimeException("Failed to find texture file " + iconPath + "!");
			}

			icons = GLFWImage.malloc(1);
			icons.position(0);
			icons.width(width.get(0));
			icons.height(height.get(0));
			icons.pixels(pixels);
			icons.position(0);
		}
	}

	public static void create(GLVersion glVersion) {
		if (!glfwInit()) {
			throw new IllegalStateException("Failed to initialize GLFW!");
		}

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, glVersion.getMajor());
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, glVersion.getMinor());
		if (samples > 0) {
			glfwWindowHint(GLFW_SAMPLES, samples);
		}
		if (glVersion.isCoreProfile()) {
			glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
			if (OperatingSystem.getOS() == OSEnum.MACOS) {
				glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
			}
		}
		window = glfwCreateWindow(width, height, title, NULL, NULL);
		if (window == NULL) {
			throw new RuntimeException("Failed to create GLFW window!");
		}

		if (icons != null) {
			glfwSetWindowIcon(window, icons);
			icons.free();
		}

		glfwSetKeyCallback(window, new Keyboard());
		glfwSetMouseButtonCallback(window, new Mouse());
		glfwSetCursorPosCallback(window, new Mouse.MousePosition());

		glfwSetFramebufferSizeCallback(window, new GLFWFramebufferSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				updateFramebuffer();
				Display.width = width;
				Display.height = height;
			}
		});

		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

		glfwMakeContextCurrent(window);
		GL.createCapabilities();

		updateFramebuffer();
		glfwSwapInterval(useVsync ? 1 : 0);

		ALContext.create();

		glfwShowWindow(window);
	}

	public static long getWindow() {
		return window;
	}

	public static void setResizable(boolean resizable) {
		Display.resizable = resizable;
	}

	public static boolean shouldClose() {
		glfwPollEvents();
		return glfwWindowShouldClose(window);
	}

	private static void updateFramebuffer() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer fbWidth = stack.mallocInt(1);
			IntBuffer fbHeight = stack.mallocInt(1);
			glfwGetFramebufferSize(window, fbWidth, fbHeight);
			glViewport(0, 0, fbWidth.get(0), fbHeight.get(0));
			framebufferWidth = fbWidth.get(0);
			framebufferHeight = fbHeight.get(0);
		}
	}

	public static void swapBuffers() {
		glfwSwapBuffers(window);
	}

	public static void terminate() {
		ALContext.dispose();
		glfwTerminate();
	}

	public static void setWidth(int width) {
		width = Math.max(1, width);
		Display.width = width;
		if (window != NULL) {
			glfwSetWindowSize(window, width, height);
		}
	}

	public static void setHeight(int height) {
		height = Math.max(1, height);
		Display.height = height;
		if (window != NULL) {
			glfwSetWindowSize(window, width, height);
		}
	}

	public static void setTitle(String title) {
		Display.title = title;
		if (window != NULL) {
			glfwSetWindowTitle(window, title);
		}
	}

	public static void useVsync(boolean useVsync) {
		Display.useVsync = useVsync;
		if (window != NULL) {
			glfwSwapInterval(useVsync ? 1 : 0);
		}
	}

	public static void setSamples(int samples) {
		Display.samples = samples;
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

	public static String getTitle() {
		return title;
	}

	public static boolean isFullscreen() {
		return isFullscreen;
	}
	
	public static void setFullscreen(boolean fullscreen) {
		if (fullscreen == isFullscreen) return;

		isFullscreen = fullscreen;

		long monitor = glfwGetPrimaryMonitor();
		GLFWVidMode vidMode = glfwGetVideoMode(monitor);

		if (fullscreen) {
			try (MemoryStack stack = MemoryStack.stackPush()) {
				IntBuffer wx = stack.mallocInt(1);
				IntBuffer wy = stack.mallocInt(1);
				glfwGetWindowPos(window, wx, wy);
				windowedX = wx.get(0);
				windowedY = wy.get(0);

				IntBuffer ww = stack.mallocInt(1);
				IntBuffer wh = stack.mallocInt(1);
				glfwGetWindowSize(window, ww, wh);
				windowedWidth = ww.get(0);
				windowedHeight = wh.get(0);
			}

			glfwSetWindowMonitor(window, monitor, 0, 0, vidMode.width(), vidMode.height(), vidMode.refreshRate());
		} else {
			glfwSetWindowMonitor(window, NULL, windowedX, windowedY, windowedWidth, windowedHeight, 0);
		}
	}

	public static int getFramebufferWidth() {
		return framebufferWidth;
	}

	public static int getFramebufferHeight() {
		return framebufferHeight;
	}
	
}
