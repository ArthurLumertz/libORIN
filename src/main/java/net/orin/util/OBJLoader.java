package net.orin.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.orin.graphics.g3d.mesh.Mesh;
import net.orin.io.FileRef;

public class OBJLoader {

	private final List<float[]> tempVertices = new ArrayList<>();
	private final List<float[]> tempTexcoords = new ArrayList<>();
	private final List<float[]> tempNormals = new ArrayList<>();

	private final List<Integer> indices = new ArrayList<>();
	private final List<float[]> finalVertices = new ArrayList<>();
	private final List<float[]> finalTexcoords = new ArrayList<>();
	private final List<float[]> finalNormals = new ArrayList<>();

	private final Map<String, Integer> indexMap = new HashMap<>();

	public Mesh parse(String filePath) {
		return parse(FileRef.internal(filePath));
	}

	public Mesh parse(FileRef ref) {
		String data = ref.readString();
		String[] lines = data.split("\r?\n");

		tempVertices.clear();
		tempTexcoords.clear();
		tempNormals.clear();

		indices.clear();
		finalVertices.clear();
		finalTexcoords.clear();
		finalNormals.clear();

		indexMap.clear();
		int nextIndex = 0;

		for (String line : lines) {
			line = line.trim();
			if (line.isEmpty() || line.startsWith("#")) {
				continue;
			}

			String[] tokens = line.split("\\s+");
			if (tokens.length == 0) {
				continue;
			}

			switch (tokens[0]) {
			case "v":
				float vx = Float.parseFloat(tokens[1]);
				float vy = Float.parseFloat(tokens[2]);
				float vz = Float.parseFloat(tokens[3]);
				tempVertices.add(new float[] { vx, vy, vz });
				break;

			case "vt":
				float tx = Float.parseFloat(tokens[1]);
				float ty = Float.parseFloat(tokens[2]);
				tempTexcoords.add(new float[] { tx, ty });
				break;

			case "vn":
				float nx = Float.parseFloat(tokens[1]);
				float ny = Float.parseFloat(tokens[2]);
				float nz = Float.parseFloat(tokens[3]);
				tempNormals.add(new float[] { nx, ny, nz });
				break;

			case "f":
				for (int i = 1; i < tokens.length - 2; i++) {
					String[] faceVerts = { tokens[1], tokens[i + 1], tokens[i + 2] };
					for (String fv : faceVerts) {
						if (!indexMap.containsKey(fv)) {
							int[] vi = parseFaceVertex(fv, tempVertices.size(), tempTexcoords.size(),
									tempNormals.size());

							float[] pos = vi[0] >= 0 ? tempVertices.get(vi[0]) : new float[] { 0, 0, 0 };
							finalVertices.add(pos);

							float[] tex = (vi[1] >= 0 && vi[1] < tempTexcoords.size()) ? tempTexcoords.get(vi[1])
									: new float[] { 0, 0 };
							finalTexcoords.add(tex);

							float[] norm = (vi[2] >= 0 && vi[2] < tempNormals.size()) ? tempNormals.get(vi[2])
									: new float[] { 0, 0, 0 };
							finalNormals.add(norm);

							indexMap.put(fv, nextIndex++);
						}
						indices.add(indexMap.get(fv));
					}
				}
				break;

			default:
				break;
			}
		}

		float[] vertexArray = new float[finalVertices.size() * 3];
		float[] texcoordArray = new float[finalTexcoords.size() * 2];
		float[] normalArray = new float[finalNormals.size() * 3];
		int[] indexArray = new int[indices.size()];

		for (int i = 0; i < finalVertices.size(); i++) {
			float[] v = finalVertices.get(i);
			vertexArray[i * 3] = v[0];
			vertexArray[i * 3 + 1] = v[1];
			vertexArray[i * 3 + 2] = v[2];

			float[] t = finalTexcoords.get(i);
			texcoordArray[i * 2] = t[0];
			texcoordArray[i * 2 + 1] = t[1];

			float[] n = finalNormals.get(i);
			normalArray[i * 3] = n[0];
			normalArray[i * 3 + 1] = n[1];
			normalArray[i * 3 + 2] = n[2];
		}
		
		for (int i = 0; i < indices.size(); i++) {
			indexArray[i] = indices.get(i);
		}

		return new Mesh(vertexArray, texcoordArray, normalArray, indexArray);
	}

	private int[] parseFaceVertex(String fv, int vCount, int vtCount, int vnCount) {
		int v = -1, vt = -1, vn = -1;
		String[] parts = fv.split("/");
		try {
			if (parts.length >= 1 && !parts[0].isEmpty()) {
				v = Integer.parseInt(parts[0]) - 1;
				if (v < 0)
					v += vCount + 1;
			}
			if (parts.length >= 2 && !parts[1].isEmpty()) {
				vt = Integer.parseInt(parts[1]) - 1;
				if (vt < 0)
					vt += vtCount + 1;
			}
			if (parts.length == 3 && !parts[2].isEmpty()) {
				vn = Integer.parseInt(parts[2]) - 1;
				if (vn < 0)
					vn += vnCount + 1;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return new int[] { v, vt, vn };
	}
}
