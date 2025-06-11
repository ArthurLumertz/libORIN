package net.orin.graphics.g2d;

import net.orin.graphics.Color;
import net.orin.graphics.Shader;
import net.orin.math.Mathf;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class ShapeBatch implements Batch {

	private Tesselator t;
	private Shader shader;
	private Matrix4f combinedMatrix;

	public ShapeBatch(Shader shader) {
		this.shader = (shader == null) ? createDefaultShader() : shader;
		this.t = new Tesselator(this.shader, false);
	}

	private Shader createDefaultShader() {
		String vertexShaderSource = "#version 330 core\n" + "\n" + "layout(location = 0) in vec2 a_pos;\n"
				+ "layout(location = 2) in vec4 a_color;\n" + "\n" + "uniform mat4 u_combMatrix;\n" + "\n"
				+ "out vec4 v_color;\n" + "\n" + "void main() {\n"
				+ "    gl_Position = u_combMatrix * vec4(a_pos, 0.0, 1.0);\n" + "    v_color = a_color;\n" + "}\n";

		String fragmentShaderSource = "#version 330 core\n" + "\n" + "out vec4 FragColor;\n" + "\n"
				+ "in vec4 v_color;\n" + "\n" + "void main() {\n" + "    vec4 finalColor = v_color;\n" + "\n"
				+ "    FragColor = finalColor;\n" + "}\n";

		return new Shader(vertexShaderSource, fragmentShaderSource);
	}

	public ShapeBatch() {
		this(null);
	}

	@Override
	public void setColor(float r, float g, float b, float a) {
		t.color(r, g, b, a);
	}

	@Override
	public void setColor(Color color) {
		t.color(color.r, color.g, color.b, color.a);
	}

	@Override
	public void begin() {
		shader.bind();
		shader.setUniformMatrix("u_combMatrix", combinedMatrix);

		t.init();
	}

	@Override
	public void end() {
		t.flush();
	}

	public void drawQuad(float x, float y, float width, float height, Color color) {
		setColor(color);

		t.vertex(x, y);
		t.vertex(x + width, y + height);
		t.vertex(x, y + height);

		t.vertex(x + width, y + height);
		t.vertex(x + width, y);
		t.vertex(x, y);
	}

	public void drawQuad(Vector2f position, Vector2f size, Color color) {
		drawQuad(position.x, position.y, size.x, size.y, color);
	}

	public void drawEllipse(float x, float y, float width, float height, Color color) {
		int segments = 32;
		float centerX = x + width / 2f;
		float centerY = y + height / 2f;
		float radiusX = width / 2f;
		float radiusY = height / 2f;

		setColor(color);

		for (int i = 0; i < segments; i++) {
			float angle1 = Mathf.PI2 * i / segments;
			float angle2 = Mathf.PI2 * (i + 1) / segments;

			float x1 = centerX + Mathf.cos(angle1) * radiusX;
			float y1 = centerY + Mathf.sin(angle1) * radiusY;
			float x2 = centerX + Mathf.cos(angle2) * radiusX;
			float y2 = centerY + Mathf.sin(angle2) * radiusY;

			t.vertex(centerX, centerY);
			t.vertex(x1, y1);
			t.vertex(x2, y2);
		}
	}

	public void drawEllipse(Vector2f position, Vector2f size, Color color) {
		drawEllipse(position.x, position.y, size.x, size.y, color);
	}

	public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, Color color) {
		setColor(color);

		t.vertex(x1, y1);
		t.vertex(x2, y2);
		t.vertex(x3, y3);
	}

	public void drawTriangle(Vector2f a, Vector2f b, Vector2f c, Color color) {
		drawTriangle(a.x, a.y, b.x, b.y, c.x, c.y, color);
	}

	public void drawPolygon(float x, float y, float radius, int sides, Color color) {
		if (sides < 3)
			return;

		setColor(color);

		for (int i = 0; i < sides; i++) {
			float angle1 = Mathf.PI2 * i / sides;
			float angle2 = Mathf.PI2 * (i + 1) / sides;

			float x1 = x + Mathf.cos(angle1) * radius;
			float y1 = y + Mathf.sin(angle1) * radius;
			float x2 = x + Mathf.cos(angle2) * radius;
			float y2 = y + Mathf.sin(angle2) * radius;

			t.vertex(x, y);
			t.vertex(x1, y1);
			t.vertex(x2, y2);
		}
	}

	public void drawPolygon(Vector2f position, float radius, int sides, Color color) {
		drawPolygon(position.x, position.y, radius, sides, color);
	}

	public void drawRoundedQuad(float x, float y, float width, float height, float borderRadius, Color color) {
		float radius = Math.min(borderRadius, Math.min(width / 2f, height / 2f));

		float cx = x + radius;
		float cy = y + radius;
		float ex = x + width - radius;
		float ey = y + height - radius;

		setColor(color);

		drawQuad(x + radius, y + radius, width - radius * 2, height - radius * 2, color);

		drawQuad(cx, y, ex - cx, radius, color);
		drawQuad(cx, y + height - radius, ex - cx, radius, color);
		drawQuad(x, cy, radius, ey - cy, color);
		drawQuad(x + width - radius, cy, radius, ey - cy, color);

		drawCorner(cx, cy, radius, 180, 270);
		drawCorner(ex, cy, radius, 270, 360);
		drawCorner(ex, ey, radius, 0, 90);
		drawCorner(cx, ey, radius, 90, 180);
	}

	private void drawCorner(float cx, float cy, float radius, int angleStart, int angleEnd) {
		int segments = 8;
		float step = (angleEnd - angleStart) / (float) segments;

		for (int i = 0; i < segments; i++) {
			float angle1 = Mathf.toRadians(angleStart + i * step);
			float angle2 = Mathf.toRadians(angleStart + (i + 1) * step);

			float x1 = cx + Mathf.cos(angle1) * radius;
			float y1 = cy + Mathf.sin(angle1) * radius;
			float x2 = cx + Mathf.cos(angle2) * radius;
			float y2 = cy + Mathf.sin(angle2) * radius;

			t.vertex(cx, cy);
			t.vertex(x1, y1);
			t.vertex(x2, y2);
		}
	}

	@Override
	public void setCombinedMatrix(Matrix4f combinedMatrix) {
		this.combinedMatrix = combinedMatrix;
	}

	@Override
	public Matrix4f getCombinedMatrix() {
		return combinedMatrix;
	}

	@Override
	public Shader getShader() {
		return shader;
	}

	@Override
	public void dispose() {
		t.dispose();
	}

}
