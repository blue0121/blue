package test.base.core.security;

import blue.base.core.security.KeyPair;
import blue.base.core.security.KeyPairMode;
import blue.base.core.security.SecurityFactory;
import blue.base.core.security.Signature;
import blue.base.core.security.SignatureMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 2021-06-15
 */
public class SignatureTest {
    private String data1 = "我的电脑-jin.zheng";

	public SignatureTest() {
	}

    private void testSign(KeyPairMode keyMode, SignatureMode mode) {
        KeyPair key = SecurityFactory.generateKeyPair(keyMode);
        Signature signature = SecurityFactory.createSignature(mode);
        String sign = signature.signString(key.getPrivate(), data1);
        System.out.println(mode.getKey() + ": " + sign);
        Assertions.assertTrue(signature.verifyString(key.getPublic(), data1, sign));
    }

    @Test
    public void testSignRsa() {
        this.testSign(KeyPairMode.RSA2048, SignatureMode.MD5_RSA);
        this.testSign(KeyPairMode.RSA2048, SignatureMode.SHA1_RSA);
        this.testSign(KeyPairMode.RSA2048, SignatureMode.SHA256_RSA);
        this.testSign(KeyPairMode.RSA2048, SignatureMode.SHA384_RSA);
        this.testSign(KeyPairMode.RSA2048, SignatureMode.SHA512_RSA);
    }

    @Test
    public void testSignDsa() {
	    this.testSign(KeyPairMode.DSA1024, SignatureMode.SHA1_DSA);
        this.testSign(KeyPairMode.DSA1024, SignatureMode.SHA224_DSA);
        this.testSign(KeyPairMode.DSA1024, SignatureMode.SHA256_DSA);
        this.testSign(KeyPairMode.DSA1024, SignatureMode.SHA384_DSA);
        this.testSign(KeyPairMode.DSA1024, SignatureMode.SHA512_DSA);
    }

    @Test
    public void testSignEcDsa() {
        this.testSign(KeyPairMode.EC256, SignatureMode.SHA1_ECDSA);
        this.testSign(KeyPairMode.EC256, SignatureMode.SHA224_ECDSA);
        this.testSign(KeyPairMode.EC256, SignatureMode.SHA256_ECDSA);
        this.testSign(KeyPairMode.EC256, SignatureMode.SHA384_ECDSA);
        this.testSign(KeyPairMode.EC256, SignatureMode.SHA512_ECDSA);
    }

}
