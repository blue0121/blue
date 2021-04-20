package blue.base.core.file;

import blue.base.internal.core.file.DefaultFilePath;

import java.util.Collection;

/**
 * @author Jin Zheng
 * @since 1.0 2021-02-19
 */
public interface FilePath {

	String SLASH = "/";

	String BACKSLASH = "\\";

	/**
	 * create
	 *
	 * @param path
	 * @return
	 */
	static FilePath create(String path) {
		return new DefaultFilePath(path);
	}

	/**
	 * get original path
	 *
	 * @return
	 */
	String getOriginalPath();

	/**
	 * get current path
	 *
	 * @return
	 */
	String getCurrentPath();

	/**
	 * concat path and trim
	 *
	 * @param paths
	 * @return
	 */
	String concat(Object... paths);

	/**
	 * concat path and trim
	 *
	 * @param paths
	 * @return
	 */
	String concat(Collection<?> paths);

	/**
	 * trim multi '/' or '\'
	 *
	 * @return
	 */
	String trim();

	/**
	 * copy
	 *
	 * @return
	 */
	FilePath copy();

}
