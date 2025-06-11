package net.orin.math.g2d;

public class Circle {

    public static final Circle tmp = new Circle();
    public static final Circle tmp2 = new Circle();

    public float x;
    public float y;
    public float radius;

    public Circle() {}

    public Circle(Circle circle) {
        set(circle);
    }

    public Circle(float x, float y, float radius) {
        set(x, y, radius);
    }

    public Circle set(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        return this;
    }

    public Circle set(Circle circle) {
        return set(circle.x, circle.y, circle.radius);
    }

    public boolean intersects(Circle other) {
        float dx = other.x - this.x;
        float dy = other.y - this.y;
        float distanceSquared = dx * dx + dy * dy;
        float radiusSum = this.radius + other.radius;
        return distanceSquared <= radiusSum * radiusSum;
    }

    public boolean contains(float px, float py) {
        float dx = px - this.x;
        float dy = py - this.y;
        return dx * dx + dy * dy <= radius * radius;
    }

    public boolean contains(Circle other) {
        float dx = other.x - this.x;
        float dy = other.y - this.y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        return dist + other.radius <= this.radius;
    }

    public Circle copy() {
        return new Circle(this);
    }

    @Override
    public String toString() {
        return "Circle{x=" + x + ", y=" + y + ", radius=" + radius + "}";
    }

}
