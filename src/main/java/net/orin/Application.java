package net.orin;

import static org.lwjgl.opengl.GL11.*;

import net.orin.lwjgl3.Display;
import net.orin.lwjgl3.DisplayMode;
import net.orin.opengl.GLVersion;

public class Application {

    private Game game;

    private int targetFPS;
    private boolean useVsync;
    private String title;
    private boolean resizable;
    private String iconPath;
    private int samples;

    private DisplayMode displayMode;
    private GLVersion glVersion;

    private boolean running;

    public Application(Game game) {
        this.game = game;
        this.displayMode = new DisplayMode(854, 480);
        setDisplayMode(displayMode);
        setOpenGLVersion(GLVersion.GL33);
        setIcon("orin-framework-64x.png");
        setTitle("Game");
        setTargetFPS(0);
        useVsync(true);
    }

    private void init() {
        Display.setDisplayMode(displayMode);
        Display.setTitle(title);
        Display.setResizable(resizable);
        Display.setIcon(iconPath);
        Display.setSamples(samples);
        Display.useVsync(useVsync);
        Display.create(glVersion);
    }

    private void run() {
        running = true;
        init();

        long lastTime = System.nanoTime();
        long lastFrame = System.currentTimeMillis();
        long lastTimer = System.currentTimeMillis();

        double nsPerTick = (targetFPS > 0) ? 1000000000.0 / targetFPS : 0;
        double unprocessed = 0;
        int ticks = 0;
        int frames = 0;

        game.create();

        while (running) {
            long now = System.nanoTime();
            if (targetFPS > 0) {
                unprocessed += (now - lastTime) / nsPerTick;
                lastTime = now;

                while (unprocessed >= 1) {
                    ticks++;

                    long nowFrame = System.currentTimeMillis();
                    float deltaTime = (float) (nowFrame - lastFrame) / 1000f;
                    lastFrame = nowFrame;

                    Time.elapsedTime += deltaTime;
                    Time.deltaTime = deltaTime;

                    glViewport(0, 0, Display.getWidth(), Display.getHeight());

                    game.update();
                    unprocessed--;
                }

                game.render();
                frames++;

                Display.swapBuffers();

            } else {
                long nowFrame = System.currentTimeMillis();
                float deltaTime = (float) (nowFrame - lastFrame) / 1000f;
                lastFrame = nowFrame;

                Time.elapsedTime += deltaTime;
                Time.deltaTime = deltaTime;

                glViewport(0, 0, Display.getWidth(), Display.getHeight());

                game.update();
                game.render();
                frames++;

                Display.swapBuffers();

                lastTime = now;
            }

            if (Display.shouldClose()) running = false;

            if (System.currentTimeMillis() - lastTimer > 1000) {
                Orin.frames = frames;
                Orin.updates = ticks;
                frames = 0;
                ticks = 0;
                lastTimer += 1000;
            }
        }

        game.dispose();
        Display.terminate();
    }

    public void setOpenGLVersion(GLVersion glVersion) {
        this.glVersion = glVersion;
    }

    public void setTargetFPS(int targetFPS) {
        this.targetFPS = targetFPS;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSamples(int samples) {
        this.samples = samples;
    }

    public void useVsync(boolean useVsync) {
        this.useVsync = useVsync;
    }

    public void setIcon(String iconPath) {
        this.iconPath = iconPath;
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    public void start() {
        run();
    }

}
