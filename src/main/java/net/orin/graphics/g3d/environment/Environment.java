package net.orin.graphics.g3d.environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.orin.graphics.Color;
import net.orin.graphics.Shader;
import net.orin.graphics.g3d.Camera3D;
import net.orin.graphics.g3d.Renderer;

public class Environment {

	private final List<Light<?>> lights = new ArrayList<>();

	private float fogStart = 10f;
	private float fogEnd = 50f;
	private float[] fogColor = new float[] { 0.5f, 0.5f, 0.5f };
	
	private Shader shader;
	
	public void apply(Renderer renderer, Camera3D camera) {
		this.shader = renderer.getShader();

		int spotLightCount = 0;
		int pointLightCount = 0;
		int dirLightCount = 0;

		for (Light<?> light : lights) {
			if (light instanceof SpotLight) {
				SpotLight spotLight = (SpotLight) light;
				String prefix = "spotLights[" + spotLightCount + "]";
				shader.setUniform3f(prefix + ".position", spotLight.getPosition().x, spotLight.getPosition().y,
						spotLight.getPosition().z);
				shader.setUniform3f(prefix + ".direction", spotLight.getDirection().x, spotLight.getDirection().y,
						spotLight.getDirection().z);
				shader.setUniform3f(prefix + ".color", spotLight.getColor().r, spotLight.getColor().g,
						spotLight.getColor().b);
				shader.setUniform1f(prefix + ".intensity", spotLight.getIntensity());
				shader.setUniform1f(prefix + ".cutoffAngle", spotLight.getCutoffAngle());
				shader.setUniform1f(prefix + ".exponent", spotLight.getExponent());
				shader.setUniform1f(prefix + ".constant", spotLight.getConstant());
				shader.setUniform1f(prefix + ".linear", spotLight.getLinear());
				shader.setUniform1f(prefix + ".quadratic", spotLight.getQuadratic());
				spotLightCount++;
			} else if (light instanceof PointLight) {
				PointLight point = (PointLight) light;
				String prefix = "pointLights[" + pointLightCount + "]";
				shader.setUniform3f(prefix + ".position", point.getPosition().x, point.getPosition().y,
						point.getPosition().z);
				shader.setUniform3f(prefix + ".color", point.getColor().r, point.getColor().g, point.getColor().b);
				shader.setUniform1f(prefix + ".intensity", point.getIntensity());
				pointLightCount++;
			} else if (light instanceof DirectionalLight) {
				DirectionalLight dir = (DirectionalLight) light;
				String prefix = "dirLights[" + dirLightCount + "]";
				shader.setUniform3f(prefix + ".direction", dir.getDirection().x, dir.getDirection().y,
						dir.getDirection().z);
				shader.setUniform3f(prefix + ".color", dir.getColor().r, dir.getColor().g, dir.getColor().b);
				dirLightCount++;
			}
		}

		shader.setUniform1i("numSpotLights", spotLightCount);
		shader.setUniform1i("numPointLights", pointLightCount);
		shader.setUniform1i("numDirLights", dirLightCount);

		shader.setUniform3f("fogColor", fogColor[0], fogColor[1], fogColor[2]);
		shader.setUniform1f("fogStart", fogStart);
		shader.setUniform1f("fogEnd", fogEnd);

		shader.setUniform3f("viewPos", camera.x(), camera.y(), camera.z());
	}

	public Environment addLight(Light<?> light) {
		if (light != null && !lights.contains(light)) {
			lights.add(light);
		}
		return this;
	}

	public Environment removeLight(Light<?> light) {
		lights.remove(light);
		return this;
	}

	public List<Light<?>> getLights() {
		return Collections.unmodifiableList(lights);
	}

	public float getFogStart() {
		return fogStart;
	}

	public Environment setFogStart(float fogStart) {
		this.fogStart = fogStart;
		return this;
	}

	public float getFogEnd() {
		return fogEnd;
	}

	public Environment setFogEnd(float fogEnd) {
		this.fogEnd = fogEnd;
		return this;
	}

	public float[] getFogColor() {
		return fogColor;
	}

	public Environment setFogColor(Color color) {
		fogColor[0] = color.r;
		fogColor[1] = color.g;
		fogColor[2] = color.b;
		return this;
	}

	public Environment setFogColor(float r, float g, float b) {
		this.fogColor[0] = r;
		this.fogColor[1] = g;
		this.fogColor[2] = b;
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T extends Light<T>> List<T> getLightsByType(Class<T> type) {
		List<T> result = new ArrayList<>();
		for (Light<?> light : lights) {
			if (type.isInstance(light)) {
				result.add((T) light);
			}
		}
		return result;
	}

}
