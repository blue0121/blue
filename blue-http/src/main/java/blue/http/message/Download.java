package blue.http.message;

import blue.internal.http.message.DownloadBuilder;

import java.io.File;

/**
 * @author zhengjin
 * @since 1.0 2018年03月26日
 */
public interface Download
{
	enum Type
	{
		/**
		 * 内存
		 */
		MEMORY,

		/**
		 * 文件
		 */
		FILE
	}

	/**
	 * 获取输出流类型
	 */
	Type type();

	/**
	 * 获取内存字节数组
	 */
	byte[] memory();

	/**
	 * 获取下载文件名称
	 */
	String filename();

	/**
	 * 获取文件
	 */
	File file();

	/**
	 * 是否为文件下载
	 */
	boolean isDownload();

	static DownloadBuilder createBuilder()
	{
		return new DownloadBuilder();
	}

}
