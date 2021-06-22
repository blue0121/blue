package blue.base.core.security;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Jin Zheng
 * @since 2021-06-16
 */
public interface KeyPair {

	/**
	 * 获取公钥
	 *
	 * @return
	 */
	PublicKey getPublic();

	/**
	 * 获取私钥
	 *
	 * @return
	 */
	PrivateKey getPrivate();
}
