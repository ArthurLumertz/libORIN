package net.orin.graphics.g3d;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Matrix4f;

import net.orin.graphics.Shader;
import net.orin.graphics.g3d.mesh.Mesh;
import net.orin.graphics.g3d.mesh.TexturedMesh;
import net.orin.io.FileRef;
import net.orin.util.Disposable;

public class Renderer implements Disposable {

	private Shader shader;
	private Matrix4f combinedMatrix;

	public Renderer() {
		this(null);
	}

	public Renderer(Shader shader) {
		this.shader = (shader == null) ? createDefaultShader() : shader;

		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
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

	private Shader createDefaultShader() {
		return new Shader(FileRef.internal("default.vert"), FileRef.internal("default.frag"));
	}

	@Override
	public void dispose() {
		shader.dispose();
	}

	public void begin() {
		glEnable(GL_MULTISAMPLE);
		
		shader.bind();
		if (combinedMatrix != null) {			
			shader.setUniformMatrix("u_combMatrix", combinedMatrix);
		}
	}

	public void end() {
		shader.unbind();
		glDisable(GL_MULTISAMPLE);
	}

	public void setCombinedMatrix(Matrix4f combinedMatrix) {
		this.combinedMatrix = combinedMatrix;
	}

	public void drawMesh(Mesh mesh, Matrix4f transformMatrix) {
		if (transformMatrix != null) {
			shader.setUniformMatrix("u_transMatrix", transformMatrix);
		}

		if (mesh instanceof TexturedMesh) {
			TexturedMesh msh = (TexturedMesh) mesh;
			msh.getTexture().bind();

			shader.setUniform1b("useTexture", true);
		}

		glBindVertexArray(mesh.getVaoId());
		glDrawElements(GL_TRIANGLES, mesh.getIndexCount(), GL_UNSIGNED_INT, 0);
		glBindVertexArray(0);
		
		shader.setUniform1b("useTexture", false);
	}

	public Shader getShader() {
		return shader;
	}
	
	public Matrix4f getCombinedMatrix() {
		return combinedMatrix;
	}

//    public Camera3D getCamera() {
//        return camera;
//    }

}