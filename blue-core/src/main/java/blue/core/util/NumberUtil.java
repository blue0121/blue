package blue.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 字节数组工具类
 * 
 * @author zhengj
 * @since 1.0
 * @date 2009-1-4
 */
public class NumberUtil
{
	public static final Pattern NUMERIC = Pattern.compile("^[-\\+]?[\\d]*$");

	private NumberUtil()
	{
	}

	/**
	 * 判断类型是否为数字型(byte, short, int, long, float, double)
	 * 
	 * @param clazz 类型
	 * @return true为数字型
	 */
	public static boolean isNumber(Class<?> clazz)
	{
		return clazz == byte.class || clazz == short.class || clazz == int.class || clazz == long.class || clazz == Byte.class
				|| clazz == Short.class || clazz == Integer.class || clazz == Long.class || clazz == float.class || clazz == double.class
				|| clazz == Float.class || clazz == Double.class;
	}

	/**
	 * 判断类型是否为整型(byte, short, int, long)
	 * 
	 * @param clazz 类型
	 * @return true为整型
	 */
	public static boolean isInteger(Class<?> clazz)
	{
		return clazz == byte.class || clazz == short.class || clazz == int.class || clazz == long.class || clazz == Byte.class
				|| clazz == Short.class || clazz == Integer.class || clazz == Long.class;
	}

	/**
	 * 判断类型是否为浮点型(float, double)
	 * 
	 * @param clazz 类型
	 * @return true为浮点型
	 */
	public static boolean isFloat(Class<?> clazz)
	{
		return clazz == float.class || clazz == double.class || clazz == Float.class || clazz == Double.class;
	}

	/**
	 * 把 byte 数组的前4个字节转成 int，不足4个字节按实际的长度算
	 * 
	 * @param bytes byte数组
	 * @return byte数组前4个字节所代表的 int 值
	 */
	public static int toInt(byte[] bytes)
	{
		int length = bytes.length > 4 ? 4 : bytes.length;
		int result = 0;
		for (int i = 0; i < length; i++)
		{
			result = (result << 8) - Byte.MIN_VALUE + (int)bytes[i];
		}
		return result;
	}

	/**
	 * 把 byte 转换为十六进制字符串
	 * 
	 * @param val byte
	 * @return 十六进制字符串，范围是00~ff
	 */
	public static String toHexString(byte val)
	{
		return Integer.toHexString(val & 0xff | 0x100).substring(1);
	}

	/**
	 * 把 byte 转换为十六进制字符串
	 * 
	 * @param val 十六进制字符串
	 * @return byte
	 */
	public static byte toByte(String val)
	{
		try
		{
			int i = Integer.parseInt(val, 16);
			return (byte)(i % 0xff);
		}
		catch (NumberFormatException e)
		{
			return 0;
		}
	}

	/**
	 * 把 byte 数组转换为十六进制字符串
	 * 
	 * @param bytes byte 数组
	 * @return 十六进制字符串，范围是00~ff，每个字节占用两个字符
	 */
	public static String toHexString(byte[] bytes)
	{
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (byte b : bytes)
		{
			sb.append(Integer.toHexString(b & 0xff | 0x100).substring(1));
		}
		return sb.toString();
	}

	/**
	 * 把十六进制字符串转化为 byte 数组
	 * 
	 * @param val 十六进制字符串
	 * @return byte 数组，如果有非法字符，用 0 代替
	 */
	public static byte[] toBytes(String val)
	{
		int length = val.length();
		boolean mod2 = (length & 1) == 0; // 模2
		int size = mod2 ? length / 2 : length / 2 + 1;
		int j = mod2 ? 2 : 1;
		int k = j;
		byte[] bytes = new byte[size];
		String s = val.substring(0, j);
		bytes[0] = toByte(s);

		j += k;
		for (int i = 1; i < bytes.length; i++, j += 2)
		{
			s = val.substring(j - 2, j);
			bytes[i] = toByte(s);
		}
		return bytes;
	}

