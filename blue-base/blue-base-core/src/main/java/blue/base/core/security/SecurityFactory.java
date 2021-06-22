package blue.base.core.security;

import blue.base.core.util.AssertUtil;
import blue.base.internal.core.security.DefaultAesCipher;
import blue.base.internal.core.security.DefaultDigest;
import blue.base.internal.core.security.DefaultRsaCipher;
import blue.base.internal.core.security.DefaultSignature;
import blue.base.internal.core.security.GenerateKeyPair;
import blue.base.internal.core.security.LoadOpenSslKeyPair;

import java.io.InputStream;

/**
 * @author Jin Zheng
 * @since 2021-06-01
 */
public class SecurityFactory {
	private SecurityFactory() {
	}

	/**
	 * 生产公钥/私钥对
	 *
	 * @param mode
	 * @param keySize
	 * @return
	 */
	public static KeyPair generateKeyPair(KeyPairMode mode, int keySize) {
		AssertUtil.notNull(mode, "KeyPairMode");
		return new GenerateKeyPair(mode, keySize);
	}

	/**
	 * 生产公钥/私钥对
	 *
	 * @param mode
	 * @return
	 */
	public static KeyPair generateKeyPair(KeyPairMode mode) {
		AssertUtil.notNull(mode, "KeyPairMode");
		return new GenerateKeyPair(mode, mode.getSize());
	}

	/**
	 * 读取OpenSSL产生的证书(公钥)和私钥(PKCS#8格式)
	 *
	 * @param mode  算法模型
	 * @param inPub  证书(公钥)
	 * @param inPriv 私钥(PKCS#8格式)
	 * @return
	 */
	public static KeyPair loadOpenSslKeyPair(KeyPairMode mode, InputStream inPub, InputStream inPriv) {
		AssertUtil.notNull(mode, "KeyPairMode");
		return new LoadOpenSslKeyPair(mode, inPub, inPriv);
	}

	public static Digest createDigest(DigestType type) {
		AssertUtil.notNull(type, "Digest Type");
		return new DefaultDigest(type);
	}

	public static AesCipher createAesCipher(CipherMode mode) {
		AssertUtil.notNull(mode, "Cipher Mode");
		return new DefaultAesCipher(mode);
	}

	public static RsaCipher createRsaCipher() {
		return new DefaultRsaCipher();
	}

	public static Signature createSignature(SignatureMode mode) {
		AssertUtil.notNull(mode, "Signature Mode");
		return new DefaultSignature(mode);
	}

}
