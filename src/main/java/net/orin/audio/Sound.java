package net.orin.audio;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import net.orin.io.FileRef;

public class Sound implements Audio {

	private int bufferId;
	private int sourceId;

	public Sound() {
	}

	public Sound(String filePath) {
		this(FileRef.internal(filePath));
	}

	public Sound(FileRef ref) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			byte[] bytes = ref.readAllBytes();
			ByteBuffer buffer = MemoryUtil.memAlloc(bytes.length);
			buffer.put(bytes).flip();

			IntBuffer channels = stack.mallocInt(1);
			IntBuffer sampleRate = stack.mallocInt(1);

			ShortBuffer rawAudio = stb_vorbis_decode_memory(buffer, channels, sampleRate);
			if (rawAudio == null) {
				throw new RuntimeException("Failed to decode audio: " + ref.path() + "!");
			}

			int channelCount = channels.get(0);
			int format = channelCount == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16;

			bufferId = alGenBuffers();
			alBufferData(bufferId, format, rawAudio, sampleRate.get(0));

			MemoryUtil.memFree(rawAudio);
			MemoryUtil.memFree(buffer);

			sourceId = alGenSources();
			alSourcei(sourceId, AL_BUFFER, bufferId);
		}
	}

	@Override
	public void setPosition(float x, float y, float z) {
		alSource3f(sourceId, AL_POSITION, x, y, z);
	}

	@Override
	public void setPosition(float x, float y) {
		setPosition(x, y, 0f);
	}

	@Override
	public void setPosition(Vector3f position) {
		setPosition(position.x, position.y, position.z);
	}

	@Override
	public void setPosition(Vector2f position) {
		setPosition(position.x, position.y, 0f);
	}

	@Override
	public void play() {
		alSourcePlay(sourceId);
	}

	@Override
	public void play(float volume, float pitch) {
		setVolume(volume);
		setPitch(pitch);
		play();
	}

	@Override
	public void setPitch(float pitch) {
		alSourcef(sourceId, AL_PITCH, pitch);
	}

	@Override
	public void setVolume(float volume) {
		alSourcef(sourceId, AL_GAIN, volume);
	}

	@Override
	public void setLooping(boolean looping) {
		alSourcei(sourceId, AL_LOOPING, looping ? AL_TRUE : AL_FALSE);
	}

	@Override
	public void dispose() {
		alDeleteSources(sourceId);
		alDeleteBuffers(bufferId);
	}

}
