package blue.base.core.security;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 128位 AES加密/解密
 *
 * @author Jin Zheng
 * @since 2021-06-07
 */
public interface AesCipher {

	/**
	 * 创建128位密钥
	 *
	 * @return
	 */
	byte[] generateKey();

	/**
	 * 加密字节数组
	 *
	 * @param key  key.length != 16时，md5(key)
	 * @param data
	 * @return
	 */
	byte[] encrypt(byte[] key, byte[] data);

	/**
	 * 加密字节数组
	 *
	 * @param password password.getBytes(UTF8).length != 16时，md5(password.getBytes(UTF8))
	 * @param data
	 * @return
	 */
	byte[] encrypt(String password, byte[] data);

	/**
	 * 加密字符串，返回base64
	 *
	 * @param password password.getBytes(UTF8).length != 16时，md5(password.getBytes(UTF8))
	 * @param data
	 * @return base64
	 */
	String encryptString(String password, String data);

	/**
	 * 加密输入流，结果写入输出流
	 *
	 * @param key
	 * @param in
	 * @param out
	 */
	void encrypt(byte[] key, InputStream in, OutputStream out);

	/**
	 * 解密字节数组
	 *
	 * @param key key.length != 16时，md5(key)
	 * @param raw
	 * @return
	 */
	byte[] decrypt(byte[] key, byte[] raw);

	/**
	 * 解密字节数组
	 *
	 * @param password password.getBytes(UTF8).length != 16时，md5(password.getBytes(UTF8))
	 * @param raw
	 * @return
	 */
	byte[] decrypt(String password, byte[] raw);

	/**
	 * 解密base64字符串，返回字符串
	 *
	 * @param password  password.getBytes(UTF8).length != 16时，md5(password.getBytes(UTF8))
	 * @param rawBase64
	 * @return 字符串原文
	 */
	String decryptString(String password, String rawBase64);

	/**
	 * 解密输入流，写入输出流
	 *
	 * @param key
	 * @param in
	 * @param out
	 */
	void decrypt(byte[] key, InputStream in, OutputStream out);

	/**
	 * 模式
	 *
	 * @return
	 */
	CipherMode getCipherMode();

	/**
	 * 算法描述：算法/模式/补码方式
	 *
	 * @return
	 */
	String getAlgorithm();

}
