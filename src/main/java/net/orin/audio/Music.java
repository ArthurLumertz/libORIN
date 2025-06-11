package net.orin.audio;

import net.orin.io.FileRef;

public class Music extends Sound {

    public Music() {}

    public Music(String filePath) {
        super(filePath);
    }

    public Music(FileRef ref) {
        super(ref);
    }

}
