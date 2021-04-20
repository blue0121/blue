package blue.base.core.file;

import java.io.File;
import java.io.FileFilter;

/**
 * 文件处理类
 *
 * @author zhengj
 * @since 2015年9月14日 1.0
 */
public interface FileHandler extends FileFilter {
	/**
	 * 处理文件
	 *
	 * @param file
	 */
	void handle(File file);

}
