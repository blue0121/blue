package blue.base.core.security;

import blue.base.core.util.AssertUtil;
import blue.base.internal.core.security.DefaultAesCipher;
import blue.base.internal.core.security.DefaultDigest;

/**
 * @author Jin Zheng
 * @since 2021-06-01
 */
public class SecurityFactory {
	private SecurityFactory() {
	}

	public static Digest createDigest(DigestType type) {
		AssertUtil.notNull(type, "Digest Type");
		return new DefaultDigest(type);
	}

	public static AesCipher createAesCipher(CipherMode mode) {
		AssertUtil.notNull(mode, "Cipher Mode");
		return new DefaultAesCipher(mode);
	}

}
