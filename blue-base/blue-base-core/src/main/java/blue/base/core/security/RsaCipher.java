package blue.base.core.security;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Jin Zheng
 * @since 2021-06-15
 */
public interface RsaCipher {
	/**
	 * 用公钥加密字节数组
	 *
	 * @param key  公钥
	 * @param data
	 * @return
	 */
	byte[] encrypt(PublicKey key, byte[] data);

	/**
	 * 用公钥加密字符串，返回base64
	 *
	 * @param key  公钥
	 * @param data
	 * @return base64
	 */
	String encryptString(PublicKey key, String data);

	/**
	 * 用私钥解密字节数组
	 *
	 * @param key 私钥
	 * @param raw
	 * @return
	 */
	byte[] decrypt(PrivateKey key, byte[] raw);

	/**
	 * 用私钥解密base64字符串，返回字符串
	 *
	 * @param key       私钥
	 * @param rawBase64
	 * @return 字符串原文
	 */
	String decryptString(PrivateKey key, String rawBase64);
}
