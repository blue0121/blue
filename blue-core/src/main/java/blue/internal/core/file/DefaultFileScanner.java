package blue.internal.core.file;

import blue.core.file.FileHandler;
import blue.core.file.FileScanner;
import blue.core.util.AssertUtil;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 文件遍历默认实现
 * 
 * @author zhengj
 * @since 2015年9月14日 1.0
 */
public class DefaultFileScanner implements FileScanner
{
	private final FileHandler handler;

	public DefaultFileScanner(FileHandler handler)
	{
		AssertUtil.notNull(handler, "处理器");
		this.handler = handler;
	}
	
	/**
	 * 扫描目录下的文件
	 * 
	 * @param isRecursive 是否扫描子目录，true为扫描子目录，false不扫描子目录
	 * @param dir 目录
	 */
	@Override
	public void scan(boolean isRecursive, File dir)
	{
		AssertUtil.notNull(dir, "文件目录");
		this.scanDir(isRecursive, dir);
	}
	
	/**
	 * 扫描目录下的文件
	 * 
	 * @param isRecursive 是否扫描子目录，true为扫描子目录，false不扫描子目录
	 * @param dir 目录
	 */
	private void scanDir(boolean isRecursive, File dir)
	{
		Queue<File> queue = new LinkedList<>();
		queue.offer(dir);
		while ((dir = queue.poll()) != null)
		{
			if (dir.isFile())
			{
				handler.handle(dir);
				continue;
			}
			File[] files = dir.listFiles(handler);
			if (files == null || files.length == 0)
				continue;

			for (File file : files)
			{
				if (file.isFile())
				{
					handler.handle(file);
					continue;
				}
				
				if (isRecursive)
				{
					queue.offer(file);
				}
			}
		}
	}

}
