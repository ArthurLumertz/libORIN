package net.orin.lwjgl3;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.system.MemoryUtil;

class ALContext {

	private static long device;
	private static long context;

	public static void create() {
		device = ALC10.alcOpenDevice((ByteBuffer) null);
		if (device == MemoryUtil.NULL) {
			throw new IllegalStateException("Failed to open the default audio device.");
		}

		IntBuffer contextAttribList = null;
		context = ALC10.alcCreateContext(device, contextAttribList);
		if (context == MemoryUtil.NULL) {
			throw new IllegalStateException("Failed to create OpenAL context.");
		}

		ALC10.alcMakeContextCurrent(context);

		AL.createCapabilities(ALC.createCapabilities(device));
	}

	public static void dispose() {
		ALC10.alcDestroyContext(context);
		ALC10.alcCloseDevice(device);
	}

}
