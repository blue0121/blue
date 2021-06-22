package blue.base.core.security;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Jin Zheng
 * @since 2021-06-15
 */
public interface Signature {

	/**
	 * 用私钥对字节数组签名
	 * @param key
	 * @param data
	 * @return
	 */
	byte[] sign(PrivateKey key, byte[] data);

	/**
	 * 用私钥对字符串签名，返回Base64
	 * @param key
	 * @param data
	 * @return
	 */
	String signString(PrivateKey key, String data);

	/**
	 * 用私钥对输入流签名
	 * @param key
	 * @param in
	 * @return
	 */
	byte[] sign(PrivateKey key, InputStream in);

	/**
	 * 用公钥对字节数组验证签名
	 * @param key
	 * @param data
	 * @param sign
	 * @return
	 */
	boolean verify(PublicKey key, byte[] data, byte[] sign);

	/**
	 * 用公钥对字符串验证签名
	 * @param key
	 * @param data
	 * @param signBase64
	 * @return
	 */
	boolean verifyString(PublicKey key, String data, String signBase64);

	/**
	 * 用公钥对输入流验证签名
	 * @param key
	 * @param in
	 * @param sign
	 * @return
	 */
	boolean verify(PublicKey key, InputStream in, byte[] sign);

}
