package net.orin.graphics.g2d.texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import net.orin.io.FileRef;
import net.orin.util.Disposable;

public class Texture implements Disposable {

	private int id;
	private int width;
	private int height;
	private int channels;

	public Texture() {
	}

	public Texture(String fileName) {
		this(fileName, Filter.NEAREST);
	}

	public Texture(String fileName, Filter filter) {
		this(FileRef.internal(fileName), filter);
	}

	public Texture(FileRef ref, Filter filter) {
		this(new Pixmap(ref), filter);
	}

	public Texture(Pixmap pixmap, Filter filter) {
		width = pixmap.getWidth();
		height = pixmap.getHeight();
		channels = pixmap.getChannels();

		int glFilter = filter == Filter.NEAREST ? GL_NEAREST : GL_LINEAR;

		id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, glFilter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, glFilter);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixmap.getPixels());
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public void bind(int sampler) {
		glActiveTexture(GL_TEXTURE0 + sampler); 
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void unbind(int sampler) {
		glActiveTexture(GL_TEXTURE0 + sampler);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	@Override
	public void dispose() {
		glDeleteTextures(id);
	}

	public int getId() {
		return id;
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

	public enum Filter {
		NEAREST, LINEAR
	}

}
