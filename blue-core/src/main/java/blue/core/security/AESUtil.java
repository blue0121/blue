package blue.core.security;

import blue.core.common.CoreConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * AES加解类工具类（AES/CBC/NoPadding）
 * 
 * @author zhengj
 * @since 1.0
 * @date 2012-11-14
 */
public class AESUtil
{
	private static Logger logger = LoggerFactory.getLogger(AESUtil.class);

	private static final String KEY_ALGORITHM = "AES";
	// 算法/模式/补码方式
	private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

	private AESUtil()
	{
	}

	/**
	 * 小数据量加密，返回Base64编码字符串
	 * 
	 * @param password AES加密密钥
	 * @param data 明文
	 * @return 密文，Base64编码
	 */
	public static String encrypt(String password, String data)
	{
		String raw = null;
		byte[] keys = DigestUtil.md5(password);
		try
		{
			byte[] dataBytes = data.getBytes(CoreConst.UTF_8);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

			SecretKeySpec keySpec = new SecretKeySpec(keys, KEY_ALGORITHM);
			IvParameterSpec ivSpec = new IvParameterSpec(keys);

			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
			byte[] tmp = cipher.doFinal(dataBytes);

			raw = Base64.getEncoder().encodeToString(tmp);
		}
		catch (Exception e)
		{
			logger.error("AES加密失败", e);
		}
		return raw;
	}

	/**
	 * 小数据量解密
	 * 
	 * @param password AES加密密钥
	 * @param raw 密文，Base64编码
	 * @return 明文
	 */
	public static String decrypt(String password, String raw)
	{
		String data = null;
		byte[] rawBytes = Base64.getDecoder().decode(raw);
		byte[] keys = DigestUtil.md5(password);
		try
		{
			SecretKeySpec keySpec = new SecretKeySpec(keys, KEY_ALGORITHM);
			IvParameterSpec ivSpec = new IvParameterSpec(keys);

			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
			byte[] dataBytes = cipher.doFinal(rawBytes);

			data = new String(dataBytes, CoreConst.UTF_8);
		}
		catch (Exception e)
		{
			logger.error("AES解密失败", e);
		}
		return data;
	}

}
