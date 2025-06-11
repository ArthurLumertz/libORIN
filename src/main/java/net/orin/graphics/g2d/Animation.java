package net.orin.graphics.g2d;

public class Animation<T> {

	private T[] frames;
	private int frameIndex;
	private float frameDuration;

	public Animation() {
	}

	@SuppressWarnings("unchecked")
	public Animation(float frameDuration, T... frames) {
		this.frames = frames;
		this.frameDuration = frameDuration;
	}

	public Animation<T> set(Animation<T> other) {
		frames = other.frames;
		frameIndex = other.frameIndex;
		frameDuration = other.frameDuration;
		return this;
	}

	public T getFrame(float elapsedTime, boolean looping) {
		if (frames == null || frames.length == 0) {
			return null;
		}

		int totalFrames = frames.length;
		int frameCount = (int) (elapsedTime / frameDuration);

		if (looping) {
			frameIndex = frameCount % totalFrames;
			if (frameIndex < 0)
				frameIndex += totalFrames;
		} else {
			frameIndex = frameCount;
			if (frameIndex >= totalFrames)
				frameIndex = totalFrames - 1;
			if (frameIndex < 0)
				frameIndex = 0;
		}

		return frames[frameIndex];
	}

}
