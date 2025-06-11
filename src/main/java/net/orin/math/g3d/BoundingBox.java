package net.orin.math.g3d;

import net.orin.math.Mathf;
import org.joml.Vector3f;

public class BoundingBox {

	public static final BoundingBox tmp = new BoundingBox();
	public static final BoundingBox tmp2 = new BoundingBox();

	public Vector3f min = new Vector3f();
	public Vector3f max = new Vector3f();

	public BoundingBox() {
	}

	public BoundingBox(Vector3f min, Vector3f max) {
		set(min, max);
	}

	public BoundingBox(float x0, float y0, float z0, float x1, float y1, float z1) {
		set(x0, y0, z0, x1, y1, z1);
	}

	public BoundingBox(BoundingBox boundingBox) {
		set(boundingBox);
	}

	public BoundingBox set(Vector3f min, Vector3f max) {
		this.min.set(min);
		this.max.set(max);
		return this;
	}

	public BoundingBox set(float x0, float y0, float z0, float x1, float y1, float z1) {
		this.min.set(x0, y0, z0);
		this.max.set(x1, y1, z1);
		return this;
	}

	public BoundingBox set(BoundingBox other) {
		this.min.set(other.min);
		this.max.set(other.max);
		return this;
	}

	public boolean intersects(BoundingBox other) {
		return (this.min.x <= other.max.x && this.max.x >= other.min.x)
				&& (this.min.y <= other.max.y && this.max.y >= other.min.y)
				&& (this.min.z <= other.max.z && this.max.z >= other.min.z);
	}

	public boolean intersects(Sphere other) {
		float closestX = Mathf.clamp(other.center.x, min.x, max.x);
		float closestY = Mathf.clamp(other.center.y, min.y, max.y);
		float closestZ = Mathf.clamp(other.center.z, min.z, max.z);

		float distanceSquared = (closestX - other.center.x) * (closestX - other.center.x)
				+ (closestY - other.center.y) * (closestY - other.center.y)
				+ (closestZ - other.center.z) * (closestZ - other.center.z);

		return distanceSquared <= other.radius * other.radius;
	}

	public boolean contains(Vector3f point) {
		return contains(point.x, point.y, point.z);
	}

	public boolean contains(float x, float y, float z) {
		return x >= min.x && x <= max.x && y >= min.y && y <= max.y && z >= min.z && z <= max.z;
	}

	public boolean contains(BoundingBox boundingBox) {
		return contains(boundingBox.min) && contains(boundingBox.max);
	}

	public boolean contains(Sphere sphere) {
		return (sphere.center.x - sphere.radius) >= min.x && (sphere.center.x + sphere.radius) <= max.x
				&& (sphere.center.y - sphere.radius) >= min.y && (sphere.center.y + sphere.radius) <= max.y
				&& (sphere.center.z - sphere.radius) >= min.z && (sphere.center.z + sphere.radius) <= max.z;
	}

	public BoundingBox copy() {
		return new BoundingBox(this);
	}

}
