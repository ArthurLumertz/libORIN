package net.orin.math;

import java.util.Random;

public class Mathf {

	private static final Random random = new Random();

	public static final float PI = (float) Math.PI;
	public static final float PI2 = 2f * PI;
	public static final float HALF_PI = PI / 2;

	public static final float DEG_TO_RAD = PI / 180f;
	public static final float RAD_TO_DEG = 180f / PI;

	private static final int SIN_BITS = 14;
	private static final int SIN_MASK = ~(-1 << SIN_BITS);
	private static final int SIN_COUNT = SIN_MASK + 1;

	private static final float[] SIN_TABLE = new float[SIN_COUNT];
	private static final float DEG_TO_INDEX = SIN_COUNT / 360f;
	private static final float RAD_TO_INDEX = SIN_COUNT / PI2;

	static {
		for (int i = 0; i < SIN_COUNT; i++) {
			SIN_TABLE[i] = (float) Math.sin((i + 0.5f) / SIN_COUNT * PI2);
		}

		SIN_TABLE[0] = 0f;
		SIN_TABLE[(int) (90 * DEG_TO_INDEX) & SIN_MASK] = 1f;
		SIN_TABLE[(int) (180 * DEG_TO_INDEX) & SIN_MASK] = 0f;
		SIN_TABLE[(int) (270 * DEG_TO_INDEX) & SIN_MASK] = -1f;
	}

	public static float mod(float value, float min, float max) {
		float range = max - min;
		float result = (value - min) % range;
		result += (result < 0) ? range : 0;
		return result + min;
	}

	public static float sqrt(float value) {
		return (float) Math.sqrt(value);
	}

	public static float fastSqrt(float value) {
		return value * fastInverseSqrt(value);
	}

	private static float fastInverseSqrt(float x) {
		float xhalf = 0.5f * x;
		int i = Float.floatToIntBits(x);
		i = 0x5f3759df - (i >> 1);
		x = Float.intBitsToFloat(i);
		x = x * (1.5f - xhalf * x * x);
		return x;
	}

	public static float clamp(float value, float min, float max) {
		return (value < min) ? min : (value > max) ? max : value;
	}

	public static float max(float a, float b) {
		return (a > b) ? a : b;
	}

	public static float min(float a, float b) {
		return (a < b) ? a : b;
	}

	public static float lerp(float a, float b, float t) {
		return a + (b - a) * t;
	}

	public static float sin(float radians) {
		return SIN_TABLE[(int) (radians * RAD_TO_INDEX) & SIN_MASK];
	}

	public static float cos(float radians) {
		return SIN_TABLE[(int) ((radians + HALF_PI) * RAD_TO_INDEX) & SIN_MASK];
	}

	public static float sinDeg(float degrees) {
		return SIN_TABLE[(int) (degrees * DEG_TO_INDEX) & SIN_MASK];
	}

	public static float cosDeg(float degrees) {
		return SIN_TABLE[(int) ((degrees + 90) * DEG_TO_INDEX) & SIN_MASK];
	}

	public static float toRadians(float degrees) {
		return degrees * DEG_TO_RAD;
	}

	public static float toDegrees(float radians) {
		return radians * RAD_TO_DEG;
	}

	public static int random(int range) {
		return random.nextInt(range);
	}

	public static int random(int min, int max) {
		return min + random.nextInt(max - min);
	}

	public static float random(float range) {
		return random.nextFloat() * range;
	}

	public static float random(float min, float max) {
		return min + random.nextFloat() * (max - min);
	}

	public static int sign(float value) {
		return (value < 0) ? -1 : 1;
	}

	public static int floor(float value) {
		int i = (int) value;
		return (value < i) ? i - 1 : i;
	}

	public static int ceil(float value) {
		int i = (int) value;
		return (value > i) ? i + 1 : i;
	}

	public static int round(float value) {
		return (int) (value + 0.5f);
	}

	public static float atan2(float y, float x) {
		return (float) Math.atan2(y, x);
	}

	public static int nextPowerOfTwo(int value) {
		if (value <= 0)
			return 1;
		value--;
		value |= value >> 1;
		value |= value >> 2;
		value |= value >> 4;
		value |= value >> 8;
		value |= value >> 16;
		return value + 1;
	}

	public static boolean isPowerOfTwo(int value) {
		return value > 0 && (value & (value - 1)) == 0;
	}

	public static boolean isZero(float value, float epsilon) {
		return Math.abs(value) <= epsilon;
	}

	public static boolean isEqual(float a, float b, float epsilon) {
		return Math.abs(a - b) <= epsilon;
	}

	public static float wrapAngle(float angle) {
		angle = mod(angle, -180f, 180f);
		if (angle >= 180f)
			angle -= 360f;
		return angle;
	}

}
