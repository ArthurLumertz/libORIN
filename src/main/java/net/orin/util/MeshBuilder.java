package net.orin.util;

import net.orin.graphics.g3d.mesh.Mesh;
import net.orin.math.Mathf;

import java.util.ArrayList;
import java.util.List;

public class MeshBuilder {

	private List<Float> vertices = new ArrayList<>();
	private List<Float> texCoords = new ArrayList<>();
	private List<Float> normals = new ArrayList<>();
	private List<Integer> indices = new ArrayList<>();

	public void createCube(float width, float height, float depth) {
		vertices.clear();
		texCoords.clear();
		normals.clear();
		indices.clear();

		float w = width / 2f;
		float h = height / 2f;
		float d = depth / 2f;

		float[] positions = { -w, -h, d, w, -h, d, w, h, d, -w, h, d,

				w, -h, -d, -w, -h, -d, -w, h, -d, w, h, -d,

				-w, -h, -d, -w, -h, d, -w, h, d, -w, h, -d,

				w, -h, d, w, -h, -d, w, h, -d, w, h, d,

				-w, h, d, w, h, d, w, h, -d, -w, h, -d,

				-w, -h, -d, w, -h, -d, w, -h, d, -w, -h, d };

		float[] uvs = { 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1,
				0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, };

		float[] faceNormals = { 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, -1, 0, 0,
				-1, 0, 0, -1, 0, 0, -1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0,
				-1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0 };

		int[] indices = { 0, 1, 2, 2, 3, 0, 4, 5, 6, 6, 7, 4, 8, 9, 10, 10, 11, 8, 12, 13, 14, 14, 15, 12, 16, 17, 18,
				18, 19, 16, 20, 21, 22, 22, 23, 20 };

		for (int i = 0; i < 24; i++) {
			vertices.add(positions[i * 3]);
			vertices.add(positions[i * 3 + 1]);
			vertices.add(positions[i * 3 + 2]);

			texCoords.add(uvs[i * 2]);
			texCoords.add(uvs[i * 2 + 1]);

			normals.add(faceNormals[i * 3]);
			normals.add(faceNormals[i * 3 + 1]);
			normals.add(faceNormals[i * 3 + 2]);
		}

		for (int index : indices) {
			this.indices.add(index);
		}
	}

	public void createSphere(float radius, int stacks, int slices) {
		vertices.clear();
		texCoords.clear();
		normals.clear();
		indices.clear();

		for (int stack = 0; stack <= stacks; stack++) {
			float phi = Mathf.PI * stack / stacks; // from 0 to PI
			float y = Mathf.cos(phi);
			float sinPhi = Mathf.sin(phi);

			for (int slice = 0; slice <= slices; slice++) {
				float theta = Mathf.PI2 * slice / slices;
				float x = sinPhi * Mathf.cos(theta);
				float z = sinPhi * Mathf.sin(theta);

				vertices.add(radius * x);
				vertices.add(radius * y);
				vertices.add(radius * z);

				normals.add(x);
				normals.add(y);
				normals.add(z);

				float u = (float) slice / slices;
				float v = 1f - (float) stack / stacks;
				texCoords.add(u);
				texCoords.add(v);
			}
		}

		for (int stack = 0; stack < stacks; stack++) {
			for (int slice = 0; slice < slices; slice++) {
				int first = stack * (slices + 1) + slice;
				int second = first + slices + 1;

				// First triangle
				indices.add(first);
				indices.add(first + 1);
				indices.add(second);

				// Second triangle
				indices.add(second);
				indices.add(first + 1);
				indices.add(second + 1);
			}
		}
	}

