package net.orin.graphics.g3d.environment;

import net.orin.graphics.Color;

@SuppressWarnings("unchecked")
public abstract class Light<T extends Light<T>> {

	protected final Color color = new Color();
	
	public T setColor (float r, float g, float b, float a) {
		this.color.set(r, g, b, a);
		return (T)this;
	}

	public T setColor (Color color) {
		this.color.set(color);
		return (T)this;
	}
	
	public Color getColor() {
		return color;
	}
	
	
}
