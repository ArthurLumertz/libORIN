package net.orin.graphics.g3d.environment;

import org.joml.Vector3f;

import net.orin.graphics.Color;

public class DirectionalLight extends Light<DirectionalLight> {

	private final Vector3f direction = new Vector3f();

	public DirectionalLight setDirection(float x, float y, float z) {
		this.direction.set(x, y, z).normalize();
		return this;
	}

	public DirectionalLight setDirection(Vector3f direction) {
		this.direction.set(direction).normalize();
		return this;
	}

	public DirectionalLight set(Color color, Vector3f direction) {
		if (color != null) {
			this.color.set(color);
		}
		if (direction != null) {
			this.direction.set(direction).normalize();
		}
		return this;
	}

	public DirectionalLight set(DirectionalLight other) {
		return set(other.color, other.direction);
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof DirectionalLight) && equals((DirectionalLight) o);
	}

	public boolean equals(final DirectionalLight other) {
		return (other != null)
				&& ((other == this) || ((color.equals(other.color) && direction.equals(other.direction))));
	}
	
	public Vector3f getDirection() {
		return direction;
	}
	

}
