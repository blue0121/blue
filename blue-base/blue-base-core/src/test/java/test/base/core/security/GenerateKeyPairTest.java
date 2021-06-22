package test.base.core.security;

import blue.base.core.security.KeyPair;
import blue.base.core.security.KeyPairMode;
import blue.base.core.security.SecurityFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 2021-06-22
 */
public class GenerateKeyPairTest {
	private static final int ZERO = 0;

	public GenerateKeyPairTest() {
	}

	public void testKeyPair(KeyPairMode mode, int keySize) {
		KeyPair keyPair = SecurityFactory.generateKeyPair(mode, keySize);
		Assertions.assertNotNull(keyPair);
		Assertions.assertNotNull(keyPair.getPrivate());
		Assertions.assertNotNull(keyPair.getPublic());
	}

	@Test
	public void testRSA() {
		this.testKeyPair(KeyPairMode.RSA512, ZERO);
		this.testKeyPair(KeyPairMode.RSA1024, ZERO);
		this.testKeyPair(KeyPairMode.RSA2048, ZERO);
		this.testKeyPair(KeyPairMode.RSA3084, ZERO);
		this.testKeyPair(KeyPairMode.RSA4096, ZERO);
		this.testKeyPair(KeyPairMode.RSA, 768);
		Assertions.assertThrows(IllegalArgumentException.class, () -> this.testKeyPair(KeyPairMode.RSA, 0));
	}

	@Test
	public void testDSA() {
		this.testKeyPair(KeyPairMode.DSA512, ZERO);
		this.testKeyPair(KeyPairMode.DSA768, ZERO);
		this.testKeyPair(KeyPairMode.DSA1024, ZERO);
		this.testKeyPair(KeyPairMode.DSA, 704);
		Assertions.assertThrows(IllegalArgumentException.class, () -> this.testKeyPair(KeyPairMode.DSA, 0));
	}

	@Test
	public void testECDSA() {
		this.testKeyPair(KeyPairMode.EC256, ZERO);
		this.testKeyPair(KeyPairMode.EC384, ZERO);
		this.testKeyPair(KeyPairMode.EC521, ZERO);
		Assertions.assertThrows(IllegalArgumentException.class, () -> this.testKeyPair(KeyPairMode.EC, 0));

	}

}
