package blue.http.message;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 上传文件对象
 *
 * @author Jin Zheng
 * @since 1.0 2020-01-03
 */
public interface UploadFile
{
	/**
	 * 获取上传文件大小
	 *
	 * @return 上传文件大小
	 */
	int getSize();

	/**
	 * 获取上传文件的 Content-Type
	 *
	 * @return 上传文件的 Content-Type
	 */
	String getContentType();

	/**
	 * 获取上传文件原始名称
	 *
	 * @return 上传文件原始名称
	 */
	String getOriginalName();

	/**
	 * 获取上传文件
	 *
	 * @return 上传文件
	 */
	File getFile();

	/**
	 * 获取上传文件流
	 *
	 * @return 上传文件流
	 * @throws IOException IO异常时抛出
	 */
	InputStream getInputStream() throws IOException;
}
