package net.orin.math.g3d;

import net.orin.math.Mathf;
import org.joml.Vector3f;

public class Sphere {

	public static final Sphere tmp = new Sphere();
	public static final Sphere tmp2 = new Sphere();

	public float radius;
	public final Vector3f center = new Vector3f();

	public Sphere() {
	}

	public Sphere(Sphere other) {
		set(other);
	}

	public Sphere(Vector3f center, float radius) {
		set(center, radius);
	}

	public Sphere(float x, float y, float z, float radius) {
		set(x, y, z, radius);
	}

	public Sphere set(Vector3f center, float radius) {
		this.center.set(center);
		this.radius = radius;
		return this;
	}

	public Sphere set(float x, float y, float z, float radius) {
		this.center.set(x, y, z);
		this.radius = radius;
		return this;
	}

	public Sphere set(Sphere other) {
		return set(other.center, other.radius);
	}

	public boolean intersects(Sphere other) {
		float radiusSum = radius + other.radius;
		return center.distanceSquared(other.center) <= radiusSum * radiusSum;
	}

	public boolean intersects(BoundingBox boundingBox) {
		float closestX = Mathf.clamp(center.x, boundingBox.min.x, boundingBox.max.x);
		float closestY = Mathf.clamp(center.y, boundingBox.min.y, boundingBox.max.y);
		float closestZ = Mathf.clamp(center.z, boundingBox.min.z, boundingBox.max.z);

		float distSq = (closestX - center.x) * (closestX - center.x) + (closestY - center.y) * (closestY - center.y)
				+ (closestZ - center.z) * (closestZ - center.z);

		return distSq <= radius * radius;
	}

	public boolean overlaps(float x, float y, float z) {
		float dx = x - center.x;
		float dy = y - center.y;
		float dz = z - center.z;
		return dx * dx + dy * dy + dz * dz <= radius * radius;
	}

	public boolean overlaps(Vector3f point) {
		return overlaps(point.x, point.y, point.z);
	}

	public Sphere copy() {
		return new Sphere(this);
	}

}
