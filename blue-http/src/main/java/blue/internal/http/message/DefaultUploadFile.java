package blue.internal.http.message;

import blue.core.id.IdGenerator;
import blue.core.util.UrlUtil;
import blue.http.message.UploadFile;
import io.netty.handler.codec.http.multipart.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 上传文件对象的默认实现
 * 
 * @author zhengj
 * @since 1.0 2017年3月27日
 */
public class DefaultUploadFile implements UploadFile
{
	private static Logger logger = LoggerFactory.getLogger(DefaultUploadFile.class);
	
	private int size;
	private File file;
	private String contentType;
	private String originalName;
	
	public DefaultUploadFile(FileUpload fileUpload) throws IOException
	{
		contentType = fileUpload.getContentType();
		originalName = fileUpload.getFilename();
		if (!fileUpload.isInMemory())
		{
			file = fileUpload.getFile();
			if (file != null)
			{
				size = (int) file.length();
			}
			return;
		}
		String dir = UrlUtil.getTempDir();
		String ext = UrlUtil.getFileExt(originalName);
		file = new File(dir + IdGenerator.uuid12bit() + ext);
		size = fileUpload.get().length;
		try (FileOutputStream fos = new FileOutputStream(file))
		{
			fos.write(fileUpload.get());
			fos.flush();
		}
		logger.info("Write file：{} - {} bytes", file.getPath(), size);
	}

	@Override
	public int getSize()
	{
		return size;
	}

	@Override
	public String getContentType()
	{
		return contentType;
	}

	@Override
	public String getOriginalName()
	{
		return originalName;
	}

	@Override
	public File getFile()
	{
		return file;
	}

	@Override
	public InputStream getInputStream() throws IOException
	{
		return new FileInputStream(file);
	}

	@Override
	public String toString()
	{
		return String.format("%s [%s] - %s [%d bytes]", originalName, contentType, file, size);
	}
	
	
}
