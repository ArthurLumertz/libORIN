package net.orin;

import org.lwjgl.opengl.GL11;

import net.orin.graphics.Color;
import net.orin.graphics.g2d.texture.Texture;
import net.orin.graphics.g3d.Camera3D;
import net.orin.graphics.g3d.Renderer;
import net.orin.graphics.g3d.environment.DirectionalLight;
import net.orin.graphics.g3d.environment.Environment;
import net.orin.graphics.g3d.environment.SpotLight;
import net.orin.graphics.g3d.mesh.MeshInstance;
import net.orin.graphics.g3d.mesh.SubMesh;
import net.orin.graphics.g3d.mesh.TexturedMesh;
import net.orin.lwjgl3.Display;
import net.orin.lwjgl3.input.Key;
import net.orin.lwjgl3.input.Keyboard;
import net.orin.lwjgl3.input.Mouse;
import net.orin.util.AssetLoader;
import net.orin.util.MeshBuilder;

public class EngineTester implements Game {

	private Camera3D camera;
	private MeshInstance instance;
	private Renderer renderer;
	private Environment environment;

	private SpotLight spotLight;

	private AssetLoader assetLoader;

	@Override
	public void create() {
		camera = new Camera3D(67f, (float) Display.getWidth() / Display.getHeight(), 0.03f, 300f);
		camera.setPosition(0, 1, 0);

		renderer = new Renderer();

		environment = new Environment();
		environment.setFogColor(Color.BLACK).setFogStart(1f).setFogEnd(5f);
		environment.addLight(new DirectionalLight().setDirection(-1, -1, -1).setColor(Color.WHITE));

		spotLight = new SpotLight().setColor(new Color(1f, 1f, 0.95f)).setIntensity(1f)
				.setAttenuation(1f, 0.09f, 0.032f).setCutoffAngle(12f).setExponent(20f);
		environment.addLight(spotLight);

		MeshBuilder builder = new MeshBuilder();
		
		instance = new MeshInstance();

		assetLoader = new AssetLoader();
		assetLoader.load("orin-framework-64x.png", Texture.class);
		assetLoader.finishLoading();

		builder.createCube(1f, 1f, 1f);
		instance.addMesh(new SubMesh(new TexturedMesh(assetLoader.get("orin-framework-64x.png", Texture.class), builder.build())).setPosition(0, 1, 0));
		
		builder.createPlane(256f, 256f, 64, 64);
		instance.addMesh(builder.build());
		
		instance.setPosition(0, 0, 0);

		Mouse.setGrabbed(true);
	}

	@Override
	public void update() {
		float xa = Mouse.getDX();
		float ya = Mouse.getDY();
		camera.addYaw(xa);
		camera.addPitch(ya);

		spotLight.setPosition(camera.getPosition());
		spotLight.setDirection(camera.getForward());

		float speed = Time.getDeltaTime() * 5;

		if (Keyboard.isKeyDown(Key.W)) {
			camera.addPosition(camera.getForward().mul(speed));
		}
		if (Keyboard.isKeyDown(Key.S)) {
			camera.addPosition(camera.getForward().mul(speed).negate());
		}
		if (Keyboard.isKeyDown(Key.A)) {
			camera.addPosition(camera.getRight().mul(speed).negate());
		}
		if (Keyboard.isKeyDown(Key.D)) {
			camera.addPosition(camera.getRight().mul(speed));
		}

		camera.update();
	}

	@Override
	public void render() {
		GL11.glClearColor(0f, 0f, 0f, 0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		renderer.setCombinedMatrix(camera.getCombinedMatrix());

		renderer.begin();
		environment.apply(renderer, camera);

		instance.draw(renderer);
		renderer.end();
	}

	@Override
	public void dispose() {
		instance.dispose();
		renderer.dispose();
	}

	public static void main(String[] args) {
		Application app = new Application(new EngineTester());
		app.setTargetFPS(165);
		app.useVsync(false);
		app.setResizable(false);
		app.start();
	}

}
