package net.orin.graphics.g3d.mesh;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import net.orin.graphics.g3d.Renderer;
import net.orin.math.g3d.Transform;
import net.orin.util.Disposable;

public class MeshInstance implements Disposable {

	private Matrix4f tmp = new Matrix4f();

	private List<SubMesh> meshes;
	private Transform transform;

	public MeshInstance() {
		this(2);
	}
	
	public MeshInstance(int expected) {
		this.meshes = new ArrayList<>(expected);
		this.transform = new Transform();
	}

	public MeshInstance(MeshInstance other) {
		this(other.meshes.toArray(new Mesh[0]));
		set(other);
	}

	public MeshInstance addMesh(Mesh mesh) {
		meshes.add(new SubMesh(mesh));
		return this;
	}

	public MeshInstance addMesh(SubMesh mesh) {
		meshes.add(mesh);
		return this;
	}

	public MeshInstance(Mesh[] meshes) {
		this(meshes.length);
		for (int i = 0; i < meshes.length; i++) {
			addMesh(meshes[i]);
		}
	}

	public MeshInstance(Mesh mesh) {
		this(new Mesh[] { mesh });
	}

	public void draw(Renderer renderer) {
		for (int i = 0; i < meshes.size(); i++) {
			SubMesh mesh = meshes.get(i);

			tmp.set(mesh.getTransform().getMatrix(tmp));
			tmp.mul(transform.getMatrix());

			renderer.drawMesh(mesh.getMesh(), tmp);
		}
	}

	public MeshInstance set(MeshInstance other) {
		this.meshes = other.meshes;
		this.transform.set(other.transform);
		return this;
	}

	public MeshInstance setPosition(float x, float y, float z) {
		transform.setPosition(x, y, z);
		return this;
	}

	public MeshInstance setPosition(Vector3f position) {
		transform.setPosition(position);
		return this;
	}

	public MeshInstance addPosition(float x, float y, float z) {
		transform.addPosition(x, y, z);
		return this;
	}

	public MeshInstance addPosition(Vector3f position) {
		transform.addPosition(position);
		return this;
	}

	public MeshInstance setScale(float x, float y, float z) {
		transform.setScale(x, y, z);
		return this;
	}

	public MeshInstance setScale(Vector3f scale) {
		transform.setScale(scale);
		return this;
	}

	public MeshInstance addScale(float x, float y, float z) {
		transform.addScale(x, y, z);
		return this;
	}

	public MeshInstance addScale(Vector3f scale) {
		transform.addScale(scale);
		return this;
	}

	public MeshInstance setRotation(float x, float y, float z) {
		transform.setRotation(x, y, z);
		return this;
	}

	public MeshInstance setRotation(Vector3f rotation) {
		transform.setRotation(rotation);
		return this;
	}

	public MeshInstance addRotation(float x, float y, float z) {
		transform.addRotation(x, y, z);
		return this;
	}

	public MeshInstance addRotation(Vector3f rotation) {
		transform.addRotation(rotation);
		return this;
	}

	public MeshInstance setTransform(Transform other) {
		this.transform.set(other);
		return this;
	}

	public Transform getTransform() {
		return transform;
	}

	public MeshInstance copy() {
		return new MeshInstance(this);
	}

	public List<SubMesh> getMeshes() {
		return meshes;
	}

	@Override
	public void dispose() {
		for (int i = 0; i < meshes.size(); i++) {
			SubMesh mesh = meshes.get(i);
			mesh.getMesh().dispose();
		}
	}

	@Override
	public String toString() {
		return "MeshInstance{" + "meshes=" + meshes + ", transform=" + transform + '}';
	}
}
