package blue.core.file;

import blue.core.util.AssertUtil;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 文件遍历
 * 
 * @author zhengj
 * @since 2015年9月14日 1.0
 */
public class FileScanner
{
	private final FileHandler handler;

	public FileScanner(FileHandler handler)
	{
		if (handler == null)
			throw new IllegalArgumentException("处理器不能为空");

		this.handler = handler;
	}

	/**
	 * 扫描目录及子目录下的文件
	 * 
	 * @param dir 目录
	 */
	public void scan(File dir)
	{
		this.scan(true, dir);
	}
	
	/**
	 * 扫描目录下的文件
	 * 
	 * @param isRecursive 是否扫描子目录，true为扫描子目录，false不扫描子目录
	 * @param dir 目录
	 */
	public void scan(boolean isRecursive, File dir)
	{
		AssertUtil.notNull(dir, "文件目录");
		this.scanDir(isRecursive, dir);
	}

	/**
	 * 扫描目录及子目录下的文件
	 * 
	 * @param dir 目录
	 */
	public void scanPath(String dir)
	{
		this.scanPath(true, dir);
	}

	/**
	 * 扫描目录下的文件
	 * 
	 * @param isRecursive 是否扫描子目录，true为扫描子目录，false不扫描子目录
	 * @param dir 目录
	 */
	public void scanPath(boolean isRecursive, String dir)
	{
		AssertUtil.notNull(dir, "文件目录");
		File file = new File(dir);
		if (!file.exists())
			throw new IllegalArgumentException(dir + " 不存在");
		
		this.scanDir(isRecursive, file);
	}
	
	/**
	 * 扫描目录及子目录下的文件
	 * 
	 * @param dirList 目录列表
	 */
	public void scan(List<File> dirList)
	{
		this.scan(dirList);
	}

	/**
	 * 扫描目录下的文件
	 * 
	 * @param isRecursive 是否扫描子目录，true为扫描子目录，false不扫描子目录
	 * @param dirList 目录列表
	 */
	public void scan(boolean isRecursive, List<File> dirList)
	{
		AssertUtil.notEmpty(dirList, "文件目录列表");
		for (File dir : dirList)
		{
			AssertUtil.notNull(dir, "文件目录");
			this.scanDir(isRecursive, dir);
		}
	}
	
	/**
	 * 扫描目录及子目录下的文件
	 * 
	 * @param dirList 目录列表
	 */
	public void scanPath(List<String> dirList)
	{
		this.scanPath(true, dirList);
	}
	
	/**
	 * 扫描目录下的文件
	 * 
	 * @param isRecursive 是否扫描子目录，true为扫描子目录，false不扫描子目录
	 * @param dirList 目录列表
	 */
	public void scanPath(boolean isRecursive, List<String> dirList)
	{
		AssertUtil.notEmpty(dirList, "文件目录列表");
		for (String str : dirList)
		{
			AssertUtil.notEmpty(str, "文件目录");
			File file = new File(str);
			if (!file.exists())
				throw new IllegalArgumentException(str + " 不存在");
			
			this.scanDir(isRecursive, file);
		}
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
			
			for (File file : dir.listFiles(handler))
			{
				if (dir.isFile())
				{
					handler.handle(dir);
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
