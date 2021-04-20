package blue.base.core.util;

import java.util.Random;

/**
 * 随机工具类
 *
 * @author zhengj
 * @since 2014-7-30 1.0
 */
public class RandomUtil {
	private static String[] chars = {"0123456789", "abcdefghijklmnopqrstuvwxyz", "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
			"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", "23456789abcdefghijkmnpqrstuvwxyz",
			"23456789ABCDEFGHIJKMNPQRSTUVWXYZ", "23456789abcdefghijkmnpqrstuvwxyzABCDEFGHIJKMNPQRSTUVWXYZ"};

	private static Random random = new Random();

	public static enum RandomType {
		/**
		 * 数字
		 */
		NUMBER(0),
		/**
		 * 小写字母
		 */
		LOWER_CASE(1),
		/**
		 * 大写字母
		 */
		UPPER_CASE(2),
		/**
		 * 大小写字母
		 */
		UPPER_LOWER_CASE(3),
		/**
		 * 小写字母+数字
		 */
		LOWER_CASE_NUMBER(4),
		/**
		 * 大写字母+数字
		 */
		UPPER_CASE_NUMBER(5),
		/**
		 * 大小写字母+数字
		 */
		UPPER_LOWER_CASE_NUMBER(6),
		;

		private int index;

		private RandomType(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}
	}

	private RandomUtil() {
	}

	/**
	 * 产生随机数
	 *
	 * @param type 随机字符类型
	 * @param len  随机数长度
	 * @return 随机数
	 */
	public static String rand(RandomType type, int len) {
		String text = chars[type.getIndex()];
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			int n = random.nextInt(text.length());
			sb.append(text.charAt(n));
		}
		return sb.toString();
	}

	/**
	 * 产生不大于max最大整数的随机数
	 *
	 * @param max 最大整数
	 * @return
	 */
	public static int getInt(int max) {
		return random.nextInt(max);
	}

}
