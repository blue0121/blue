package blue.core.message;

import java.nio.charset.Charset;

/**
 * @author Jin Zheng
 * @since 2020-02-11
 */
public class ByteValue
{
	private final byte[] bytes;

	public ByteValue(byte[] bytes)
	{
		this.bytes = bytes;
	}

	public ByteValue(String string)
	{
		this.bytes = string.getBytes();
	}

	public byte[] getBytes()
	{
		return bytes;
	}

	public String getString()
	{
		return new String(bytes);
	}

	public String getString(Charset charset)
	{
		return new String(bytes, charset);
	}
}
