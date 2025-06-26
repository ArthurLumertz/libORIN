package net.orin.system;

public class OperatingSystem {

	private static final OSEnum OS = findOsByName();

	private static OSEnum findOsByName() {
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("win")) {
			return OSEnum.WINDOWS;
		} else if (osName.contains("mac") || osName.contains("darwin")) {
			return OSEnum.MACOS;
		} else if (osName.contains("nux") || osName.contains("linux")) {
			return OSEnum.LINUX;
		} else if (osName.contains("freebsd")) {
			return OSEnum.FREEBSD;
		} else if (osName.contains("openbsd")) {
			return OSEnum.OPENBSD;
		} else if (osName.contains("sunos") || osName.contains("solaris")) {
			return OSEnum.SOLARIS;
		}
		return OSEnum.UNKNOWN;
	}

	public static OSEnum getOS() {
		return OS;
	}

}
