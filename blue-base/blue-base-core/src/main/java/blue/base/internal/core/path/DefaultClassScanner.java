package blue.base.internal.core.path;

import blue.base.core.path.ClassHandler;
import blue.base.core.path.ClassScanner;
import blue.base.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Queue;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * 类遍历默认实现
 *
 * @author Jin Zheng
 * @since 1.0 2021-01-15
 */
public class DefaultClassScanner implements ClassScanner {
	private static Logger logger = LoggerFactory.getLogger(DefaultClassScanner.class);

	private ClassHandler handler;

	public DefaultClassScanner(ClassHandler handler) {
		AssertUtil.notNull(handler, "类处理器");
		this.handler = handler;
	}

	/**
	 * 扫描包下的类文件
	 *
	 * @param isRecursive 是否扫描子包，true为扫描子包，false不扫描子包
	 * @param pkg         包
	 */
	@Override
	public void scan(boolean isRecursive, String pkg) {
		AssertUtil.notEmpty(pkg, "扫描包");

		this.scanPackage(isRecursive, pkg);
	}

	private void scanPackage(boolean isRecursive, String pkg) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String dir = ScanFileUtil.dotToSplash(pkg);
		Queue<String> queue = new LinkedList<>();
		queue.offer(dir);
		while ((dir = queue.poll()) != null) {
			try {
				Enumeration<URL> urls = loader.getResources(dir);
				while (urls.hasMoreElements()) {
					URL url = urls.nextElement();
					String protocol = url.getProtocol();

					if ("file".equals(protocol)) {
						this.scanFilePackage(loader, isRecursive, dir, queue, url);
					}
					else if ("jar".equals(protocol)) {
						this.scanJarPackage(loader, dir, url);
					}
				}
			}
			catch (IOException e) {
				logger.error("扫描包出错：" + dir, e);
			}
		}
	}

	private void scanJarPackage(ClassLoader loader, String dir, URL url) throws IOException {
		String jarFile = ScanFileUtil.getRootPath(url);
		logger.debug("扫描jar包：{}", jarFile);
		try (JarInputStream jis = new JarInputStream(new FileInputStream(jarFile))) {
			JarEntry entry = null;
			while ((entry = jis.getNextJarEntry()) != null) {
				String fileName = entry.getName();
				// 找到类
				if (fileName.startsWith(dir) && fileName.endsWith(".class")) {
					String clazz = ScanFileUtil.fileToClass(fileName);
					logger.debug("从jar包里找到类：{}", clazz);
					try {
						handler.handle(loader.loadClass(clazz));
					}
					catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void scanFilePackage(ClassLoader loader, boolean isRecursive, String dir,
	                             Queue<String> queue, URL url) {
		File[] files = new File(url.getFile()).listFiles(handler);
		if (files == null || files.length == 0) {
			return;
		}

		for (File file : files) {
			String fileName = file.getName();
			String d = ScanFileUtil.dotToSplash(dir);
			if (file.isDirectory() && isRecursive) {
				queue.offer(d + "/" + fileName);
				logger.debug("添加目录：{}", d);
				continue;
			}

			String clazz = ScanFileUtil.fileToClass(d + "/" + fileName);
			logger.debug("目录：{} - 找到类：{}", dir, clazz);

			try {
				handler.handle(loader.loadClass(clazz));
			}
			catch (ClassNotFoundException e) {
			}

		}
	}

}
