package net.orin.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface Camera<T>  {

    Vector3f UP = new Vector3f(0f, 1f, 0f);

    void update();

    T getPosition();
    void setPosition(T position);
    void addPosition(T position);

    Matrix4f getProjectionMatrix();
    Matrix4f getViewMatrix();
    Matrix4f getCombinedMatrix();

}
