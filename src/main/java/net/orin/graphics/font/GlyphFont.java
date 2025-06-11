package net.orin.graphics.font;

import net.orin.graphics.Color;
import net.orin.graphics.g2d.TextureBatch;
import net.orin.graphics.g2d.texture.Pixmap;
import net.orin.graphics.g2d.texture.Texture;
import net.orin.graphics.g2d.texture.Texture.Filter;
import net.orin.graphics.g2d.texture.TextureRegion;
import net.orin.io.FileRef;

public class GlyphFont implements Font {

	private final Color tmpColor = new Color();

	private final int[] charWidths = new int[256];
	private final TextureRegion[] charRegions = new TextureRegion[256];

	private final Texture texture;
	private final int glyphSize;

	private final TextureBatch batch;

	private float fontSize = 16f;
	
	public GlyphFont(TextureBatch batch) {
		this("default.png", 8, batch);
	}

	public GlyphFont(String filePath, int glyphSize, TextureBatch batch) {
		this(FileRef.internal(filePath), glyphSize, batch);
	}

	public GlyphFont(FileRef ref, int glyphSize, TextureBatch batch) {
		Pixmap pixmap = new Pixmap(ref);
		texture = new Texture(pixmap, Filter.NEAREST);

		int w = pixmap.getWidth();
		int h = pixmap.getHeight();
		int[] rawPixels = new int[w * h];

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				int flippedY = h - 1 - y;
				rawPixels[x + y * w] = pixmap.getPixel(x, flippedY);
			}
		}

		for (int i = 0; i < 128; i++) {
			int x;
			int xt = i % 16;
			int yt = i / 16;
			boolean emptyColumn = false;

			for (x = 0; x < glyphSize && !emptyColumn; x++) {
				int xPixel = xt * glyphSize + x;
				emptyColumn = true;
				for (int y = 0; y < glyphSize; ++y) {
					int yPixel = (yt * glyphSize + y) * w;
					int pixel = rawPixels[xPixel + yPixel] & 0xFF;
					if (pixel <= 128)
						continue;
					emptyColumn = false;
				}
			}

			if (i == 32) {
				x = glyphSize / 2;
			}

			charWidths[i] = x;
			charRegions[i] = new TextureRegion(texture, xt * glyphSize, yt * glyphSize, glyphSize, glyphSize);
		}

		this.glyphSize = glyphSize;
		this.batch = batch;
	}

	@Override
	public void draw(String msg, float x, float y, Color color, boolean darken) {
		int hex = color.toHex();
		if (darken) {
			int a = hex & 0xFF;
			int r = (hex >> 24) & 0xFF;
			int g = (hex >> 16) & 0xFF;
			int b = (hex >> 8) & 0xFF;

			r = (r * 1) / 4;
			g = (g * 1) / 4;
			b = (b * 1) / 4;

			hex = (r << 24) | (g << 16) | (b << 8) | a;
		}

		tmpColor.set(hex);
		tmpColor.a = 1f;

		float xo = 0f;
		for (int i = 0; i < msg.length(); i++) {
			char ch = msg.charAt(i);

			TextureRegion region = charRegions[ch];
			batch.drawRegion(region, (x + xo), (y - fontSize), fontSize, fontSize, tmpColor);

			xo += charWidths[ch] * 2f;
		}
	}

	@Override
	public void drawShadow(String msg, float x, float y, Color color) {
		draw(msg, x + (fontSize / glyphSize), y - (fontSize / glyphSize), color, true);
		draw(msg, x, y, color, false);
	}

	@Override
	public float getWidth(String msg) {
		int len = 0;
		for (int i = 0; i < msg.length(); ++i) {
			char ch = msg.charAt(i);
			len += charWidths[ch];
		}
		return len * (fontSize / glyphSize);
	}

	@Override
	public void setSize(float fontSize) {
		this.fontSize = fontSize;
	}

	@Override
	public float getSize() {
		return fontSize;
	}

	@Override
	public void dispose() {
		texture.dispose();
	}

}
