package test.core.security;

import blue.core.security.AESUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestAESUtil
{
	public TestAESUtil()
	{
	}
	
	@Test
	public void testAES()
	{
		String password = "password";
		String data = "test_password";
		
		String raw = AESUtil.encrypt(password, data);
		System.out.println(raw);
		String data2 = AESUtil.decrypt(password, raw);
		System.out.println(data2);
		Assertions.assertEquals(data, data2, "解密后的字符串和原来的不同");
	}

	@Test
	public void test()
	{
		String password = "password";
		String data = "root";
		String raw = AESUtil.encrypt(password, data);
		System.out.println(raw);
	}

}