	public void createCone(float radius, float height, int slices) {
		vertices.clear();
		texCoords.clear();
		normals.clear();
		indices.clear();

		vertices.add(0f);
		vertices.add(height / 2f);
		vertices.add(0f);

		normals.add(0f);
		normals.add(1f);
		normals.add(0f);

		texCoords.add(0.5f);
		texCoords.add(1f);

		vertices.add(0f);
		vertices.add(-height / 2f);
		vertices.add(0f);

		normals.add(0f);
		normals.add(-1f);
		normals.add(0f);

		texCoords.add(0.5f);
		texCoords.add(0.5f);

		for (int i = 0; i <= slices; i++) {
			float angle = Mathf.PI2 * i / slices;
			float x = radius * Mathf.cos(angle);
			float z = radius * Mathf.sin(angle);

			vertices.add(x);
			vertices.add(-height / 2f);
			vertices.add(z);

			float sideNormalX = x;
			float sideNormalY = radius / height;
			float sideNormalZ = z;
			float length = Mathf
					.sqrt(sideNormalX * sideNormalX + sideNormalY * sideNormalY + sideNormalZ * sideNormalZ);
			normals.add(sideNormalX / length);
			normals.add(sideNormalY / length);
			normals.add(sideNormalZ / length);

			texCoords.add((float) i / slices);
			texCoords.add(0f);
		}

		int tipIndex = 0;
		int baseCenterIndex = 1;
		int baseStartIndex = 2;
		
		for (int i = 0; i < slices; i++) {
			indices.add(tipIndex);
			indices.add(baseStartIndex + i + 1);
			indices.add(baseStartIndex + i);
		}

		for (int i = 0; i < slices; i++) {
			indices.add(baseCenterIndex);
			indices.add(baseStartIndex + i);
			indices.add(baseStartIndex + i + 1);
		}
	}

	public void createCylinder(float radius, float height, int slices) {
		vertices.clear();
		texCoords.clear();
		normals.clear();
		indices.clear();

		float halfHeight = height / 2f;

		for (int i = 0; i <= slices; i++) {
			float angle = Mathf.PI2 * i / slices;
			float x = radius * Mathf.cos(angle);
			float z = radius * Mathf.sin(angle);

			vertices.add(x);
			vertices.add(halfHeight);
			vertices.add(z);

			normals.add(0f);
			normals.add(1f);
			normals.add(0f);

			texCoords.add((x / radius + 1f) * 0.5f);
			texCoords.add((z / radius + 1f) * 0.5f);

			vertices.add(x);
			vertices.add(-halfHeight);
			vertices.add(z);

			normals.add(0f);
			normals.add(-1f);
			normals.add(0f);

			texCoords.add((x / radius + 1f) * 0.5f);
			texCoords.add((z / radius + 1f) * 0.5f);

			vertices.add(x);
			vertices.add(halfHeight);
			vertices.add(z);

			normals.add(x / radius);
			normals.add(0f);
			normals.add(z / radius);

			texCoords.add((float) i / slices);
			texCoords.add(1f);

			vertices.add(x);
			vertices.add(-halfHeight);
			vertices.add(z);

			normals.add(x / radius);
			normals.add(0f);
			normals.add(z / radius);

			texCoords.add((float) i / slices);
			texCoords.add(0f);
		}

		int topCenterIndex = vertices.size() / 3;
		vertices.add(0f);
		vertices.add(halfHeight);
		vertices.add(0f);

		normals.add(0f);
		normals.add(1f);
		normals.add(0f);

		texCoords.add(0.5f);
		texCoords.add(0.5f);

		int bottomCenterIndex = vertices.size() / 3;
		vertices.add(0f);
		vertices.add(-halfHeight);
		vertices.add(0f);

		normals.add(0f);
		normals.add(-1f);
		normals.add(0f);

		texCoords.add(0.5f);
		texCoords.add(0.5f);

		for (int i = 0; i < slices; i++) {
			int topIndex = i * 4;
			int bottomIndex = i * 4 + 1;
			int sideTopIndex = i * 4 + 2;
			int sideBottomIndex = i * 4 + 3;

			indices.add(topCenterIndex);
			indices.add((topIndex + 4) % (slices * 4));
			indices.add(topIndex);

			indices.add(bottomCenterIndex);
			indices.add(bottomIndex);
			indices.add((bottomIndex + 4) % (slices * 4));

			indices.add(sideTopIndex);
			indices.add(sideTopIndex + 4);
			indices.add(sideBottomIndex);

			indices.add(sideTopIndex + 4);
			indices.add(sideBottomIndex + 4);
			indices.add(sideBottomIndex);
		}

	}

