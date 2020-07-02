package blue.http.message;

import blue.core.id.IdGenerator;
import blue.core.util.UrlUtil;
import blue.http.exception.HttpServerException;
import blue.internal.http.message.DefaultDownload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;

/**
 * @author zhengjin
 * @since 1.0 2018年03月26日
 */
public class DownloadBuilder
{
	private static Logger logger = LoggerFactory.getLogger(DownloadBuilder.class);
	
	private Download.Type type;
	private byte[] memory;
	private File file;
	private String filename;
	private boolean download = false;
	private boolean handled = false;

	DownloadBuilder()
	{
	}

	public Download build()
	{
		DefaultDownload dd = new DefaultDownload();
		dd.setDownload(download);
		dd.setFile(file);
		dd.setFilename(filename);
		dd.setMemory(memory);
		dd.setType(type);
		return dd;
	}

	public DownloadBuilder handle(Download.Type type, Consumer<OutputStream> f)
	{
		if (handled)
			throw new HttpServerException("handle() 只能调用一次");

		handled = true;
		this.type = (type == null ? Download.Type.MEMORY : type);

		if (type == Download.Type.MEMORY)
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			f.accept(baos);
			memory = baos.toByteArray();
		}
		else if (type == Download.Type.FILE)
		{
			String tmp = UrlUtil.getTempDir() + IdGenerator.uuid12bit() + ".tmp";
			try (FileOutputStream fos = new FileOutputStream(tmp))
			{
				f.accept(fos);
			}
			catch (IOException e)
			{
				logger.error("写入文件输出流错误", e);
			}
			file = new File(tmp);
		}
		return this;
	}

	public DownloadBuilder setFile(File file)
	{
		Assert.notNull(file, "文件不能为空");
		if (this.file != null)
			throw new HttpServerException("setFile() 方法只能调用一次");

		this.file = file;
		return this;
	}

	public DownloadBuilder setFilename(String filename)
	{
		this.filename = filename;
		return this;
	}

	public DownloadBuilder setDownload(boolean download)
	{
		this.download = download;
		return this;
	}
}
