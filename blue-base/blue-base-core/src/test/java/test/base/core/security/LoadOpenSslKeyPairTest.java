package test.base.core.security;

import blue.base.core.security.KeyPair;
import blue.base.core.security.KeyPairMode;
import blue.base.core.security.SecurityFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

/**
 * @author Jin Zheng
 * @since 2021-06-22
 */
public class LoadOpenSslKeyPairTest {
	public LoadOpenSslKeyPairTest() {
	}

	private void testLoadOpenSsl(KeyPairMode mode, InputStream inPub, InputStream inPriv) {
        KeyPair keyPair = SecurityFactory.loadOpenSslKeyPair(mode, inPub, inPriv);
        Assertions.assertNotNull(keyPair);
        Assertions.assertNotNull(keyPair.getPublic());
        Assertions.assertNotNull(keyPair.getPrivate());
    }

    @Test
    public void testRsa() {
	    InputStream inPub = LoadOpenSslKeyPairTest.class.getResourceAsStream("/cert/rsa_pub.pub");
        InputStream inPriv = LoadOpenSslKeyPairTest.class.getResourceAsStream("/cert/rsa_priv_pkcs8.key");
	    this.testLoadOpenSsl(KeyPairMode.RSA, inPub, inPriv);
    }

    @Test
    public void testDsa() {
        InputStream inPub = LoadOpenSslKeyPairTest.class.getResourceAsStream("/cert/dsa_pub.pub");
        InputStream inPriv = LoadOpenSslKeyPairTest.class.getResourceAsStream("/cert/dsa_priv_pkcs8.key");
        this.testLoadOpenSsl(KeyPairMode.DSA, inPub, inPriv);
    }

    @Test
    public void testEcDsa() {
        InputStream inPub = LoadOpenSslKeyPairTest.class.getResourceAsStream("/cert/ecdsa_pub.pub");
        InputStream inPriv = LoadOpenSslKeyPairTest.class.getResourceAsStream("/cert/ecdsa_priv_pkcs8.key");
        this.testLoadOpenSsl(KeyPairMode.EC, inPub, inPriv);
    }

}
