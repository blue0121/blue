package blue.base.core.util;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 字符串工具类
 *
 * @author zhengj
 * @date 2009-2-15
 * @date 1.0
 * @since 1.0
 */
public class StringUtil {
	public static final String SPLIT = "[\\s,;\\|]";

	private StringUtil() {
	}

	/**
	 * 在字符串上加上掩码
	 *
	 * @param src    原字符串
	 * @param start  开始位置，从0开始
	 * @param length 替换长度
	 * @param mark   掩码
	 * @return 加上掩码后的字符串
	 */
	public static String mask(String src, int start, int length, String mark) {
		int len = src.length();
		if (len <= start) {
			return src;
		}

		StringBuilder sb = new StringBuilder(src.substring(0, start));
		for (int i = 0; i < Math.ceil((double) length / mark.length()); i++) {
			sb.append(mark);
		}

		if (len > start + length) {
			sb.append(src.substring(start + length));
		}

		return sb.toString();

	}

	/**
	 * 生成字符串序列
	 *
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @param start  开始序号
	 * @param end    结束序号
	 * @param length 序列长度
	 * @return 字符串序列
	 */
	public static List<String> sequence(String prefix, String suffix, int start, int end, int length) {
		if (start < 0 || end < 0) {
			throw new IllegalArgumentException("start 或 end 不能小于 0");
		}
		if (start > end) {
			throw new IllegalArgumentException("start 不能大于 end");
		}

		prefix = (prefix == null ? "" : prefix);
		suffix = (suffix == null ? "" : suffix);
		List<String> list = new ArrayList<>();
		for (int i = start; i <= end; i++) {
			StringBuilder seq = new StringBuilder(prefix.length() + suffix.length() + length);
			seq.append(prefix);
			String val = String.valueOf(i);
			int j = length - val.length();
			while (j-- > 0) {
				seq.append("0");
			}
			seq.append(val);
			list.add(seq.toString());
			seq.append(suffix);
		}
		return list;
	}

	/**
	 * 根据模板生成字符串
	 *
	 * @param template 模板
	 * @param param    参数
	 * @return 生成的字符串
	 */
	public static String template(String template, Map<String, String> param) {
		return template(template, param, null, null);
	}

	/**
	 * 根据模板生成字符串
	 *
	 * @param template 模板
	 * @param param    参数
	 * @param prefix   前缀
	 * @param suffix   后缀
	 * @return 生成的字符串
	 */
	public static String template(String template, Map<String, String> param, String prefix, String suffix) {
		AssertUtil.notEmpty(template, "模板");
		AssertUtil.notEmpty(param, "参数");
		StringBuilder content = new StringBuilder(template);
		for (Map.Entry<String, String> entry : param.entrySet()) {
			StringBuilder p = new StringBuilder(32);
			if (prefix != null && !prefix.isEmpty()) {
				p.append(prefix);
			}
			p.append(entry.getKey());
			if (suffix != null && !suffix.isEmpty()) {
				p.append(suffix);
			}
			int index = 0;
			while (index != -1) {
				index = content.indexOf(p.toString());
				if (index != -1) {
					content.replace(index, index + p.length(), entry.getValue());
				}
			}
		}
		return content.toString();
	}

	/**
	 * 从类所在模块路径文件读取文本
	 *
	 * @param clazz     类
	 * @param classpath 类路径文件
	 * @return 读取的文本
	 */
	public static String getString(Class<?> clazz, String classpath) {
		if (classpath == null || classpath.isEmpty()) {
			return null;
		}

		if (clazz == null) {
			clazz = StringUtil.class;
		}
		InputStream is = clazz.getResourceAsStream(classpath);
		if (is == null) {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			String path = classpath;
			if (classpath.startsWith("/")) {
				path = classpath.substring(1);
			}
			is = loader.getResourceAsStream(path);
		}
		String str = null;
		try (InputStream in = is) {
			byte[] buf = in.readAllBytes();
			str = new String(buf, StandardCharsets.UTF_8);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 把 "5, 10, 50, 100, 150, 200, 500, 1000, 2000, 5000" 分割成字符串列表
	 *
	 * @param str 待分割字符串
	 * @return 字符串列表
	 */
	public static List<String> split(String str) {
		List<String> list = new ArrayList<>();
		if (str == null || str.isEmpty()) {
			return list;
		}

		for (String s : str.split(SPLIT)) {
			s = s.trim();
			if (s.isEmpty()) {
				continue;
			}

			list.add(s);
		}
		return list;
	}

	/**
	 * 左填充字符串
	 *
	 * @param str 原始字符串
	 * @param len 目标字符串长度
	 * @param pad 填充字符串
	 * @return 目标字符串
	 */
	public static String leftPad(String str, int len, String pad) {
		if (str == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len - str.length(); i += pad.length()) {
			sb.append(pad);
		}
		sb.append(str);
		return sb.toString();
	}

	/**
	 * 连接字符串
	 *
	 * @param list      字符串列表
	 * @param separator 分割符
	 * @return 连接后的字符串
	 */
	public static String join(Collection<?> list, String separator) {
		if (list == null || list.isEmpty()) {
			return null;
		}

		String sep = separator == null ? "" : separator;
		StringBuilder concat = new StringBuilder();
		for (Object obj : list) {
			concat.append(obj).append(sep);
		}
		if (concat.length() > sep.length()) {
			concat.delete(concat.length() - sep.length(), concat.length());
		}
		return concat.toString();
	}

	/**
	 * 获取Jdbc类型，jdbc:mysql://localhost:3306/yourDBName => mysql
	 *
	 * @param jdbcUrl
	 * @return
	 */
	public static String getJdbcType(String jdbcUrl) {
		if (jdbcUrl == null || jdbcUrl.isEmpty()) {
			return null;
		}

		int len = 5;
		int index = jdbcUrl.indexOf(":", len);
		if (index == -1) {
			return null;
		}

		String jdbcType = jdbcUrl.substring(len, index);
		return jdbcType;
	}

}
