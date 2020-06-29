package blue.internal.http.message;

import blue.http.message.Download;

import java.io.File;

/**
 * @author zhengjin
 * @since 1.0 2018年03月26日
 */
public class DefaultDownload implements Download
{
	private Type type;
	private byte[] memory;
	private File file;
	private String filename;
	private boolean download;

	public DefaultDownload()
	{
	}

	@Override
	public Type type()
	{
		return type;
	}

	@Override
	public byte[] memory()
	{
		return memory;
	}

	@Override
	public String filename()
	{
		return filename;
	}

	@Override
	public File file()
	{
		return file;
	}

	@Override
	public boolean isDownload()
	{
		return download;
	}

	public void setType(Type type)
	{
		this.type = type;
	}

	public void setMemory(byte[] memory)
	{
		this.memory = memory;
	}

	public void setFile(File file)
	{
		this.file = file;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public void setDownload(boolean download)
	{
		this.download = download;
	}
}
