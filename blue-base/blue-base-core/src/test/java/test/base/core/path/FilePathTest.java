package test.base.core.path;

import blue.base.core.path.FilePath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 1.0 2021-02-19
 */
public class FilePathTest
{
	public FilePathTest()
	{
	}

	@Test
	public void testConcat()
	{
		FilePath path = FilePath.create("/a");
		Assertions.assertEquals("/a/b", path.concat("b"));
		Assertions.assertEquals("/a/b", path.getCurrentPath());
		Assertions.assertEquals("/a/b/c/d", path.concat("c", "//d"));
		Assertions.assertEquals("/a/b/c/d", path.getCurrentPath());
		Assertions.assertEquals("/a", path.getOriginalPath());
	}

	@Test
	public void testTrim()
	{
		FilePath path = FilePath.create("a//b");
		Assertions.assertEquals("a/b", path.trim());
		Assertions.assertEquals("a/b", path.getCurrentPath());
	}

}