	public void createPlane(float width, float height, int segmentsWidth, int segmentsHeight) {
		vertices.clear();
		texCoords.clear();
		normals.clear();
		indices.clear();

		float halfWidth = width / 2f;
		float halfHeight = height / 2f;

		for (int y = 0; y <= segmentsHeight; y++) {
			float z = ((float) y / segmentsHeight) * height - halfHeight;
			for (int x = 0; x <= segmentsWidth; x++) {
				float xPos = ((float) x / segmentsWidth) * width - halfWidth;
				vertices.add(xPos);
				vertices.add(0f);
				vertices.add(z);

				normals.add(0f);
				normals.add(1f);
				normals.add(0f);

				texCoords.add((float) x / segmentsWidth);
				texCoords.add(1f - (float) y / segmentsHeight);
			}
		}

		for (int y = 0; y < segmentsHeight; y++) {
			for (int x = 0; x < segmentsWidth; x++) {
				int i0 = y * (segmentsWidth + 1) + x;
				int i1 = i0 + 1;
				int i2 = i0 + (segmentsWidth + 1);
				int i3 = i2 + 1;

				indices.add(i0);
				indices.add(i2);
				indices.add(i1);

				indices.add(i1);
				indices.add(i2);
				indices.add(i3);
			}
		}
	}

	public void createTorus(float majorRadius, float minorRadius, int majorSegments, int minorSegments) {
		vertices.clear();
		texCoords.clear();
		normals.clear();
		indices.clear();

		for (int i = 0; i <= majorSegments; i++) {
			float majorAngle = (float) i / majorSegments * Mathf.PI2;
			float cosMajor = Mathf.cos(majorAngle);
			float sinMajor = Mathf.sin(majorAngle);

			for (int j = 0; j <= minorSegments; j++) {
				float minorAngle = (float) j / minorSegments * Mathf.PI2;
				float cosMinor = Mathf.cos(minorAngle);
				float sinMinor = Mathf.sin(minorAngle);

				float x = (majorRadius + minorRadius * cosMinor) * cosMajor;
				float y = minorRadius * sinMinor;
				float z = (majorRadius + minorRadius * cosMinor) * sinMajor;

				vertices.add(x);
				vertices.add(y);
				vertices.add(z);

				float nx = cosMinor * cosMajor;
				float ny = sinMinor;
				float nz = cosMinor * sinMajor;
				float length = Mathf.sqrt(nx * nx + ny * ny + nz * nz);
				normals.add(nx / length);
				normals.add(ny / length);
				normals.add(nz / length);

				texCoords.add((float) i / majorSegments);
				texCoords.add((float) j / minorSegments);
			}
		}

		for (int i = 0; i < majorSegments; i++) {
			for (int j = 0; j < minorSegments; j++) {
				int first = i * (minorSegments + 1) + j;
				int second = first + minorSegments + 1;

		        indices.add(first);
		        indices.add(first + 1);
		        indices.add(second);

		        indices.add(second);
		        indices.add(first + 1);
		        indices.add(second + 1);
			}
		}
	}

	public float[] getVertices() {
		return toArray(vertices);
	}

	public float[] getTexCoords() {
		return toArray(texCoords);
	}

	public float[] getNormals() {
		return toArray(normals);
	}

	public int[] getIndices() {
		return indices.stream().mapToInt(i -> i).toArray();
	}

	private float[] toArray(List<Float> list) {
		float[] array = new float[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		return array;
	}

	public Mesh build() {
		return new Mesh(getVertices(), getTexCoords(), getNormals(), getIndices());
	}
}
