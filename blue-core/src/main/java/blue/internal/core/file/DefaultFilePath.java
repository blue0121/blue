package blue.internal.core.file;

import blue.core.file.FilePath;
import blue.core.util.AssertUtil;

import java.util.Collection;

/**
 * @author Jin Zheng
 * @since 1.0 2021-02-19
 */
public class DefaultFilePath implements FilePath
{
	private static final char CHAR_SLASH = SLASH.charAt(0);
	private static final char CHAR_BACKSLASH = BACKSLASH.charAt(0);

	private final String originalPath;
	private StringBuilder currentPath;

	public DefaultFilePath(String path)
	{
		AssertUtil.notEmpty(path, "Original Path");
		this.originalPath = path;
		this.currentPath = new StringBuilder(path);
	}

	private DefaultFilePath(String originalPath, StringBuilder currentPath)
	{
		this.originalPath = originalPath;
		this.currentPath = new StringBuilder(currentPath);
	}

	@Override
	public String getOriginalPath()
	{
		return originalPath;
	}

	@Override
	public String getCurrentPath()
	{
		return currentPath.toString();
	}

	@Override
	public String concat(Object...paths)
	{
		if (paths.length == 0)
			return currentPath.toString();

		for (Object path : paths)
		{
			currentPath.append(SLASH).append(path);
		}
		return this.trim();
	}

	@Override
	public String concat(Collection<?> paths)
	{
		if (paths == null || paths.isEmpty())
			return currentPath.toString();

		for (Object path : paths)
		{
			currentPath.append(SLASH).append(path);
		}
		return this.trim();
	}

	@Override
	public String trim()
	{
		StringBuilder trimPath = new StringBuilder(currentPath.length());
		char last = 0;
		for (int i = 0; i < currentPath.length(); i++)
		{
			char current = currentPath.charAt(i);
			if (current == CHAR_SLASH)
			{
				if (last == CHAR_SLASH)
				{
					continue;
				}
			}
			else if (current == CHAR_BACKSLASH)
			{
				if (last == CHAR_BACKSLASH)
				{
					continue;
				}
			}
			trimPath.append(current);
			last = current;
		}
		this.currentPath = trimPath;
		return trimPath.toString();
	}

	@Override
	public FilePath copy()
	{
		return new DefaultFilePath(this.originalPath, this.currentPath);
	}
}
