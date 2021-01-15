package blue.core.file;

import java.io.File;
import java.io.FileFilter;

/**
 * 类处理器
 * 
 * @author zhengj
 * @since 1.0 2016年7月21日
 */
public interface ClassHandler extends FileFilter
{
	/**
	 * 处理类
	 * 
	 * @param clazz 类型
	 */
	void handle(Class<?> clazz);

	/**
	 * 类过滤器默认实现
	 *
	 * @param path
	 * @return
	 */
	@Override
	default boolean accept(File path)
	{
		if (path.isDirectory())
			return true;

		return path.isFile() && path.getName().endsWith(".class");
	}
}
