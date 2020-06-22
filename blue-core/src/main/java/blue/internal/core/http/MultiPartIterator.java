package blue.internal.core.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Jin Zheng
 * @since 1.0 2020-04-30
 */
public class MultiPartIterator implements Iterator<byte[]>
{
	private final Iterator<MultiPartItem> iterator;
	private final String boundary;
	private final Charset charset;

	private boolean done;
	private byte[] next;
	private InputStream curInputStream;

	public MultiPartIterator(Collection<MultiPartItem> items, String boundary)
	{
		this(items, boundary, StandardCharsets.UTF_8);
	}

	public MultiPartIterator(Collection<MultiPartItem> items, String boundary, Charset charset)
	{
		this.iterator = items.iterator();
		this.boundary = boundary;
		this.charset = charset;
	}

	@Override
	public boolean hasNext()
	{
		if (done)
			return false;

		if (next != null)
			return true;

		try
		{
			next = computeNext();
		}
		catch (IOException e)
		{
			throw new UncheckedIOException(e);
		}
		if (next == null)
		{
			done = true;
			return false;
		}
		return true;
	}

	@Override
	public byte[] next()
	{
		if (!this.hasNext())
			throw new NoSuchElementException();

		byte[] buf = next;
		next = null;
		return buf;
	}

	private byte[] computeNext() throws IOException
	{
		if (curInputStream != null)
		{
			byte[] buf = new byte[MultiPartItem.BUFFER_SIZE];
			int read = curInputStream.read(buf);
			if (read > 0)
			{
				return Arrays.copyOf(buf, read);
			}
			else
			{
				curInputStream.close();
				curInputStream = null;
				return MultiPartItem.NEW_LINE.getBytes(charset);
			}
		}
		if (!iterator.hasNext())
			return null;

		MultiPartItem item = iterator.next();
		StringBuilder value = new StringBuilder(MultiPartItem.HEADER_SIZE);
		switch (item.getType())
		{
			case FINAL_BOUNDARY:
				value.append(MultiPartItem.BOUNDARY).append(boundary).append(MultiPartItem.NEW_LINE);
				break;
			case STRING:
				value.append(MultiPartItem.BOUNDARY).append(boundary).append(MultiPartItem.NEW_LINE);
				value.append("Content-Disposition: form-data; name=").append(item.getName()).append(MultiPartItem.NEW_LINE);
				value.append("Content-Type: text/plain; charset=").append(charset.name()).append(MultiPartItem.NEW_LINE);
				value.append(MultiPartItem.NEW_LINE).append(item.getValue()).append(MultiPartItem.NEW_LINE);
				break;
			case FILE:
				Path file = item.getPath();
				String filename = file.getFileName().toString();
				String contentType = Files.probeContentType(file);
				curInputStream = Files.newInputStream(file, StandardOpenOption.READ);
				this.buildFilePart(value, item.getName(), filename, contentType);
				break;
			case STREAM:
				curInputStream = item.getStream().get();
				this.buildFilePart(value, item.getName(), item.getFilename(), item.getContentType());
				break;
			default:
				return null;
		}
		return value.toString().getBytes(charset);
	}

	private void buildFilePart(StringBuilder value, String name, String filename, String contentType)
	{
		if (contentType == null || contentType.isEmpty())
		{
			contentType = MultiPartItem.CONTENT_TYPE;
		}
		value.append(MultiPartItem.BOUNDARY).append(boundary).append(MultiPartItem.NEW_LINE);
		value.append("Content-Disposition: form-data; name=").append(name);
		if (filename != null && !filename.isEmpty())
		{
			value.append("; filename=").append(filename);
		}
		value.append(MultiPartItem.NEW_LINE);
		value.append("Content-Type: ").append(contentType).append(MultiPartItem.NEW_LINE).append(MultiPartItem.NEW_LINE);
	}
}
