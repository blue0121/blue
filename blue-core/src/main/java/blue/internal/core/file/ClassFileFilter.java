package blue.internal.core.file;

import java.io.File;
import java.io.FileFilter;

public class ClassFileFilter implements FileFilter
{
	public ClassFileFilter()
	{
	}

	@Override
	public boolean accept(File path)
	{
		if (path.isDirectory())
			return true;
		
		return path.isFile() && path.getName().endsWith(".class");
	}
}
