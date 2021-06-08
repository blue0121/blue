package test.base.core.security;

import blue.base.core.security.AesCipher;
import blue.base.core.security.CipherMode;
import blue.base.core.security.SecurityFactory;
import blue.base.core.util.StringUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Jin Zheng
 * @since 2021-06-07
 */
public class AesCipherTest {
	private String data1 = "我的电脑-jin.zheng";
	private String pwd1 = "password";

	public AesCipherTest() {
	}

	@Test
	public void testGenerateKey() {
		AesCipher cipher = SecurityFactory.createAesCipher(CipherMode.CTR);
		byte[] key = cipher.generateKey();
		byte[] raw1 = cipher.encrypt(key, data1.getBytes(StandardCharsets.UTF_8));
		String raw1Base64 = Base64.getEncoder().encodeToString(raw1);
		System.out.println("key: " + raw1Base64);
		byte[] src1 = cipher.decrypt(key, raw1);
		Assertions.assertEquals(data1, new String(src1, StandardCharsets.UTF_8));
	}

	private void testMode(CipherMode mode) {
		AesCipher cipher = SecurityFactory.createAesCipher(mode);
		byte[] raw1 = cipher.encrypt(pwd1, data1.getBytes(StandardCharsets.UTF_8));
		String raw1Base64 = Base64.getEncoder().encodeToString(raw1);
		System.out.println(mode + ": " + raw1Base64);
		byte[] src1 = cipher.decrypt(pwd1, raw1);
		Assertions.assertEquals(data1, new String(src1, StandardCharsets.UTF_8));

		String raw2 = cipher.encryptString(pwd1, data1);
		String data2 = cipher.decryptString(pwd1, raw2);
		Assertions.assertEquals(data1, data2);

		InputStream in = AesCipherTest.class.getResourceAsStream("/log4j2.xml");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		cipher.encrypt(pwd1.getBytes(StandardCharsets.UTF_8), in, out);
		String data3 = StringUtil.getString(AesCipherTest.class, "/log4j2.xml");
		byte[] raw3 = out.toByteArray();
		in = new ByteArrayInputStream(raw3);
		out.reset();
		cipher.decrypt(pwd1.getBytes(StandardCharsets.UTF_8), in, out);
		String data33 = new String(out.toByteArray(), StandardCharsets.UTF_8);
		Assertions.assertEquals(data3, data33);

		byte[] d3 = cipher.decrypt(pwd1, raw3);
		Assertions.assertEquals(data3, new String(d3, StandardCharsets.UTF_8));
	}

	@Test
	public void testECB() {
		this.testMode(CipherMode.ECB);
	}

	@Test
	public void testCBC() {
		this.testMode(CipherMode.CBC);
	}

	@Test
	public void testCFB() {
		this.testMode(CipherMode.CFB);
	}

	@Test
	public void testOFB() {
		this.testMode(CipherMode.OFB);
	}

	@Test
	public void testCTR() {
		this.testMode(CipherMode.CTR);
	}

}
