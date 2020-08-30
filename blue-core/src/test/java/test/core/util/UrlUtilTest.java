package test.core.util;

import blue.core.util.UrlUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UrlUtilTest
{
	public UrlUtilTest()
	{
	}
	
	@Test
	public void testMatch()
	{
		Assertions.assertTrue(UrlUtil.match("/token", "/*"), "URL匹配错误");
		Assertions.assertTrue(UrlUtil.match("/token", "/token"), "URL匹配错误");
		Assertions.assertTrue(UrlUtil.match("/token", "/to*"), "URL匹配错误");
		Assertions.assertFalse(UrlUtil.match("/app/user_list", "/app"), "URL匹配错误");
		Assertions.assertTrue(UrlUtil.match("/app/user_list", "/app/*"), "URL匹配错误");
	}

	@Test
	public void testConcat()
	{
		Assertions.assertNull(UrlUtil.concat());
		Assertions.assertEquals("a/b/c/d", UrlUtil.concat("a", "b", "c", "/d"));
		Assertions.assertEquals("a/b/c", UrlUtil.concat("a", "b", "////c"));
		Assertions.assertEquals("a/b/c", UrlUtil.concat("a", "////b/", "////c"));
		Assertions.assertEquals("/a/b/c", UrlUtil.concat("/", "//a", "/b/", "////c"));
	}

	@Test
	public void testTrim()
	{
		Assertions.assertEquals("/abc/ced", UrlUtil.trim("/abc/ced"));
		Assertions.assertEquals("/abc/ced", UrlUtil.trim("/abc//ced"));
		Assertions.assertEquals("/abc/ced", UrlUtil.trim("//abc/ced"));
		Assertions.assertEquals("/abc/ced", UrlUtil.trim("//abc//ced"));
		Assertions.assertEquals("/abc/ced", UrlUtil.trim("//abc/////ced"));
		Assertions.assertEquals("/", UrlUtil.trim("//"));

		Assertions.assertEquals("\\abc\\ced", UrlUtil.trim("\\abc\\ced"));
		Assertions.assertEquals("\\abc\\ced", UrlUtil.trim("\\abc\\\\ced"));
		Assertions.assertEquals("\\abc\\ced", UrlUtil.trim("\\\\abc\\ced"));
		Assertions.assertEquals("\\abc\\ced", UrlUtil.trim("\\\\abc\\\\\\\\ced"));
		Assertions.assertEquals("\\", UrlUtil.trim("\\\\"));
	}
	
}
