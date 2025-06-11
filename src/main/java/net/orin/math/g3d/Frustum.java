package net.orin.math.g3d;

import net.orin.math.Mathf;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Frustum {

    private final Vector4f[] planes = new Vector4f[6];

    public Frustum() {
        for (int i = 0; i < 6; i++) {
            planes[i] = new Vector4f();
        }
    }

    public void update(Matrix4f mat) {
        planes[0].set(
                mat.m03() + mat.m00(),
                mat.m13() + mat.m10(),
                mat.m23() + mat.m20(),
                mat.m33() + mat.m30()
        );
        planes[1].set(
                mat.m03() - mat.m00(),
                mat.m13() - mat.m10(),
                mat.m23() - mat.m20(),
                mat.m33() - mat.m30()
        );
        planes[2].set(
                mat.m03() + mat.m01(),
                mat.m13() + mat.m11(),
                mat.m23() + mat.m21(),
                mat.m33() + mat.m31()
        );
        planes[3].set(
                mat.m03() - mat.m01(),
                mat.m13() - mat.m11(),
                mat.m23() - mat.m21(),
                mat.m33() - mat.m31()
        );
        planes[4].set(
                mat.m03() + mat.m02(),
                mat.m13() + mat.m12(),
                mat.m23() + mat.m22(),
                mat.m33() + mat.m32()
        );
        planes[5].set(
                mat.m03() - mat.m02(),
                mat.m13() - mat.m12(),
                mat.m23() - mat.m22(),
                mat.m33() - mat.m32()
        );

        for (int i = 0; i < 6; i++) {
            Vector4f p = planes[i];
            float length = Mathf.sqrt(p.x * p.x + p.y * p.y + p.z * p.z);
            p.div(length);
        }
    }

    public boolean testSphere(Vector3f center, float radius) {
        for (int i = 0; i < 6; i++) {
            Vector4f p = planes[i];
            float distance = p.x * center.x + p.y * center.y + p.z * center.z + p.w;
            if (distance < -radius) {
                return false;
            }
        }
        return true;
    }

    public boolean testSphere(Sphere sphere) {
        return testSphere(sphere.center, sphere.radius);
    }

    public boolean testBoundingBox(float x0, float y0, float z0, float x1, float y1, float z1) {
        for (int i = 0; i < 6; i++) {
            Vector4f p = planes[i];

            float px = p.x >= 0 ? x1 : x0;
            float py = p.y >= 0 ? y1 : y0;
            float pz = p.z >= 0 ? z1 : z0;

            float distance = p.x * px + p.y * py + p.z * pz + p.w;
            if (distance < 0) {
                return false;
            }
        }
        return true;
    }

    public boolean testBoundingBox(BoundingBox boundingBox) {
        return testBoundingBox(boundingBox.min.x, boundingBox.min.y, boundingBox.min.z,
                                boundingBox.max.x, boundingBox.max.y, boundingBox.max.z);
    }

}
