package net.orin.graphics.g2d;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import net.orin.graphics.Camera;

public class Camera2D implements Camera<Vector2f> {

    private final float viewportWidth;
    private final float viewportHeight;

    private final Vector2f position = new Vector2f();

    private final Matrix4f projectionMatrix = new Matrix4f();
    private final Matrix4f viewMatrix = new Matrix4f();
    private final Matrix4f combinedMatrix = new Matrix4f();
    private final Matrix4f inverseCombinedMatrix = new Matrix4f();

    private final Vector4f worldPos = new Vector4f();
    private final Vector4f projected = new Vector4f();
    private final Vector2f screenPos = new Vector2f();
    private final Vector4f screenPos2 = new Vector4f();
    private final Vector2f tmp2f = new Vector2f();
    
    public Camera2D(float viewportWidth, float viewportHeight) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.projectionMatrix.ortho2D(0f, viewportWidth, 0f, viewportHeight);
    }

    @Override
    public void update() {
        viewMatrix.identity().translate(-position.x, -position.y, 0f);
        projectionMatrix.mul(viewMatrix, combinedMatrix);
        combinedMatrix.invert(inverseCombinedMatrix);
    }
    
    public Vector2f project(float worldX, float worldY) {
        worldPos.set(worldX, worldY, 0f, 1f);
        projected.zero();

        combinedMatrix.transform(worldPos, projected);
        projected.div(projected.w);

        float screenX = (projected.x + 1f) / 2f * viewportWidth;
        float screenY = (projected.y + 1f) / 2f * viewportHeight;

        return tmp2f.set(screenX, screenY);
    }
    
    public Vector2f unproject(float screenX, float screenY) {
        float ndcX = (2f * screenX) / viewportWidth - 1f;
        float ndcY = (2f * screenY) / viewportHeight - 1f;

        screenPos2.set(ndcX, ndcY, 0f, 1f);
        worldPos.zero();

        inverseCombinedMatrix.transform(screenPos2, worldPos);
        worldPos.div(worldPos.w);

        return tmp2f.set(worldPos.x, worldPos.y);
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
