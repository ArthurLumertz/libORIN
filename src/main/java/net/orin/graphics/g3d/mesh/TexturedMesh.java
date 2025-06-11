package net.orin.graphics.g3d.mesh;

import net.orin.graphics.g2d.texture.Texture;
import net.orin.graphics.g2d.texture.Texture.Filter;
import net.orin.io.FileRef;

public class TexturedMesh extends Mesh {

	private Texture texture;

	public TexturedMesh() {
	}

	public TexturedMesh(TexturedMesh other) {
		this(other.texture, other);
	}

	public TexturedMesh(Texture texture, Mesh mesh) {
		super(mesh);
		this.texture = texture;
	}
	
	public TexturedMesh(FileRef ref, Mesh mesh) {
		this(new Texture(ref, Filter.NEAREST), mesh);
	}
	
	public TexturedMesh(String textureFile, Mesh mesh) {
		this(FileRef.internal(textureFile), mesh);
	}

	public Texture getTexture() {
		return texture;
	}

	@Override
	public TexturedMesh copy() {
		return new TexturedMesh(this);
	}

	@Override
	public void dispose() {
		texture.dispose();
		super.dispose();
	}

}
