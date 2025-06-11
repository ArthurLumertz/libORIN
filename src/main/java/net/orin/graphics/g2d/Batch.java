package net.orin.graphics.g2d;

import net.orin.graphics.Color;
import net.orin.graphics.Shader;
import net.orin.util.Disposable;
import org.joml.Matrix4f;

public interface Batch extends Disposable {

    void begin();

    void end();

    void setColor(Color color);

    void setColor(float r, float g, float b, float a);

    void setCombinedMatrix(Matrix4f getCombinedMatrix);

    Shader getShader();

    Matrix4f getCombinedMatrix();

}
