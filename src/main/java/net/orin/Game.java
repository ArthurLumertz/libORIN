package net.orin;

import net.orin.util.Disposable;

public interface Game extends Disposable {

    void create();

    void update();

    void render();

}
