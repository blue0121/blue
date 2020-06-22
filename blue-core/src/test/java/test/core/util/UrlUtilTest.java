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
	
	
}
