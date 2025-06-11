package net.orin.graphics.g3d.mesh;

import org.joml.Vector3f;

import net.orin.math.g3d.Transform;

public class SubMesh {

	private final Mesh mesh;
	private final Transform transform;

	public SubMesh(Mesh mesh) {
		this.mesh = mesh;
		this.transform = new Transform();
	}

	public SubMesh(Mesh mesh, Transform transform) {
		this.mesh = mesh;
		this.transform = new Transform();
		this.transform.set(transform);
	}

	public Mesh getMesh() {
		return mesh;
	}

	public Transform getTransform() {
		return transform;
	}

	public SubMesh setPosition(float x, float y, float z) {
		transform.setPosition(x, y, z);
		return this;
	}
	
	public SubMesh setPosition(Vector3f position) {
		transform.setPosition(position);
		return this;
	}
	
	public SubMesh setRotation(float x, float y, float z) {
		transform.setRotation(x, y, z);
		return this;
	}
	
	public SubMesh setRotation(Vector3f position) {
		transform.setRotation(position);
		return this;
	}

	public SubMesh addPosition(float x, float y, float z) {
		transform.addPosition(x, y, z);
		return this;
	}
	
	public SubMesh addPosition(Vector3f position) {
		transform.addPosition(position);
		return this;
	}
	
	public SubMesh addRotation(float x, float y, float z) {
		transform.addRotation(x, y, z);
		return this;
	}
	
	public SubMesh addRotation(Vector3f position) {
		transform.addRotation(position);
		return this;
	}
	
}