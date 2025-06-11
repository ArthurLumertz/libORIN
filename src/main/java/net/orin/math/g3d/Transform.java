package net.orin.math.g3d;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import net.orin.math.Mathf;

public class Transform {

	private final Vector3f position = new Vector3f();
	private final Vector3f scale = new Vector3f(1f, 1f, 1f);
	private final Vector3f rotation = new Vector3f();

	private final Matrix4f transMatrix = new Matrix4f();

	public Transform() {
	}

	public Transform(Vector3f position, Vector3f scale, Vector3f rotation) {
		set(position, scale, rotation);
	}

	public Transform set(Vector3f position, Vector3f scale, Vector3f rotation) {
		this.position.set(position);
		this.scale.set(scale);
		this.rotation.set(rotation);
		normalizeRot();
		return this;
	}

	public Transform mul(Transform other) {
		this.position.mul(other.position);
		this.scale.mul(other.scale);
		this.rotation.mul(other.rotation);
		return this;
	}

	public Transform set(Transform other) {
		return set(other.position, other.scale, other.rotation);
	}

	public Transform setPosition(float x, float y, float z) {
		position.set(x, y, z);
		return this;
	}

	public Transform addPosition(float x, float y, float z) {
		position.add(x, y, z);
		return this;
	}

	public Transform setPosition(Vector3f position) {
		this.position.set(position);
		return this;
	}

	public Transform addPosition(Vector3f position) {
		this.position.add(position);
		return this;
	}

	public Transform setScale(float x, float y, float z) {
		scale.set(x, y, z);
		return this;
	}

	public Transform setScale(Vector3f scale) {
		this.scale.set(scale);
		return this;
	}

	public Transform addScale(float x, float y, float z) {
		this.scale.add(x, y, z);
		return this;
	}

	public Transform addScale(Vector3f scale) {
		this.scale.add(scale);
		return this;
	}

	public Transform setRotation(float x, float y, float z) {
		rotation.set(x, y, z);
		normalizeRot();
		return this;
	}

	public Transform setRotation(Vector3f rotation) {
		this.rotation.set(rotation);
		normalizeRot();
		return this;
	}

	public Transform addRotation(float x, float y, float z) {
		this.rotation.add(x, y, z);
		normalizeRot();
		return this;
	}

	public Transform addRotation(Vector3f scale) {
		this.rotation.add(scale);
		normalizeRot();
		return this;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getScale() {
		return scale;
	}

	public Vector3f getRotation() {
		return rotation;
	}
	
	private void normalizeRot() {
		rotation.x = Mathf.mod(rotation.x, 0f, 360f);
		rotation.y = Mathf.mod(rotation.y, 0f, 360f);
		rotation.z = Mathf.mod(rotation.z, 0f, 360f);
	}

	public Matrix4f getMatrix() {
		transMatrix.identity().translate(position)
				.rotateXYZ(Mathf.toRadians(rotation.x), Mathf.toRadians(rotation.y), Mathf.toRadians(rotation.z))
				.scale(scale);
		return transMatrix;
	}
	
	public Matrix4f getMatrix(Matrix4f dest) {
		return dest.identity().translate(position)
				.rotateXYZ(Mathf.toRadians(rotation.x), Mathf.toRadians(rotation.y), Mathf.toRadians(rotation.z))
				.scale(scale);
	}

	@Override
	public String toString() {
		return "Transform{" + "position=" + position + ", scale=" + scale + ", rotation=" + rotation + ", transMatrix="
				+ transMatrix + '}';
	}

}
