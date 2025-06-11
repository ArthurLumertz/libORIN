package net.orin.graphics.g3d.environment;

import org.joml.Vector3f;

import net.orin.graphics.Color;

public class SpotLight extends Light<SpotLight> {

	private Vector3f position = new Vector3f();
	private Vector3f direction = new Vector3f();
	private float intensity = 1f;
	private float cutoffAngle = 2f;
	private float exponent = 1f;
	private float constant = 1.0f;
	private float linear = 0.09f;
	private float quadratic = 0.032f;
	
	public SpotLight() {
	}

	public SpotLight setPosition(float x, float y, float z) {
		this.position.set(x, y, z);
		return this;
	}
	
	public SpotLight setAttenuation(float constant, float linear, float quadratic) {
		this.constant = constant;
		this.linear = linear;
		this.quadratic = quadratic;
		return this;
	}

	public SpotLight setPosition(Vector3f position) {
		this.position.set(position);
		return this;
	}

	public SpotLight setDirection(float x, float y, float z) {
		this.direction.set(x, y, z);
		return this;
	}

	public SpotLight setDirection(Vector3f direction) {
		this.direction.set(direction);
		return this;
	}

	public SpotLight setIntensity(float intensity) {
		this.intensity = intensity;
		return this;
	}

	public SpotLight setCutoffAngle(float cutoffAngle) {
		this.cutoffAngle = cutoffAngle;
		return this;
	}

	public SpotLight setExponent(float exponent) {
		this.exponent = exponent;
		return this;
	}

	public SpotLight set(SpotLight copyFrom) {
		return set(copyFrom.color, copyFrom.position, copyFrom.direction, copyFrom.intensity, copyFrom.cutoffAngle,
				copyFrom.exponent);
	}

	public SpotLight set(Color color, Vector3f position, Vector3f direction, float intensity, float cutoffAngle,
			float exponent) {
		if (color != null) {
			this.color.set(color);
		}
		if (position != null) {
			this.position.set(position);
		}
		if (direction != null) {
			this.direction.set(direction).normalize();
		}
		this.intensity = intensity;
		this.cutoffAngle = cutoffAngle;
		this.exponent = exponent;
		return this;
	}

	public SpotLight setTarget(float x, float y, float z) {
		direction.set(x, y, z).sub(position).normalize();
		return this;
	}

	public SpotLight setTarget(Vector3f target) {
		direction.set(target).sub(position).normalize();
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof SpotLight) && equals((SpotLight) obj);
	}

	public boolean equals(SpotLight other) {
		if (other == null)
			return false;
		return color.equals(other.color) && position.equals(other.position) && direction.equals(other.direction)
				&& Float.compare(intensity, other.intensity) == 0 && Float.compare(cutoffAngle, other.cutoffAngle) == 0
				&& Float.compare(exponent, other.exponent) == 0;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getDirection() {
		return direction;
	}

	public float getIntensity() {
		return intensity;
	}

	public float getCutoffAngle() {
		return cutoffAngle;
	}

	public float getExponent() {
		return exponent;
	}

	public float getConstant() {
		return constant;
	}

	public float getLinear() {
		return linear;
	}

	public float getQuadratic() {
		return quadratic;
	}
	
	

}
