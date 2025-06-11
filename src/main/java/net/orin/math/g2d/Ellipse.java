package net.orin.math.g2d;

import org.joml.Vector2f;

public class Ellipse {
	
    public static final Ellipse tmp = new Ellipse();
    public static final Ellipse tmp2 = new Ellipse();

    public float x;
    public float y;
    public float radiusX;
    public float radiusY;

    public Ellipse() {}

    public Ellipse(float x, float y, float radiusX, float radiusY) {
        set(x, y, radiusX, radiusY);
    }

    public Ellipse(Ellipse other) {
        set(other);
    }

    public Ellipse set(float x, float y, float radiusX, float radiusY) {
        this.x = x;
        this.y = y;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        return this;
    }

    public Ellipse set(Ellipse other) {
        return set(other.x, other.y, other.radiusX, other.radiusY);
    }

    public boolean contains(float px, float py) {
        float dx = px - x;
        float dy = py - y;
        return (dx * dx) / (radiusX * radiusX) + (dy * dy) / (radiusY * radiusY) <= 1f;
    }

    public boolean contains(Vector2f point) {
        return contains(point.x, point.y);
    }

    public boolean intersects(Ellipse other) {
        float dx = other.x - x;
        float dy = other.y - y;
        float distSq = dx * dx + dy * dy;
        float approxRadiusThis = 0.5f * (radiusX + radiusY);
        float approxRadiusOther = 0.5f * (other.radiusX + other.radiusY);
        float sum = approxRadiusThis + approxRadiusOther;
        return distSq <= sum * sum;
    }

    public Ellipse copy() {
        return new Ellipse(this);
    }

    @Override
    public String toString() {
        return "Ellipse{" +
                "x=" + x +
                ", y=" + y +
                ", radiusX=" + radiusX +
                ", radiusY=" + radiusY +
                '}';
    }

}
