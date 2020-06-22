package test.core.security;

import blue.core.security.DigestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author zhengjin
 * @since 1.0 2018年10月07日
 */
public class DigestUtilTest
{
	private String src = "半糖主义";

	public DigestUtilTest()
	{
	}

	@Test
	public void testMd5Hex()
	{
		Assertions.assertNull(DigestUtil.md5Hex(null));

		String md5 = DigestUtil.md5Hex(src);
		Assertions.assertNotNull(md5);
		Assertions.assertEquals(32, md5.length());
	}

	@Test
	public void testMd5()
	{
		Assertions.assertNull(DigestUtil.md5(null));

		byte[] md5 = DigestUtil.md5(src);
		Assertions.assertNotNull(md5);
		Assertions.assertEquals(16, md5.length);
	}

	@Test
	public void testEqualMd5()
	{
		String md5 = DigestUtil.md5Hex(src);
		String md = DigestUtil.md5Hex("blue");
		Assertions.assertFalse(DigestUtil.equalMd5(null, null));
		Assertions.assertFalse(DigestUtil.equalMd5(src, null));
		Assertions.assertFalse(DigestUtil.equalMd5(null, md5));
		Assertions.assertTrue(DigestUtil.equalMd5(src, md5));
		Assertions.assertFalse(DigestUtil.equalMd5(src, md));
	}

	@Test
	public void testSha1Hex()
	{
		Assertions.assertNull(DigestUtil.sha1Hex(null));

		String sha1 = DigestUtil.sha1Hex(src);
		Assertions.assertNotNull(sha1);
		Assertions.assertEquals(40, sha1.length());
	}

	@Test
	public void testSha1()
	{
		Assertions.assertNull(DigestUtil.sha1(null));

		byte[] sha1 = DigestUtil.sha1(src);
		Assertions.assertNotNull(sha1);
		Assertions.assertEquals(20, sha1.length);
	}

	@Test
	public void testEqualSha1()
	{
		String sha1 = DigestUtil.sha1Hex(src);
		String md = DigestUtil.sha1Hex("blue");
		Assertions.assertFalse(DigestUtil.equalSha1(null, null));
		Assertions.assertFalse(DigestUtil.equalSha1(src, null));
		Assertions.assertFalse(DigestUtil.equalSha1(null, sha1));
		Assertions.assertTrue(DigestUtil.equalSha1(src, sha1));
		Assertions.assertFalse(DigestUtil.equalSha1(src, md));
	}

}
