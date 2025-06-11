package net.orin.graphics.g3d;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import net.orin.graphics.Camera;
import net.orin.math.Mathf;
import net.orin.math.g3d.Frustum;

public class Camera3D implements Camera<Vector3f> {

    private final Vector3f position = new Vector3f();

    private float yaw;
    private float pitch;

    private final Vector3f up = new Vector3f(0f, 1f, 0f);
    private final Vector3f forward = new Vector3f();
    private final Vector3f right = new Vector3f();
    private final Vector3f target = new Vector3f();

    private final Matrix4f projectionMatrix = new Matrix4f();
    private final Matrix4f viewMatrix = new Matrix4f();
    private final Matrix4f combinedMatrix = new Matrix4f();

    private final Frustum frustum = new Frustum();
    
    public Camera3D(float fovy, float aspect, float near, float far) {
        projectionMatrix.perspective(Mathf.toRadians(fovy), aspect, near, far);
    }

    @Override
    public void update() {
        float pitchRad = Mathf.toRadians(pitch);
        float yawRad = Mathf.toRadians(yaw);

        forward.x = Mathf.cos(pitchRad) * Mathf.sin(yawRad);
        forward.y = Mathf.sin(pitchRad);
        forward.z = -Mathf.cos(pitchRad) * Mathf.cos(yawRad);
        forward.normalize();

        right.set(forward).cross(UP).normalize();
        up.set(right).cross(forward).normalize();

        target.set(position).add(forward);

        viewMatrix.identity().lookAt(position, target, up);
        projectionMatrix.mul(viewMatrix, combinedMatrix);
        
        frustum.update(combinedMatrix);
    }

    @Override
    public Vector3f getPosition() {
        return position;
    }

    @Override
    public void setPosition(Vector3f position) {
        this.position.set(position);
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }

    @Override
    public void addPosition(Vector3f position) {
        this.position.add(position);
    }

    public void addPosition(float x, float y, float z) {
        this.position.add(x, y, z);
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

    public void addYaw(float yaw) {
        setYaw(this.yaw + yaw);
    }

    public void addPitch(float pitch) {
        setPitch(this.pitch + pitch);
    }

    public void setPitch(float pitch) {
        this.pitch = Mathf.clamp(pitch, -89.9f, 89.9f);
    }

    public void setYaw(float yaw) {
        this.yaw = Mathf.mod(yaw, 0f, 360f);
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public Vector3f getForward() {
        return forward;
    }

    public Vector3f getRight() {
        return right;
    }

    public Vector3f getUp() {
        return up;
    }

    public Frustum getFrustum() {
    	return frustum;
    }
    
    public float x() {
    	return position.x;
    }
    
    public float y() {
    	return position.y;
    }
    
    public float z() {
    	return position.z;
    }
    
}
