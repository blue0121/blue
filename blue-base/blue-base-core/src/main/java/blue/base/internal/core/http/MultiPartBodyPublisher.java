package blue.base.internal.core.http;

import java.io.InputStream;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * @author Jin Zheng
 * @since 1.0 2020-04-30
 */
public class MultiPartBodyPublisher {
	private static final String BOUNDARY = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private final List<MultiPartItem> itemList = new ArrayList<>();
	private final Charset charset;
	private final String boundary;

	public MultiPartBodyPublisher() {
		this(StandardCharsets.UTF_8, null);
	}

	public MultiPartBodyPublisher(String boundary) {
		this(StandardCharsets.UTF_8, boundary);
	}

	public MultiPartBodyPublisher(Charset charset) {
		this(charset, null);
	}

	public MultiPartBodyPublisher(Charset charset, String boundary) {
		this.charset = charset;
		if (boundary == null && boundary.isEmpty()) {
			this.boundary = this.generateBoundary();
		}
		else {
			this.boundary = boundary;
		}
	}

	public HttpRequest.BodyPublisher build() {
		if (itemList.isEmpty()) {
			throw new IllegalArgumentException("Empty MultiPart");
		}

		this.addFinalBoundary();
		MultiPartIterator iterator = new MultiPartIterator(itemList, boundary);
		return HttpRequest.BodyPublishers.ofByteArrays(new MultiPartIterable(iterator));
	}

	public MultiPartBodyPublisher addPart(String name, String value) {
		MultiPartItem item = new MultiPartItem(name, value);
		itemList.add(item);
		return this;
	}

	public MultiPartBodyPublisher addPart(String name, Path file) {
		MultiPartItem item = new MultiPartItem(name, file);
		itemList.add(item);
		return this;
	}

	public MultiPartBodyPublisher addPart(String name, Supplier<InputStream> stream, String filename, String contentType) {
		MultiPartItem item = new MultiPartItem(name, stream, filename, contentType);
		itemList.add(item);
		return this;
	}

	private void addFinalBoundary() {
		MultiPartItem item = new MultiPartItem();
		item.setType(MultiPartType.FINAL_BOUNDARY);
		itemList.add(item);
	}

	private String generateBoundary() {
		StringBuilder buffer = new StringBuilder();
		Random rand = new Random();
		int count = rand.nextInt(11) + 30;

		for (int i = 0; i < count; ++i) {
			int index = rand.nextInt(BOUNDARY.length());
			buffer.append(BOUNDARY.charAt(index));
		}
		return buffer.toString();
	}

	public String getBoundary() {
		return boundary;
	}
}
