package blue.core.util;

/**
 * emoji 表情
 * 
 * @author zhengj
 * @since 1.0 2016年5月29日
 */
public class EmojiUtil
{
	private EmojiUtil()
	{
	}

	/**
	 * 检测是否有emoji字符
	 * 
	 * @param source
	 * @return 一旦含有就抛出
	 */
	public static boolean containsEmoji(String source)
	{
		if (source == null || source.isEmpty())
			return false;

		for (int i = 0; i < source.length(); i++)
		{
			if (!isNotEmojiCharacter(source.charAt(i)))
				return true;
		}

		return false;
	}

	private static boolean isNotEmojiCharacter(char codePoint)
	{
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
				|| (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}

	/**
	 * 过滤emoji 或者 其他非文字类型的字符
	 * 
	 * @param source
	 * @return
	 */
	public static String filterEmoji(String source)
	{
		if (!containsEmoji(source))
			return source;

		StringBuilder buf = null;
		int len = source.length();
		for (int i = 0; i < len; i++)
		{
			char codePoint = source.charAt(i);
			if (isNotEmojiCharacter(codePoint))
			{
				if (buf == null)
				{
					buf = new StringBuilder(source.length());
				}
				buf.append(codePoint);
			}
		}
		if (buf == null)
		{
			return source;
		}
		else
		{
			if (buf.length() == len)
			{
				buf = null;
				return source;
			}
			else
			{
				return buf.toString();
			}
		}

	}

}
