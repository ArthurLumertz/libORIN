package net.orin.graphics;

import net.orin.math.Mathf;

public class Color {

	public static final Color tmp = new Color();
	public static final Color tmp2 = new Color();

	public static final Color WHITE = new Color(0xFFFFFFFF);
	public static final Color BLACK = new Color(0x000000FF);
	public static final Color RED = new Color(0xFF0000FF);
	public static final Color GREEN = new Color(0x00FF00FF);
	public static final Color BLUE = new Color(0x0000FFFF);
	public static final Color YELLOW = new Color(0xFFFF00FF);
	public static final Color CYAN = new Color(0x00FFFFFF);
	public static final Color MAGENTA = new Color(0xFF00FFFF);
	public static final Color TRANSPARENT = new Color(0x00000000);
	public static final Color GRAY = new Color(0x808080FF);
	public static final Color LIGHT_GRAY = new Color(0xD3D3D3FF);
	public static final Color DARK_GRAY = new Color(0x404040FF);
	public static final Color ORANGE = new Color(0xFFA500FF);
	public static final Color PINK = new Color(0xFFC0CBFF);

	public float r;
	public float g;
	public float b;
	public float a;

	public Color() {
	}

	public Color(float r, float g, float b, float a) {
		this.r = Mathf.clamp(r, 0f, 1f);
		this.g = Mathf.clamp(g, 0f, 1f);
		this.b = Mathf.clamp(b, 0f, 1f);
		this.a = Mathf.clamp(a, 0f, 1f);
	}

	public Color(float r, float g, float b) {
		this(r, g, b, 1f);
	}

	public Color(int hex) {
		this.r = ((hex >> 24) & 0xFF) / 255f;
		this.g = ((hex >> 16) & 0xFF) / 255f;
		this.b = ((hex >> 8) & 0xFF) / 255f;
		this.a = (hex & 0xFF) / 255f;
	}

	public Color set(float r, float g, float b, float a) {
		this.r = Mathf.clamp(r, 0f, 1f);
		this.g = Mathf.clamp(g, 0f, 1f);
		this.b = Mathf.clamp(b, 0f, 1f);
		this.a = Mathf.clamp(a, 0f, 1f);
		return this;
	}

	public Color set(int hex) {
		r = ((hex >> 24) & 0xFF) / 255f;
		g = ((hex >> 16) & 0xFF) / 255f;
		b = ((hex >> 8) & 0xFF) / 255f;
		a = (hex & 0xFF) / 255f;
		return this;
	}

	public Color set(Color other) {
		r = other.r;
		g = other.g;
		b = other.b;
		a = other.a;
		return this;
	}

	public Color lerp(Color color, float t) {
		r = Mathf.lerp(r, color.r, t);
		g = Mathf.lerp(g, color.g, t);
		b = Mathf.lerp(b, color.b, t);
		a = Mathf.lerp(a, color.a, t);
		return this;
	}

	public int toHex() {
		int ri = (int) (r * 255f) & 0xFF;
		int gi = (int) (g * 255f) & 0xFF;
		int bi = (int) (b * 255f) & 0xFF;
		int ai = (int) (a * 255f) & 0xFF;
		return (ri << 24) | (gi << 16) | (bi << 8) | ai;
	}

	@Override
	public int hashCode() {
		int result = Float.hashCode(r);
		result = 31 * result + Float.hashCode(g);
		result = 31 * result + Float.hashCode(b);
		result = 31 * result + Float.hashCode(a);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Color color = (Color) o;
		return Float.compare(r, color.r) == 0 && Float.compare(g, color.g) == 0 && Float.compare(b, color.b) == 0
				&& Float.compare(a, color.a) == 0;
	}

	public Color copy() {
		return new Color(r, g, b, a);
	}

}
