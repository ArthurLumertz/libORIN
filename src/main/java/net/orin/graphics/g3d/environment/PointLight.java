package net.orin.graphics.g3d.environment;

import org.joml.Vector3f;

import net.orin.graphics.Color;

public class PointLight extends Light<PointLight> {

	private final Vector3f position = new Vector3f();
	private float intensity = 1f;

	public PointLight setPosition(float x, float y, float z) {
		position.set(x, y, z);
		return this;
	}

	public PointLight setPosition(Vector3f position) {
		this.position.set(position);
		return this;
	}

	public PointLight setIntensity(float intensity) {
		this.intensity = intensity;
		return this;
	}

	public PointLight set(Color color, Vector3f position, float intensity) {
		this.color.set(color);
		this.position.set(position);
		this.intensity = intensity;
		return this;
	}

	public PointLight set(PointLight light) {
		return set(color, position, intensity);
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof PointLight) && equals((PointLight) obj);
	}

	public boolean equals(PointLight other) {
		return (other != null && (other == this
				|| (color.equals(other.color) && position.equals(other.position) && intensity == other.intensity)));
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getIntensity() {
		return intensity;
	}

}
