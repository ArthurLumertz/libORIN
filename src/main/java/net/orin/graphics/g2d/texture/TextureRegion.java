package net.orin.graphics.g2d.texture;

public class TextureRegion {

    private Texture texture;

    private float u0;
    private float v0;
    private float u1;
    private float v1;

    public TextureRegion() {
    }

    public TextureRegion(Texture texture, int x, int y, int width, int height) {
        set(texture, x, y, width, height);
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setRegion(int x, int y, int width, int height) {
        if (texture == null) {
            throw new IllegalStateException("Texture must be set before setting region");
        }

        float tw = texture.getWidth();
        float th = texture.getHeight();
        u0 = x / tw;
        v1 = 1f - (y / th);
        u1 = (x + width) / tw;
        v0 = 1f - ((y + height) / th);
    }

    public TextureRegion set(Texture texture, int x, int y, int width, int height) {
        setTexture(texture);
        setRegion(x, y, width, height);
        return this;
    }

    public float getU0() {
        return u0;
    }

    public float getV0() {
        return v0;
    }

    public float getU1() {
        return u1;
    }

    public float getV1() {
        return v1;
    }

    public Texture getTexture() {
        return texture;
    }

}
