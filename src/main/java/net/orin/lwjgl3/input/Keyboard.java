package net.orin.lwjgl3.input;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard extends GLFWKeyCallback {

    private static boolean[] keysDown = new boolean[GLFW_KEY_LAST];
    private static boolean[] keysReleased = new boolean[GLFW_KEY_LAST];

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (key < 0 || key >= GLFW_KEY_LAST) {
            return;
        }
        keysDown[key] = action != GLFW_RELEASE;
        keysReleased[key] = action == GLFW_PRESS;
    }

    public static boolean isKeyDown(int key) {
        return keysDown[key];
    }

    public static boolean isKeyReleased(int key) {
        boolean result = keysReleased[key];
        keysReleased[key] = false;
        return result;
    }

}
