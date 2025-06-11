package net.orin.io;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileRef {

	private final FileType fileType;
	private final String filePath;
	private final File file;
	private final InputStream inputStream;

	public static FileRef internal(String filePath) {
		return new FileRef(filePath, FileType.INTERNAL);
	}

	public static FileRef external(String filePath) {
		return new FileRef(filePath, FileType.EXTERNAL);
	}

	public static FileRef absolute(String filePath) {
		return new FileRef(filePath, FileType.ABSOLUTE);
	}

	public FileRef(String filePath, FileType fileType) {
		switch (fileType) {
		case INTERNAL:
			if (!filePath.startsWith("/")) {
				filePath = "/" + filePath;
			}
			this.inputStream = getClass().getResourceAsStream(filePath);
			this.file = null;
			break;
		case EXTERNAL:
			this.file = new File(System.getProperty("user.home"), filePath);
			this.inputStream = null;
			break;
		case ABSOLUTE:
			this.file = new File(filePath);
			this.inputStream = null;
			break;
		default:
			throw new IllegalArgumentException("Unsupported file type: " + fileType);
		}

		this.fileType = fileType;
		this.filePath = filePath;
	}

	public boolean exists() {
		if (fileType == FileType.INTERNAL) {
			return inputStream != null;
		} else {
			return file.exists();
		}
	}

	public InputStream read() throws FileNotFoundException {
		switch (fileType) {
			case INTERNAL:
				InputStream stream = getClass().getResourceAsStream(filePath);
				if (stream == null)
					throw new FileNotFoundException("Internal file not found: " + filePath);
				return stream;
			case EXTERNAL:
			case ABSOLUTE:
				return new FileInputStream(file);
			default:
				throw new IllegalStateException("Unsupported file type: " + fileType);
		}
	}

	public byte[] readAllBytes() {
		try (InputStream in = read();
			 ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
			byte[] data = new byte[4096];
			int n;
			while ((n = in.read(data)) != -1) {
				buffer.write(data, 0, n);
			}
			return buffer.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String readString() {
		try (InputStream in = read();
			 BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
			StringBuilder out = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				out.append(line).append("\n");
			}
			return out.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void writeBytes(byte[] bytes) throws IOException {
		if (fileType == FileType.INTERNAL) {
			throw new IOException("Cannot write to INTERNAL file");
		}
		File parent = file.getParentFile();
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}
		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(bytes);
		}
	}

	public void writeString(String content) throws IOException {
		writeBytes(content.getBytes(StandardCharsets.UTF_8));
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public File getFile() {
		return file;
	}

	public FileType getType() {
		return fileType;
	}

	public String path() {
		return filePath;
	}

	@Override
	public String toString() {
		return "FileRef{" +
				"fileType=" + fileType +
				", filePath='" + filePath + '\'' +
				", file=" + file +
				", inputStream=" + inputStream +
				'}';
	}
}
