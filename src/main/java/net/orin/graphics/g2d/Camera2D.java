package net.orin.graphics.g2d;

import net.orin.graphics.Camera;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Camera2D implements Camera<Vector2f> {

    private final float viewportWidth;
    private final float viewportHeight;

    private final Vector2f position = new Vector2f();

    private final Matrix4f projectionMatrix = new Matrix4f();
    private final Matrix4f viewMatrix = new Matrix4f();
    private final Matrix4f combinedMatrix = new Matrix4f();

    public Camera2D(float viewportWidth, float viewportHeight) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.projectionMatrix.ortho2D(0f, viewportWidth, 0f, viewportHeight);
    }

    @Override
    public void update() {
        viewMatrix.identity().translate(-position.x, -position.y, 0f);
        projectionMatrix.mul(viewMatrix, combinedMatrix);
    }

    @Override
    public Vector2f getPosition() {
        return position;
    }

    @Override
    public void setPosition(Vector2f position) {
        this.position.set(position);
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    @Override
    public void addPosition(Vector2f position) {
        this.position.add(position);
    }

    public void addPosition(float x, float y) {
        this.position.add(x, y);
    }

    @Override
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    @Override
    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    @Override
    public Matrix4f getCombinedMatrix() {
        return combinedMatrix;
    }

    public float getViewportWidth() {
        return viewportWidth;
    }

    public float getViewportHeight() {
        return viewportHeight;
    }
}