	/**
	 * 把 long 转换化成 length 长度的字符串
	 * 
	 * @param val long值
	 * @param length 字符串的长度
	 * @return 字符串，不足前面补 0，超出则是原 long 的长度
	 */
	public static String toString(long val, int length)
	{
		String strVal = String.valueOf(val);
		int zeroLen = length - strVal.length() < 0 ? 0 : length - strVal.length();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < zeroLen; i++)
		{
			sb.append('0');
		}
		sb.append(strVal);
		return sb.toString();
	}

	/**
	 * 把 int 转换为十六进制字符串
	 * 
	 * @param val int值
	 * @return 十六进制字符串，范围是0000~ffff
	 */
	public static String toHexString(short val)
	{
		return Integer.toHexString(val & 0xffff | 0x10000).substring(1);
	}

	/**
	 * 把 int 转换为十六进制字符串
	 * 
	 * @param val int值
	 * @return 十六进制字符串，范围是00000000~ffffffff
	 */
	public static String toHexString(int val)
	{
		String tmp = Integer.toHexString(val);
		StringBuilder sb = new StringBuilder("00000000");
		sb.replace(8 - tmp.length(), 8, tmp);
		return sb.toString();
	}

	/**
	 * 把 long 转换为十六进制字符串
	 * 
	 * @param val long值
	 * @return 十六进制字符串，范围是0000000000000000~ffffffffffffffff
	 */
	public static String toHexString(long val)
	{
		String tmp = Long.toHexString(val);
		StringBuilder sb = new StringBuilder("0000000000000000");
		sb.replace(16 - tmp.length(), 16, tmp);
		return sb.toString();
	}

	/**
	 * 格式化字节，把字节转化为 B, KB, MB, GB 等表示法
	 * 
	 * @param size 字节数
	 * @return 格式后字符串
	 */
	public static String byteFormat(long size)
	{
		String[] a = { "B", "KB", "MB", "GB", "TB", "PB" };
		double val = size;
		int pos = 0;
		while (val >= 1024.0d)
		{
			val /= 1024;
			pos++;
		}
		String s = String.format("%.2f%s", val, a[pos]);
		return s;
	}

	/**
	 * 判断是否为整型数字字符串
	 *
	 * @param str 字符串
	 * @return true为整型数字字符串
	 */
	public static boolean isInteger(String str)
	{
		if (str == null || str.isEmpty())
			return false;

		return NUMERIC.matcher(str).matches();
	}

	/**
	 * 把 "5, 10, 50, 100, 150, 200, 500, 1000, 2000, 5000" 转换成数字列表
	 * @param strNum 字符串数字
	 * @return 数字列表
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> split(String strNum, Class<T> clazz)
	{
		List<T> list = new ArrayList<>();
		if (strNum == null || strNum.isEmpty())
			return list;

		for (String str : strNum.split(StringUtil.SPLIT))
		{
			String num = str.trim();
			if (num.isEmpty())
				continue;

			T obj = null;
			if (clazz == Integer.class || clazz == int.class)
			{
				obj = (T) Integer.valueOf(num);
			}
			else if (clazz == Long.class || clazz == long.class)
			{
				obj = (T) Long.valueOf(num);
			}
			else if (clazz == Double.class || clazz == double.class)
			{
				obj = (T) Double.valueOf(num);
			}
			else if (clazz == Float.class || clazz == float.class)
			{
				obj = (T) Float.valueOf(num);
			}
			else if (clazz == Short.class || clazz == short.class)
			{
				obj = (T) Short.valueOf(num);
			}
			else if (clazz == Byte.class || clazz == byte.class)
			{
				obj = (T) Byte.valueOf(num);
			}
			if (obj == null)
				continue;

			list.add(obj);
		}

		return list;
	}

	public static double[] splitDouble(String strNum)
	{
		List<Double> list = split(strNum, Double.class);
		double[] nums = new double[list.size()];
		int i = 0;
		for (Double d : list)
		{
			nums[i++] = d.doubleValue();
		}
		return nums;
	}

	public static long[] splitLong(String strNum)
	{
		List<Long> list = split(strNum, Long.class);
		long[] nums = new long[list.size()];
		int i = 0;
		for (Long d : list)
		{
			nums[i++] = d.longValue();
		}
		return nums;
	}

	public static int[] splitInt(String strNum)
	{
		List<Integer> list = split(strNum, Integer.class);
		int[] nums = new int[list.size()];
		int i = 0;
		for (Integer d : list)
		{
			nums[i++] = d.intValue();
		}
		return nums;
	}

	private static char[] cnArr = { '一', '二', '三', '四', '五', '六', '七', '八', '九' };
	private static char[] chArr = { '十', '百', '千', '万', '亿' };
	/**
	 * 中文数字转化为阿拉伯数字
	 * 
	 * @param chn 中文数字
	 * @return 阿拉伯数字
	 */
	public static int chnToInt(String chn)
	{
		AssertUtil.notEmpty(chn, "中文数字");
		if (isInteger(chn))
		{
//			String n = StringUtils.stripStart(chn, "0");
//			System.out.println(n);
			return Integer.parseInt(chn);
		}
		
		int result = 0;
		int temp = 1;// 存放一个单位的数字如：十万
		int count = 0;// 判断是否有chArr

		for (int i = 0; i < chn.length(); i++)
		{
			boolean b = true; // 判断是否是chArr
			char c = chn.charAt(i);
			for (int j = 0; j < cnArr.length; j++) // 非单位，即数字
			{
				if (c == cnArr[j])
				{
					if (0 != count) // 添加下一个单位之前，先把上一个单位值添加到结果中
					{
						result += temp;
						temp = 1;
						count = 0;
					}
					// 下标+1，就是对应的值
					temp = j + 1;
					b = false;
					break;
				}
			}
			if (b) // 单位{'十','百','千','万','亿'}
			{
				for (int j = 0; j < chArr.length; j++)
				{
					if (c == chArr[j])
					{
						switch (j)
						{
							case 0:
								temp *= 10;
								break;
							case 1:
								temp *= 100;
								break;
							case 2:
								temp *= 1000;
								break;
							case 3:
								temp *= 10000;
								break;
							case 4:
								temp *= 100000000;
								break;
							default:
								break;
						}
						count++;
					}
				}
			}
			if (i == chn.length() - 1) // 遍历到最后一个字符
			{
				result += temp;
			}
		}
		return result;

	}

}
