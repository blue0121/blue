package test.base.core.security;

import blue.base.core.security.Digest;
import blue.base.core.security.DigestType;
import blue.base.core.security.SecurityFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.security.Provider;
import java.security.Security;

/**
 * @author Jin Zheng
 * @since 2021-06-01
 */
public class DigestTest {
    private String key = "testblue";

	public DigestTest() {
	}

    @Test
    @Disabled
	public void testAllProviders() {
        Provider[] providers = Security.getProviders();
        for (var provider : providers) {
            System.out.println("==========" + provider + "==========");
            for (var key : provider.keySet()) {
                System.out.println(key);
            }
        }
    }

    @Test
    public void testMD5() {
        Digest digest = SecurityFactory.createDigest(DigestType.MD5);
        Assertions.assertEquals(16, digest.digest(key).length);
        Assertions.assertEquals(32, digest.digestToHex(key).length());
        InputStream is = DigestTest.class.getResourceAsStream("/log4j2.xml");
        Assertions.assertEquals(16, digest.digest(is).length);
        is = DigestTest.class.getResourceAsStream("/log4j2.xml");
        Assertions.assertEquals(32, digest.digestToHex(is).length());
    }

    @Test
    public void testSHA1() {
        Digest digest = SecurityFactory.createDigest(DigestType.SHA1);
        Assertions.assertEquals(20, digest.digest(key).length);
        Assertions.assertEquals(40, digest.digestToHex(key).length());
        InputStream is = DigestTest.class.getResourceAsStream("/log4j2.xml");
        Assertions.assertEquals(20, digest.digest(is).length);
        is = DigestTest.class.getResourceAsStream("/log4j2.xml");
        Assertions.assertEquals(40, digest.digestToHex(is).length());
    }

    @Test
    public void testSHA256() {
        Digest digest = SecurityFactory.createDigest(DigestType.SHA256);
        Assertions.assertEquals(32, digest.digest(key).length);
        Assertions.assertEquals(64, digest.digestToHex(key).length());
        InputStream is = DigestTest.class.getResourceAsStream("/log4j2.xml");
        Assertions.assertEquals(32, digest.digest(is).length);
        is = DigestTest.class.getResourceAsStream("/log4j2.xml");
        Assertions.assertEquals(64, digest.digestToHex(is).length());
    }

    @Test
    public void testSHA384() {
        Digest digest = SecurityFactory.createDigest(DigestType.SHA384);
        Assertions.assertEquals(48, digest.digest(key).length);
        Assertions.assertEquals(96, digest.digestToHex(key).length());
        InputStream is = DigestTest.class.getResourceAsStream("/log4j2.xml");
        Assertions.assertEquals(48, digest.digest(is).length);
        is = DigestTest.class.getResourceAsStream("/log4j2.xml");
        Assertions.assertEquals(96, digest.digestToHex(is).length());
    }

    @Test
    public void testSHA512() {
        Digest digest = SecurityFactory.createDigest(DigestType.SHA512);
        Assertions.assertEquals(64, digest.digest(key).length);
        Assertions.assertEquals(128, digest.digestToHex(key).length());
        InputStream is = DigestTest.class.getResourceAsStream("/log4j2.xml");
        Assertions.assertEquals(64, digest.digest(is).length);
        is = DigestTest.class.getResourceAsStream("/log4j2.xml");
        Assertions.assertEquals(128, digest.digestToHex(is).length());
    }

}
