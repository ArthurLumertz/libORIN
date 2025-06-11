package net.orin.opengl;

public class GLVersion {

    public static final GLVersion GL21 = new GLVersion("OpenGL 2.1", false, 2, 1);
    public static final GLVersion GL30 = new GLVersion("OpenGL 3.0", false, 3, 0);
    public static final GLVersion GL31 = new GLVersion("OpenGL 3.1", true, 3, 1);
    public static final GLVersion GL32 = new GLVersion("OpenGL 3.2", true, 3, 2);
    public static final GLVersion GL33 = new GLVersion("OpenGL 3.3", true, 3, 3);
    public static final GLVersion GL40 = new GLVersion("OpenGL 4.0", true, 4, 0);
    public static final GLVersion GL41 = new GLVersion("OpenGL 4.1", true, 4, 1);
    public static final GLVersion GL42 = new GLVersion("OpenGL 4.2", true, 4, 2);
    public static final GLVersion GL43 = new GLVersion("OpenGL 4.3", true, 4, 3);
    public static final GLVersion GL44 = new GLVersion("OpenGL 4.4", true, 4, 4);
    public static final GLVersion GL45 = new GLVersion("OpenGL 4.5", true, 4, 5);
    public static final GLVersion GL46 = new GLVersion("OpenGL 4.6", true, 4, 6);

    private final boolean coreProfile;
    private final int minor;
    private final int major;

    public GLVersion(String name, boolean coreProfile, int major, int minor) {
        this.coreProfile = coreProfile;
        this.major = major;
        this.minor = minor;
    }

    public boolean isCoreProfile() {
        return coreProfile;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

}
