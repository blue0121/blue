package blue.base.core.security;

import blue.base.core.util.NumberUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


/**
 * 消息摘要算法，封装了MD5和SHA-1两种算法
 *
 * @author zhengj
 * @date 2008-12-22
 * @since 1.0
 */
public class DigestUtil {
	private static final String MD5 = "md5";
	private static final String SHA1 = "sha1";

	private DigestUtil() {
	}

	/**
	 * 用md5算法加密，返回16进制小写字母
	 *
	 * @param src 密码原文
	 * @return md5信息摘要
	 */
	public static String md5Hex(String src) {
		byte[] dest = md5(src);
		if (dest == null || dest.length == 0) {
			return null;
		}

		return NumberUtil.toHexString(dest);
	}

	/**
	 * 用MD5算法加密，返回字节数组
	 *
	 * @param src 密码原文
	 * @return md5信息摘要，返回字节数组
	 */
	public static byte[] md5(String src) {
		return hash(src, MD5);
	}

	/**
	 * 用SHA-1算法加密，返回16进制小写字母
	 *
	 * @param src 密码原文
	 * @return SHA-1信息摘要
	 */
	public static String sha1Hex(String src) {
		byte[] dest = sha1(src);
		if (dest == null || dest.length == 0) {
			return null;
		}

		return NumberUtil.toHexString(dest);
	}

	/**
	 * 用SHA-1算法加密，返回字节数组
	 *
	 * @param src 密码原文
	 * @return SHA-1信息摘要
	 */
	public static byte[] sha1(String src) {
		return hash(src, SHA1);
	}

	private static byte[] hash(String src, String digestName) {
		byte[] bytes = null;
		if (src == null || src.isEmpty()) {
			return bytes;
		}

		try {
			byte[] dest = src.getBytes(StandardCharsets.UTF_8);
			MessageDigest sha1 = MessageDigest.getInstance(digestName);
			bytes = sha1.digest(dest);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return bytes;
	}

	/**
	 * 用MD5算法比较密码原文与密文是否相等
	 *
	 * @param src 密码原文
	 * @param md  MD5信息摘要
	 * @return 相等返回true，否则返回false
	 */
	public static boolean equalMd5(String src, String md) {
		if (src == null || md == null) {
			return false;
		}
		return md.equals(md5Hex(src));
	}

	/**
	 * 用SHA-1算法比较密码原文与密文是否相等
	 *
	 * @param src 密码原文
	 * @param md  SHA-1信息摘要
	 * @return 相等返回true，否则返回false
	 */
	public static boolean equalSha1(String src, String md) {
		if (src == null || md == null) {
			return false;
		}
		return md.equals(sha1Hex(src));
	}
}
