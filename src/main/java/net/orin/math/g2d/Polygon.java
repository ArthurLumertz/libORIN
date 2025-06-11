package net.orin.math.g2d;

import org.joml.Vector2f;

public class Polygon {

    public static final Polygon tmp = new Polygon();
    public static final Polygon tmp2 = new Polygon();

    public float x;
    public float y;
    public float radius;
    public int sides;

    public Polygon() {}

    public Polygon(float x, float y, float radius, int sides) {
        set(x, y, radius, sides);
    }

    public Polygon(Polygon other) {
        set(other);
    }

    public Polygon set(float x, float y, float radius, int sides) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.sides = sides;
        return this;
    }

    public Polygon set(Polygon other) {
        return set(other.x, other.y, other.radius, other.sides);
    }

    public boolean contains(float px, float py) {
        float dx = px - x;
        float dy = py - y;
        return dx * dx + dy * dy <= radius * radius;
    }

    public boolean contains(Vector2f point) {
        return contains(point.x, point.y);
    }

    public boolean intersects(Polygon other) {
        float dx = other.x - x;
        float dy = other.y - y;
        float distSq = dx * dx + dy * dy;
        float sum = radius + other.radius;
        return distSq <= sum * sum;
    }

    public Polygon copy() {
        return new Polygon(this);
    }

    @Override
    public String toString() {
        return "Polygon{x=" + x + ", y=" + y + ", radius=" + radius + ", sides=" + sides + "}";
    }
}
