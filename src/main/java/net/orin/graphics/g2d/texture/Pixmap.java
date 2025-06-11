package net.orin.graphics.g2d.texture;

import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import net.orin.graphics.Color;
import net.orin.io.FileRef;

public class Pixmap {

	private int width;
	private int height;
	private int channels;
	private int[] pixels;

	public Pixmap() {
	}
	
	public Pixmap(String filePath) {
		this(FileRef.internal(filePath));
	}
	
	public Pixmap(FileRef ref) {
	    try (MemoryStack stack = MemoryStack.stackPush()) {
	        if (ref.getInputStream() == null) {
	            throw new RuntimeException("Failed to load texture: " + ref.path() + "!");
	        }

	        IntBuffer w = stack.mallocInt(1);
	        IntBuffer h = stack.mallocInt(1);
	        IntBuffer c = stack.mallocInt(1);

	        byte[] bytes = ref.readAllBytes();
	        ByteBuffer buffer = MemoryUtil.memAlloc(bytes.length);
	        buffer.put(bytes).flip();

	        stbi_set_flip_vertically_on_load(true);
	        ByteBuffer pixelBytes = stbi_load_from_memory(buffer, w, h, c, 4);
	        MemoryUtil.memFree(buffer);

	        if (pixelBytes == null) {
	            throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
	        }

	        width = w.get(0);
	        height = h.get(0);
	        channels = c.get(0);

	        pixels = new int[width * height];

	        for (int i = 0; i < width * height; i++) {
	            int r = pixelBytes.get(i * 4) & 0xFF;
	            int g = pixelBytes.get(i * 4 + 1) & 0xFF;
	            int b = pixelBytes.get(i * 4 + 2) & 0xFF;
	            int a = pixelBytes.get(i * 4 + 3) & 0xFF;
	            pixels[i] = (a << 24) | (b << 16) | (g << 8) | r;
	        }

	        stbi_image_free(pixelBytes);
	    }
	}

	public Pixmap(int width, int height) {
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
	}
	
	public int getPixel(int x, int y) {
        if (!inBounds(x, y)) return 0;
        return pixels[y * width + x];
    }

    public void setPixel(int x, int y, int argb) {
        if (!inBounds(x, y)) return;
        pixels[y * width + x] = argb;
    }

    public void setPixel(int x, int y, Color color) {
    	if (!inBounds(x, y)) return;
    	setPixel(x, y, color.toHex());
    }
    
    public void clear(int colorARGB) {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = colorARGB;
        }
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public void fillRect(int x, int y, int w, int h, int colorARGB) {
        for (int iy = 0; iy < h; iy++) {
            for (int ix = 0; ix < w; ix++) {
                setPixel(x + ix, y + iy, colorARGB);
            }
        }
    }

    public void drawLine(int x0, int y0, int x1, int y1, int colorARGB) {
        int dx = Math.abs(x1 - x0);
        int dy = -Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx + dy;

        while (true) {
            setPixel(x0, y0, colorARGB);
            if (x0 == x1 && y0 == y1) break;
            int e2 = 2 * err;
            if (e2 >= dy) {
                err += dy;
                x0 += sx;
            }
            if (e2 <= dx) {
                err += dx;
                y0 += sy;
            }
        }
    }

	
	public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public int getChannels() {
    	return channels;
    }

    public int[] getPixels() {
        return pixels;
    }
	
}
