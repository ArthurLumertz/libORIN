package net.orin.graphics.g3d.mesh;

import net.orin.util.Disposable;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh implements Disposable {

    private int vaoId;
    private int vboId;
    private int tboId;
    private int nboId;
    private int eboId;

    private int indexCount;

    public Mesh() {
    }

    public Mesh(Mesh other) {
        vaoId = other.vaoId;
        vboId = other.vboId;
        tboId = other.tboId;
        nboId = other.nboId;
        eboId = other.eboId;
        indexCount = other.indexCount;
    }

    public Mesh(float[] vertices, float[] texCoords, float[] normals, int[] indices) {
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = storeDataInAttributeList(0, 3, vertices);
        tboId = storeDataInAttributeList(1, 2, texCoords);
        nboId = storeDataInAttributeList(2, 3, normals);

        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(buffer);

        glBindVertexArray(0);

        indexCount = indices.length;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getIndexCount() {
        return indexCount;
    }

    @Override
    public void dispose() {
        glDeleteVertexArrays(vaoId);
        glDeleteBuffers(vboId);
        glDeleteBuffers(tboId);
        glDeleteBuffers(nboId);
        glDeleteBuffers(eboId);
    }

    private int storeDataInAttributeList(int index, int size, float[] data) {
        if (data.length == 0) return 0;

        int vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(buffer);
        glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(index);
        return vboId;
    }

    private static FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private static IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public Mesh copy() {
        return new Mesh(this);
    }

    @Override
    public String toString() {
        return "Mesh{" +
                "indexCount=" + indexCount +
                '}';
    }
}
