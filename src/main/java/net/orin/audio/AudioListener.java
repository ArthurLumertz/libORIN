package net.orin.audio;

import net.orin.Time;
import net.orin.graphics.g3d.Camera3D;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.openal.AL10.*;

public class AudioListener {

	private static final Vector3f lastPosition = new Vector3f();
	private static final Vector3f tmpVec3 = new Vector3f();
	
	private static float[] tmp = new float[6];

	public static void setPosition(float x, float y, float z) {
		alListener3f(AL_POSITION, x, y, z);
	}

	public static void setPosition(Vector3f pos) {
		alListener3f(AL_POSITION, pos.x, pos.y, pos.z);
	}

	public static void setPosition(Vector2f pos) {
		alListener3f(AL_POSITION, pos.x, pos.y, 0.0f);
	}

	public static void setVelocity(float x, float y, float z) {
		alListener3f(AL_VELOCITY, x, y, z);
	}

	public static void setVelocity(Vector3f vel) {
		alListener3f(AL_VELOCITY, vel.x, vel.y, vel.z);
	}

	public static void setVelocity(Vector2f vel) {
		alListener3f(AL_VELOCITY, vel.x, vel.y, 0f);
	}

	public static void setOrientation(Vector3f at, Vector3f up) {
		tmp[0] = at.x;
		tmp[1] = at.y;
		tmp[2] = at.z;
		tmp[3] = up.x;
		tmp[4] = up.y;
		tmp[5] = up.z;
		
		alListenerfv(AL_ORIENTATION, tmp);
	}
	
	public static void apply(Camera3D camera) {
		Vector3f currentPos = camera.getPosition();
		Vector3f velocity = tmpVec3.set(currentPos).sub(lastPosition).mul(1f / Time.getDeltaTime());

		setPosition(currentPos);
		setVelocity(velocity);
		setOrientation(camera.getForward(), camera.getUp());

		lastPosition.set(currentPos);
	}

}
