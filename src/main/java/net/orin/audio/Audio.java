package net.orin.audio;

import net.orin.util.Disposable;
import org.joml.Vector2f;
import org.joml.Vector3f;

public interface Audio extends Disposable {

	void setPosition(float x, float y, float z);
	void setPosition(float x, float y);
	
	void setPosition(Vector3f position);
	void setPosition(Vector2f position);
	
	void play();
	void play(float volume, float pitch);
	
	void setPitch(float pitch);
	void setVolume(float volume);
	void setLooping(boolean looping);
	
}
