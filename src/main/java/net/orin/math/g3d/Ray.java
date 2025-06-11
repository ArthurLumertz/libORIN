package net.orin.math.g3d;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import net.orin.math.Mathf;

public class Ray {

	private static final Vector3f tmpOrigin = new Vector3f();
	private static final Vector3f tmpEnd = new Vector3f();

	private final Vector3f origin = new Vector3f();
	private final Vector3f direction = new Vector3f();

	public Ray() {
	}

	public Ray(Vector3f origin, Vector3f direction) {
		this.origin.set(origin);
		this.direction.set(direction).normalize();
	}

	public Ray copy() {
		return new Ray(origin, direction);
	}

	public Vector3f getEndPoint(Vector3f out, float distance) {
		return out.set(direction).mul(distance).add(origin);
	}

	public Ray mul(Matrix4f matrix) {
		tmpOrigin.set(origin).mulPosition(matrix);
		tmpEnd.set(origin).add(direction).mulPosition(matrix);
		origin.set(tmpOrigin);
		direction.set(tmpEnd).sub(tmpOrigin).normalize();
		return this;
	}

	public Ray set(Vector3f origin, Vector3f direction) {
		this.origin.set(origin);
		this.direction.set(direction).normalize();
		return this;
	}

	public Ray set(float x, float y, float z, float dx, float dy, float dz) {
		this.origin.set(x, y, z);
		this.direction.set(dx, dy, dz).normalize();
		return this;
	}

	public Ray set(Ray ray) {
		this.origin.set(ray.origin);
		this.direction.set(ray.direction).normalize();
		return this;
	}

	public float intersects(Sphere sphere) {
		Vector3f L = new Vector3f(sphere.center).sub(origin);
		float tca = L.dot(direction);
		if (tca < 0)
			return -1f;

		float d2 = L.dot(L) - tca * tca;
		float radius2 = sphere.radius * sphere.radius;
		if (d2 > radius2)
			return -1f;

		float thc = Mathf.sqrt(radius2 - d2);
		float t0 = tca - thc;
		float t1 = tca + thc;

		if (t0 < 0 && t1 < 0)
			return -1f;

		return (t0 >= 0) ? t0 : t1;
	}

	public float intersects(BoundingBox box) {
		float tmin = (box.min.x - origin.x) / direction.x;
		float tmax = (box.max.x - origin.x) / direction.x;

		if (tmin > tmax) {
			float temp = tmin;
			tmin = tmax;
			tmax = temp;
		}

		float tymin = (box.min.y - origin.y) / direction.y;
		float tymax = (box.max.y - origin.y) / direction.y;

		if (tymin > tymax) {
			float temp = tymin;
			tymin = tymax;
			tymax = temp;
		}

		if ((tmin > tymax) || (tymin > tmax))
			return -1f;

		if (tymin > tmin)
			tmin = tymin;

		if (tymax < tmax)
			tmax = tymax;

		float tzmin = (box.min.z - origin.z) / direction.z;
		float tzmax = (box.max.z - origin.z) / direction.z;

		if (tzmin > tzmax) {
			float temp = tzmin;
			tzmin = tzmax;
			tzmax = temp;
		}

		if ((tmin > tzmax) || (tzmin > tmax))
			return -1f;

		if (tzmin > tmin)
			tmin = tzmin;

		if (tzmax < tmax)
			tmax = tzmax;

		if (tmax < 0)
			return -1f;

		return (tmin >= 0) ? tmin : tmax;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;
		Ray r = (Ray) o;
		return this.direction.equals(r.direction) && this.origin.equals(r.origin);
	}

	@Override
	public int hashCode() {
		final int prime = 73;
		int result = 1;
		result = prime * result + this.direction.hashCode();
		result = prime * result + this.origin.hashCode();
		return result;
	}

	public Vector3f getOrigin() {
		return origin;
	}

	public Vector3f getDirection() {
		return direction;
	}

	@Override
	public String toString() {
		return "Ray [origin=" + origin + ", direction=" + direction + "]";
	}

}
