package blue.base.core.file;

import blue.base.core.util.AssertUtil;
import blue.base.internal.core.file.DefaultFileScanner;

import java.io.File;
import java.util.List;

/**
 * 文件遍历
 *
 * @author Jin Zheng
 * @since 1.0 2021-01-15
 */
public interface FileScanner {
	/**
	 * 创建文件遍历器
	 *
	 * @param handler 文件处理器，不能为空
	 * @return
	 */
	static FileScanner create(FileHandler handler) {
		return new DefaultFileScanner(handler);
	}

	/**
	 * 扫描目录及子目录下的文件
	 *
	 * @param dir 目录
	 */
	default void scan(File dir) {
		this.scan(true, dir);
	}

	/**
	 * 扫描目录下的文件
	 *
	 * @param isRecursive 是否扫描子目录，true为扫描子目录，false不扫描子目录
	 * @param dir         目录
	 */
	void scan(boolean isRecursive, File dir);

	/**
	 * 扫描目录及子目录下的文件
	 *
	 * @param dir 目录
	 */
	default void scanPath(String dir) {
		this.scanPath(true, dir);
	}

	/**
	 * 扫描目录下的文件
	 *
	 * @param isRecursive 是否扫描子目录，true为扫描子目录，false不扫描子目录
	 * @param dir         目录
	 */
	default void scanPath(boolean isRecursive, String dir) {
		AssertUtil.notEmpty(dir, "文件目录");
		File dirFile = new File(dir);
		AssertUtil.isTrue(dirFile.exists(), dir + " 不存在");
		this.scan(isRecursive, dirFile);
	}

	/**
	 * 扫描目录及子目录下的文件
	 *
	 * @param dirList 目录列表
	 */
	default void scan(List<File> dirList) {
		AssertUtil.notEmpty(dirList, "文件目录列表");
		dirList.forEach(e -> this.scan(true, e));
	}

	/**
	 * 扫描目录下的文件
	 *
	 * @param isRecursive 是否扫描子目录，true为扫描子目录，false不扫描子目录
	 * @param dirList     目录列表
	 */
	default void scan(boolean isRecursive, List<File> dirList) {
		AssertUtil.notEmpty(dirList, "文件目录列表");
		dirList.forEach(e -> this.scan(isRecursive, e));
	}

	/**
	 * 扫描目录及子目录下的文件
	 *
	 * @param dirList 目录列表
	 */
	default void scanPath(List<String> dirList) {
		AssertUtil.notEmpty(dirList, "文件目录列表");
		dirList.forEach(e -> this.scanPath(true, e));
	}

	/**
	 * 扫描目录下的文件
	 *
	 * @param isRecursive 是否扫描子目录，true为扫描子目录，false不扫描子目录
	 * @param dirList     目录列表
	 */
	default void scanPath(boolean isRecursive, List<String> dirList) {
		AssertUtil.notEmpty(dirList, "文件目录列表");
		dirList.forEach(e -> this.scanPath(isRecursive, e));
	}

}
