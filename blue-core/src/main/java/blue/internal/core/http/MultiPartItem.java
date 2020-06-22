package blue.internal.core.http;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.Supplier;

/**
 * @author Jin Zheng
 * @since 1.0 2020-04-30
 */
public class MultiPartItem
{
	public static final String BOUNDARY = "--";
	public static final int BUFFER_SIZE = 8192;
	public static final int HEADER_SIZE = 512;
	public static final String NEW_LINE = "\r\n";
	public static final String CONTENT_TYPE = "application/octet-stream";

	private MultiPartType type;
	private String name;
	private String value;
	private Path path;
	private Supplier<InputStream> stream;
	private String filename;
	private String contentType;

	public MultiPartItem()
	{
	}

	public MultiPartItem(String name, String value)
	{
		this.type = MultiPartType.STRING;
		this.name = name;
		this.value = value;
	}

	public MultiPartItem(String name, Path path)
	{
		this.type = MultiPartType.FILE;
		this.name = name;
		this.path = path;
	}

	public MultiPartItem(String name, Supplier<InputStream> stream, String filename, String contentType)
	{
		this.type = MultiPartType.STREAM;
		this.name = name;
		this.stream = stream;
		this.filename = filename;
		this.contentType = contentType;
	}

	public MultiPartType getType()
	{
		return type;
	}

	public void setType(MultiPartType type)
	{
		this.type = type;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public Path getPath()
	{
		return path;
	}

	public void setPath(Path path)
	{
		this.path = path;
	}

	public Supplier<InputStream> getStream()
	{
		return stream;
	}

	public void setStream(Supplier<InputStream> stream)
	{
		this.stream = stream;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public String getContentType()
	{
		return contentType;
	}

	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}
}
