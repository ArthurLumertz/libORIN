package net.orin.lwjgl3;

public class DisplayMode {

    private int width;
    private int height;

    public DisplayMode(int width, int height) {
        this.width = Math.max(1, width);
        this.height = Math.max(1, height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
