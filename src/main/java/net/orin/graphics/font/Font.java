package net.orin.graphics.font;

import net.orin.graphics.Color;
import net.orin.util.Disposable;

public interface Font extends Disposable {

	void draw(String msg, float x, float y, Color color, boolean darken);
	void drawShadow(String msg, float x, float y, Color color);
	
	float getWidth(String msg);
	void setSize(float fontSize);
	float getSize();
	
}
