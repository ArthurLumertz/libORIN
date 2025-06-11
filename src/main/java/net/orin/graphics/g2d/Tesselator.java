package net.orin.graphics.g2d;

import net.orin.graphics.Shader;
import net.orin.util.Disposable;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

class Tesselator implements Disposable {

    private static final int MAX_VERTICES = 10000;

    private final FloatBuffer vertexBuffer = MemoryUtil.memAllocFloat(MAX_VERTICES * 2);
    private final FloatBuffer textureCoordinateBuffer = MemoryUtil.memAllocFloat(MAX_VERTICES * 2);
    private final FloatBuffer colorBuffer = MemoryUtil.memAllocFloat(MAX_VERTICES * 4);

    private int vertices = 0;

    private boolean hasTexture = false;
    private float u;
    private float v;

    private boolean hasColor = false;
    private float r;
    private float g;
    private float b;
    private float a;

    private final int vaoId;
    private final int vboId;
    private final int tboId;
    private final int cboId;

    private final Shader shader;
    private final boolean textured;

    public Tesselator(Shader shader, boolean textured) {
        this.textured = textured;

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, MAX_VERTICES * 2 * Float.BYTES, GL_DYNAMIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        tboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, tboId);
        glBufferData(GL_ARRAY_BUFFER, MAX_VERTICES * 2 * Float.BYTES, GL_DYNAMIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);

        cboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, cboId);
        glBufferData(GL_ARRAY_BUFFER, MAX_VERTICES * 4 * Float.BYTES, GL_DYNAMIC_DRAW);
        glVertexAttribPointer(2, 4, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(2);

        glBindVertexArray(0);

        this.shader = shader;
    }

    public void init() {
        clear();
    }

    public void clear() {
        vertexBuffer.clear();
        textureCoordinateBuffer.clear();
        colorBuffer.clear();
        vertices = 0;
        hasTexture = false;
        hasColor = false;
    }

    public void flush() {
        if (vertices > 0) {
            glBindVertexArray(vaoId);

            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertexBuffer);

            if (hasTexture) {
                glBindBuffer(GL_ARRAY_BUFFER, tboId);
                glBufferSubData(GL_ARRAY_BUFFER, 0, textureCoordinateBuffer);
            }
            if (hasColor) {
                glBindBuffer(GL_ARRAY_BUFFER, cboId);
                glBufferSubData(GL_ARRAY_BUFFER, 0, colorBuffer);
            }

            if (textured) {
                shader.setUniform1b("u_hasTexture", hasTexture);
            }

            glDrawArrays(GL_TRIANGLES, 0, vertices);

            glBindVertexArray(0);

            clear();
        }
    }

    public void vertex(float x, float y) {
        vertexBuffer.put(vertices * 2, x);
        vertexBuffer.put(vertices * 2 + 1, y);

        if (hasTexture) {
            textureCoordinateBuffer.put(vertices * 2, u);
            textureCoordinateBuffer.put(vertices * 2 + 1, v);
        }
        if (hasColor) {
            colorBuffer.put(vertices * 4, r);
            colorBuffer.put(vertices * 4 + 1, g);
            colorBuffer.put(vertices * 4 + 2, b);
            colorBuffer.put(vertices * 4 + 3, a);
        }

        vertices++;
        if (vertices >= MAX_VERTICES) {
            flush();
        }
    }

    public void tex(float u, float v) {
        this.hasTexture = true;
        this.u = u;
        this.v = v;
    }

    public void color(float r, float g, float b, float a) {
        this.hasColor = true;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public void vertexUV(float x, float y, float u, float v) {
        tex(u, v);
        vertex(x, y);
    }

    @Override
    public void dispose() {
        MemoryUtil.memFree(vertexBuffer);
        MemoryUtil.memFree(textureCoordinateBuffer);
        MemoryUtil.memFree(colorBuffer);
    }

    public boolean hasTexture() {
        return hasTexture;
    }

}
