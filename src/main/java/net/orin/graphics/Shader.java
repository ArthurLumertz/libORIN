package net.orin.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import net.orin.io.FileRef;
import net.orin.util.Disposable;

public class Shader implements Disposable {

	private final FloatBuffer matrixBuffer = MemoryUtil.memAllocFloat(16);

	private int programId;
	
	private Map<String, Integer> uniformLocations = new HashMap<>();

	public Shader(String vertexSource, String fragmentSource) {
		int vertexId = createShader(vertexSource, GL_VERTEX_SHADER);
		int fragmentId = createShader(fragmentSource, GL_FRAGMENT_SHADER);

		programId = glCreateProgram();
		glAttachShader(programId, vertexId);
		glAttachShader(programId, fragmentId);

		glLinkProgram(programId);
		if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
			System.err.println("Failed to link program!\n" + glGetProgramInfoLog(programId));
		}

		glValidateProgram(programId);
		if (glGetProgrami(programId, GL_VALIDATE_STATUS) == GL_FALSE) {
			System.err.println("Failed to validate program!\n" + glGetProgramInfoLog(programId));
		}

		glDeleteShader(vertexId);
		glDeleteShader(fragmentId);
	}

	public Shader(FileRef vertexFile, FileRef fragmentFile) {
		this(vertexFile.readString(), fragmentFile.readString());
	}

	private int createShader(String source, int type) {
		int shaderId = glCreateShader(type);
		glShaderSource(shaderId, source);
		glCompileShader(shaderId);
		if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
			String shaderType = type == GL_VERTEX_SHADER ? "vertex" : "fragment";
			System.err.println("Failed to compile " + shaderType + " shader!\n" + glGetShaderInfoLog(shaderId));
		}
		return shaderId;
	}

	public void bind() {
		glUseProgram(programId);
	}

	public void unbind() {
		glUseProgram(0);
	}

	@Override
	public void dispose() {
		glDeleteProgram(programId);
		MemoryUtil.memFree(matrixBuffer);
	}

	public int getUniformLocation(String uniformName) {
		Integer cachedLocation = uniformLocations.get(uniformName);
		if (cachedLocation != null) return cachedLocation;
		
		int location = glGetUniformLocation(programId, uniformName);
		if (location == -1) {
			System.err.println("Failed to find uniform location: '" + uniformName + "'!");
		}
		uniformLocations.put(uniformName, location);
		return location;
	}

	public void setUniform1b(String uniformName, boolean bool) {
		int location = getUniformLocation(uniformName);
		glUniform1i(location, bool ? 1 : 0);
	}

	public void setUniform1i(String uniformName, int value) {
		int location = getUniformLocation(uniformName);
		glUniform1i(location, value);
	}

	public void setUniform2i(String uniformName, int x, int y) {
		int location = getUniformLocation(uniformName);
		glUniform2i(location, x, y);
	}

	public void setUniform3i(String uniformName, int x, int y, int z) {
		int location = getUniformLocation(uniformName);
		glUniform3i(location, x, y, z);
	}

	public void setUniform4i(String uniformName, int x, int y, int z, int w) {
		int location = getUniformLocation(uniformName);
		glUniform4i(location, x, y, z, w);
	}

	public void setUniform1f(String uniformName, float value) {
		int location = getUniformLocation(uniformName);
		glUniform1f(location, value);
	}

	public void setUniform2f(String uniformName, float x, float y) {
		int location = getUniformLocation(uniformName);
		glUniform2f(location, x, y);
	}

	public void setUniform3f(String uniformName, float x, float y, float z) {
		int location = getUniformLocation(uniformName);
		glUniform3f(location, x, y, z);
	}

	public void setUniform4f(String uniformName, float x, float y, float z, float w) {
		int location = getUniformLocation(uniformName);
		glUniform4f(location, x, y, z, w);
	}

	public void setUniformMatrix(String uniformMatrix, Matrix4f matrix) {
		int location = getUniformLocation(uniformMatrix);
		glUniformMatrix4fv(location, false, matrix.get(matrixBuffer));
	}

	public void setUniform1b(int location, boolean bool) {
		glUniform1i(location, bool ? 1 : 0);
	}

	public void setUniform1i(int location, int value) {
		glUniform1i(location, value);
	}

	public void setUniform2i(int location, int x, int y) {
		glUniform2i(location, x, y);
	}

	public void setUniform3i(int location, int x, int y, int z) {
		glUniform3i(location, x, y, z);
	}

	public void setUniform4i(int location, int x, int y, int z, int w) {
		glUniform4i(location, x, y, z, w);
	}

	public void setUniform1f(int location, float value) {
		glUniform1f(location, value);
	}

	public void setUniform2f(int location, float x, float y) {
		glUniform2f(location, x, y);
	}

	public void setUniform3f(int location, float x, float y, float z) {
		glUniform3f(location, x, y, z);
	}

	public void setUniform4f(int location, float x, float y, float z, float w) {
		glUniform4f(location, x, y, z, w);
	}

	public void setUniformMatrix(int location, Matrix4f matrix) {
		glUniformMatrix4fv(location, false, matrix.get(matrixBuffer));
	}

}
