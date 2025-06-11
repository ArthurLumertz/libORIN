package net.orin.graphics.g2d;

import net.orin.graphics.Color;
import net.orin.graphics.Shader;
import net.orin.graphics.g2d.texture.Texture;
import net.orin.graphics.g2d.texture.TextureRegion;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4i;

import static org.lwjgl.opengl.GL11.*;

public class TextureBatch implements Batch {

	private Tesselator t;

	private Matrix4f combinedMatrix;

	private Shader shader;

	public TextureBatch() {
		this(null);
	}

	public TextureBatch(Shader shader) {
		this.shader = (shader == null) ? createDefaultShader() : shader;
		this.t = new Tesselator(this.shader, true);
		enableTextures();
		enableBlending();
	}

	private Shader createDefaultShader() {
		String vertexShaderSource = "#version 330 core\n" + "\n" + "layout(location = 0) in vec2 a_pos;\n"
				+ "layout(location = 1) in vec2 a_texCoord;\n" + "layout(location = 2) in vec4 a_color;\n" + "\n"
				+ "uniform mat4 u_combMatrix;\n" + "\n" + "out vec2 v_texCoord;\n" + "out vec4 v_color;\n" + "\n"
				+ "void main() {\n" + "    gl_Position = u_combMatrix * vec4(a_pos, 0.0, 1.0);\n"
				+ "    v_texCoord = a_texCoord;\n" + "    v_color = a_color;\n" + "}\n";

		String fragmentShaderSource = "#version 330 core\n" + "\n" + "out vec4 FragColor;\n" + "\n"
				+ "in vec2 v_texCoord;\n" + "in vec4 v_color;\n" + "\n" + "uniform sampler2D u_sampler;\n"
				+ "uniform bool u_hasTexture;\n" + "\n" + "void main() {\n" + "    vec4 finalColor = v_color;\n" + "\n"
				+ "    if (u_hasTexture) {\n" + "        finalColor *= texture(u_sampler, v_texCoord);\n" + "    }\n"
				+ "\n" + "    FragColor = finalColor;\n" + "}\n";

		return new Shader(vertexShaderSource, fragmentShaderSource);
	}

	@Override
	public void setCombinedMatrix(Matrix4f combinedMatrix) {
		this.combinedMatrix = combinedMatrix;
	}

	@Override
	public void begin() {
		enableTextures();
		enableBlending();

		shader.bind();
		shader.setUniformMatrix("u_combMatrix", combinedMatrix);
		t.init();
	}

	@Override
	public void end() {
		t.flush();
		shader.unbind();

		disableTextures();
		disableBlending();
	}

	public void enableTextures() {
		glEnable(GL_TEXTURE_2D);
	}

	public void enableBlending() {
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	public void disableTextures() {
		glDisable(GL_TEXTURE_2D);
	}

	public void disableBlending() {
		glDisable(GL_BLEND);
	}

	@Override
	public void setColor(float r, float g, float b, float a) {
		t.color(r, g, b, a);
	}

	@Override
	public void setColor(Color color) {
		t.color(color.r, color.g, color.b, color.a);
	}

	public void drawTexture(Texture texture, float x, float y, float width, float height, Color color) {
		setColor(color);

		texture.bind();

		t.vertexUV(x, y, 0f, 0f);
		t.vertexUV(x + width, y, 1f, 0f);
		t.vertexUV(x + width, y + height, 1f, 1f);

		t.vertexUV(x + width, y + height, 1f, 1f);
		t.vertexUV(x, y + height, 0f, 1f);
		t.vertexUV(x, y, 0f, 0f);
	}

	public void drawTexture(Texture texture, Vector2f position, Vector2f size, Color color) {
		drawTexture(texture, position.x, position.y, size.x, size.y, color);
	}

	public void drawRegion(TextureRegion region, float x, float y, float width, float height, Color color) {
		setColor(color);

		region.getTexture().bind();

		t.vertexUV(x, y, region.getU0(), region.getV0());
		t.vertexUV(x + width, y, region.getU1(), region.getV0());
		t.vertexUV(x + width, y + height, region.getU1(), region.getV1());

		t.vertexUV(x + width, y + height, region.getU1(), region.getV1());
		t.vertexUV(x, y + height, region.getU0(), region.getV1());
		t.vertexUV(x, y, region.getU0(), region.getV0());
	}

	public void drawRegion(Texture texture, float x, float y, float width, float height, Vector4i region, Color color) {
		setColor(color);

		texture.bind();

		float tw = texture.getWidth();
		float th = texture.getHeight();
		float u0 = region.x / tw;
		float v1 = 1f - (region.y / th);
		float u1 = (region.x + region.z) / tw;
		float v0 = 1f - ((region.y + region.w) / th);

		t.vertexUV(x, y, u0, v0);
		t.vertexUV(x + width, y, u1, v0);
		t.vertexUV(x + width, y + height, u1, v1);

		t.vertexUV(x + width, y + height, u1, v1);
		t.vertexUV(x, y + height, u0, v1);
		t.vertexUV(x, y, u0, v0);
	}

	public void drawRegion(TextureRegion region, Vector2f position, Vector2f size, Color color) {
		drawRegion(region, position.x, position.y, size.x, size.y, color);
	}

	@Override
	public void dispose() {
		t.dispose();
	}

	@Override
	public Shader getShader() {
		return shader;
	}

	@Override
	public Matrix4f getCombinedMatrix() {
		return combinedMatrix;
	}

}
