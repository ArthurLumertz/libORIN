package net.orin;

public class Orin {

	private static final Version VERSION = new Version(1, 0, 0, "a1.0.0");

	protected static int frames;
	protected static int updates;

	public static int getFPS() {
		return frames;
	}

	public static int getUpdates() {
		return updates;
	}

	public static Version getVersion() {
		return VERSION;
	}

	public static class Version {

		private final int major;
		private final int minor;
		private final int patch;

		private final String version;

		public Version(int major, int minor, int patch, String version) {
			this.major = major;
			this.minor = minor;
			this.patch = patch;
			this.version = version;
		}

		public int getMajor() {
			return major;
		}

		public int getMinor() {
			return minor;
		}

		public int getPatch() {
			return patch;
		}

		public String getVersionString() {
			return version;
		}

		public boolean isNewerThan(Version other) {
			if (major != other.major) {
				return major > other.major;
			}
			if (minor != other.minor) {
				return minor > other.minor;
			}
			return patch > other.patch;
		}

		@Override
		public String toString() {
			return version;
		}
	}

}
