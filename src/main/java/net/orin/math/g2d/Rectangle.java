package net.orin.math.g2d;

import org.joml.Vector2f;

public class Rectangle {

    public static final Rectangle tmp = new Rectangle();
    public static final Rectangle tmp2 = new Rectangle();

    public float x;
    public float y;
    public float width;
    public float height;

    public Rectangle() {
    }

    public Rectangle(Rectangle other) {
        set(other);
    }

    public Rectangle(float x, float y, float width, float height) {
        set(x, y, width, height);
    }

    public Rectangle set(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        return this;
    }

    public Rectangle set(Rectangle other) {
        return set(other.x, other.y, other.width, other.height);
    }

    public boolean intersects(Rectangle other) {
        return (x < other.x + other.width &&
                x + width > other.x &&
                y < other.y + other.height &&
                y + height > other.y);
    }

    public boolean contains(float px, float py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    public boolean contains(Vector2f point) {
        return contains(point.x, point.y);
    }

    public boolean contains(Rectangle other) {
        return (other.x >= this.x &&
                other.x + other.width <= this.x + this.width &&
                other.y >= this.y &&
                other.y + other.height <= this.y + this.height);
    }

    public Rectangle copy() {
        return new Rectangle(this);
    }

    @Override
    public String toString() {
        return "Rectangle{x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "}";
    }

}
