package test.base.core.security;

import blue.base.core.security.KeyPair;
import blue.base.core.security.KeyPairMode;
import blue.base.core.security.RsaCipher;
import blue.base.core.security.SecurityFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Jin Zheng
 * @since 2021-06-15
 */
public class RsaCipherTest {
    private String data1 = "我的电脑-jin.zheng";

	public RsaCipherTest() {
	}

	private void testRsa(KeyPair key) {
        RsaCipher cipher = SecurityFactory.createRsaCipher();
        byte[] raw1 = cipher.encrypt(key.getPublic(), data1.getBytes(StandardCharsets.UTF_8));
        String raw1Base64 = Base64.getEncoder().encodeToString(raw1);
        System.out.println("raw: " + raw1Base64);
        byte[] src1 = cipher.decrypt(key.getPrivate(), raw1);
        Assertions.assertEquals(data1, new String(src1, StandardCharsets.UTF_8));
    }

	@Test
	public void testGenerate() {
        KeyPair key = SecurityFactory.generateKeyPair(KeyPairMode.RSA2048);
        this.testRsa(key);
    }

    @Test
    public void testLoadOpenSsl1() {
        InputStream inPub = RsaCipherTest.class.getResourceAsStream("/cert/public.pem");
        InputStream inPriv = RsaCipherTest.class.getResourceAsStream("/cert/private.key");
        KeyPair key = SecurityFactory.loadOpenSslKeyPair(KeyPairMode.RSA, inPub, inPriv);
        this.testRsa(key);
    }

    @Test
    public void testLoadOpenSsl2() {
        InputStream inPub = RsaCipherTest.class.getResourceAsStream("/cert/public.crt");
        InputStream inPriv = RsaCipherTest.class.getResourceAsStream("/cert/private.key");
        KeyPair key = SecurityFactory.loadOpenSslKeyPair(KeyPairMode.RSA, inPub, inPriv);
        this.testRsa(key);
    }

    @Test
    public void testNotRsa() {
	    KeyPair key = SecurityFactory.generateKeyPair(KeyPairMode.DSA512);
        RsaCipher cipher = SecurityFactory.createRsaCipher();
        Assertions.assertThrows(IllegalArgumentException.class, () -> cipher.encrypt(key.getPublic(), data1.getBytes()));
        Assertions.assertThrows(IllegalArgumentException.class, () -> cipher.decrypt(key.getPrivate(), data1.getBytes()));
    }

}
