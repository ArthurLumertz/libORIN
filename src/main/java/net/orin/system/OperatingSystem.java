package net.orin.system;

public class OperatingSystem {

    private static final OSEnum OS;

    static {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            OS = OSEnum.WINDOWS;
        } else if (osName.contains("mac") || osName.contains("darwin")) {
            OS = OSEnum.MACOS;
        } else if (osName.contains("nux") || osName.contains("linux")) {
            OS = OSEnum.LINUX;
        } else if (osName.contains("freebsd")) {
            OS = OSEnum.FREEBSD;
        } else if (osName.contains("openbsd")) {
            OS = OSEnum.OPENBSD;
        } else if (osName.contains("sunos") || osName.contains("solaris")) {
            OS = OSEnum.SOLARIS;
        } else {
            OS = OSEnum.UNKNOWN;
        }
    }

    public static OSEnum getOS() {
        return OS;
    }

}
