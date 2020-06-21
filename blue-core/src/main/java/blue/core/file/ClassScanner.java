package blue.core.file;

import blue.core.util.AssertUtil;
import blue.internal.core.file.ClassFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class ClassScanner
{
	private static Logger logger = LoggerFactory.getLogger(ClassScanner.class);

	private FileFilter fileFilter;
	private ClassHandler classHandler;

	public ClassScanner()
	{
		this.fileFilter = new ClassFileFilter();
	}

	/**
	 * 扫描包及子包下的类文件
	 *
	 * @param pkg 包
	 */
	public void scan(String pkg)
	{
		this.scan(true, pkg);
	}

	/**
	 * 扫描包下的类文件
	 *
	 * @param isRecursive 是否扫描子包，true为扫描子包，false不扫描子包
	 * @param pkg 包
	 */
	public void scan(boolean isRecursive, String pkg)
	{
		AssertUtil.notEmpty(pkg, "扫描包");
		AssertUtil.notNull(classHandler, "类处理器");

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		this.scanPackage(loader, isRecursive, pkg);
	}

	/**
	 * 扫描包下的类文件
	 *
	 * @param packageList 包列表
	 */
	public void scan(List<String> packageList)
	{
		this.scan(true, packageList);
	}

	/**
	 * 扫描包下的类文件
	 *
	 * @param isRecursive 是否扫描子包，true为扫描子包，false不扫描子包
	 * @param packageList 包列表
	 */
	public void scan(boolean isRecursive, List<String> packageList)
	{
		AssertUtil.notEmpty(packageList, "扫描包列表");
		AssertUtil.notNull(classHandler, "类处理器");

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		for (String pkg : packageList)
		{
			AssertUtil.notEmpty(pkg, "扫描包");
			this.scanPackage(loader, isRecursive, pkg);
		}
	}


	private void scanPackage(ClassLoader loader, boolean isRecursive, String pkg)
	{
		String dir = ScanFileUtil.dotToSplash(pkg);
		Queue<String> queue = new LinkedList<>();
		queue.offer(dir);
		while ((dir = queue.poll()) != null)
		{
			try
			{
				Enumeration<URL> urls = loader.getResources(dir);
				while (urls.hasMoreElements())
				{
					URL url = urls.nextElement();
					String protocol = url.getProtocol();

					if ("file".equals(protocol))
					{
						this.scanFilePackage(loader, isRecursive, dir, queue, url);
					}
					else if ("jar".equals(protocol))
					{
						this.scanJarPackage(loader, dir, url);
					}
				}
			}
			catch (IOException e)
			{
				logger.error("扫描包出错：" + dir, e);
			}
		}
	}

	private void scanJarPackage(ClassLoader loader, String dir, URL url) throws IOException
	{
		String jarFile = ScanFileUtil.getRootPath(url);
		logger.debug("扫描jar包：{}", jarFile);
		try (JarInputStream jis = new JarInputStream(new FileInputStream(jarFile)))
		{
			JarEntry entry = null;
			while ((entry = jis.getNextJarEntry()) != null)
			{
				String fileName = entry.getName();
				// 找到类
				if (fileName.startsWith(dir) && fileName.endsWith(".class"))
				{
					String clazz = ScanFileUtil.fileToClass(fileName);
					logger.debug("从jar包里找到类：{}", clazz);
					try
					{
						classHandler.handle(loader.loadClass(clazz));
					}
					catch (ClassNotFoundException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void scanFilePackage(ClassLoader loader, boolean isRecursive, String dir,
			Queue<String> queue, URL url)
	{
		for (File file : new File(url.getFile()).listFiles(fileFilter))
		{
			String fileName = file.getName();
			String d = ScanFileUtil.dotToSplash(dir);
			if (file.isDirectory() && isRecursive)
			{
				queue.offer(d + "/" + fileName);
				logger.debug("添加目录：{}", d);
				continue;
			}

			String clazz = ScanFileUtil.fileToClass(d + "/" + fileName);
			logger.debug("目录：{} - 找到类：{}", dir, clazz);

			try
			{
				classHandler.handle(loader.loadClass(clazz));
			}
			catch (ClassNotFoundException e)
			{
			}

		}
	}


	public void setFileFilter(FileFilter fileFilter)
	{
		this.fileFilter = fileFilter;
	}

	public void setClassHandler(ClassHandler classHandler)
	{
		this.classHandler = classHandler;
	}

}
