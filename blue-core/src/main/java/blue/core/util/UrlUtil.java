package blue.core.util;

import java.io.File;

/**
 * URL工具类
 * 
 * @author zhengj
 * @since 1.0 2012-11-12
 */
public class UrlUtil
{
	private UrlUtil()
	{
	} 
	
	/**
	 * 把主键ID值转换为路径
	 * 
	 * @param val 主键ID值
	 * @return 路径，前后都带'/'
	 */
	public static String path(long val)
	{
		return path(val, 0);
	}
	
	/**
	 * 把主键ID值转换为路径
	 * 
	 * @param val 主键ID值
	 * @param level 层次
	 * @return 路径，前都带'/'，后不带
	 */
	public static String path(long val, int level)
	{
		StringBuilder sb = new StringBuilder(String.valueOf(val));
		if (sb.length() % 2 != 0)
			sb.insert(0, "0");
		
		int n = level * 2 - sb.length();
		if (n > 0)
		{
			for (int i = 0; i < n; i++)
			{
				sb.insert(0, "0");
			}
		}
		
		for (int i = 0; i < sb.length(); i+=3)
		{
			sb.insert(i, "/");
		}
		return sb.toString();
	}
	
	/**
	 * 把相对路径转换为绝对路径
	 * 
	 * @param url 网页地址
	 * @param link 网页内超链接
	 * @return 绝对路径的超链接
	 */
	public static String getLink(String url, String link)
	{
		String link2 = link.toLowerCase();
		//url = url.toLowerCase();
		if (link2.startsWith("http://"))
		{
		}
		else if (link2.startsWith("/"))
		{
			int pos = url.indexOf("/", 8);
			String prefix = url.substring(0, pos);
			link = prefix + link;
		}
		else
		{
			int pos = url.lastIndexOf("/");
			String prefix = url.substring(0, pos + 1);
			link = prefix + link;
		}
		return link;
	}
	
	/**
	 * 取得主机的名称
	 * 
	 * @param url 网页地址
	 * @return 主机名称
	 */
	public static String getHost(String url)
	{
		//url = url.toLowerCase();
		int start = url.indexOf("//");
		int end = url.indexOf("/", start + 2);
		if (end != -1)
			return url.substring(start + 2, end);
		else
			return url.substring(start+ 2);
	}
	
	/**
	 * 取得文件的名称
	 * 
	 * @param url URL地址
	 * @return 文件名称
	 */
	public static String getFileName(String url)
	{
		//url = url.toLowerCase();
		int pos = url.lastIndexOf("/");
		if (pos != -1)
		{
			return url.substring(pos + 1, url.length());
		}
		else
		{
			return url;
		}
	}
	
	/**
	 * 取得文件的扩展名
	 * 
	 * @param url URL地址
	 * @return 文件扩展名
	 */
	public static String getFileExt(String url)
	{
		//url = url.toLowerCase();
		int pos = url.lastIndexOf(".");
		if (pos != -1)
		{
			return url.substring(pos, url.length()).toLowerCase();
		}
		else
		{
			return getFileName(url);
		}
	}

	/**
	 * 去除前缀，取得真实路径
	 *
	 * @param root 前缀
	 * @param url 完整路径
	 * @return 真实路径
	 */
	public static String getRealPath(String root, String url)
	{
		if (root == null || root.isEmpty() || root.equals("/") || url == null || url.isEmpty() || url.equals("/"))
			return url;

		if (url.startsWith(root))
			url = url.substring(root.length(), url.length());

		return url;
	}
	
	/**
	 * 取得文件的缩略图路径
	 * 
	 * @param path 原路径
	 * @return 缩略图路径
	 */
	public static String getThumbPath(String path)
	{
		path = path.toLowerCase();
		int pos = path.lastIndexOf(".");
		if (pos != -1)
		{
			String ext = path.substring(pos, path.length()).toLowerCase();
			String src = path.substring(0, pos);
			return src + "_thumb" + ext;
		}
		else
		{
			return path + "_thumb";
		}
	}
	
	/**
	 * 取得文件的相对路径
	 * 
	 * @param absolute 文件的绝对路径
	 * @param root 文件根路径
	 * @return 文件的相对路径
	 */
	public static String getRelativePath(String absolute, String root)
	{
		String path = absolute;
		int pos = -1;
		if ((pos = path.indexOf(":")) != -1)
			path = path.substring(pos + 1).replaceAll("\\\\", "/");
		
		if ((pos = root.indexOf(":")) != -1)
			root = root.substring(pos + 1).replaceAll("\\\\", "/");
		
		return path.substring(root.length());
	}
	
	/**
	 * 获取当前操作系统的临时文件目录
	 * 
	 * @return 当前操作系统的临时文件目录
	 */
	public static String getTempDir()
	{
		String dir = System.getProperty("java.io.tmpdir");
		return dir + File.separator;
	}
	
	/**
	 * URL匹配
	 * 
	 * @param url 具体的URL
	 * @param tpl URL匹配模板
	 * @return 匹配成功返回true，匹配失败返回false
	 */
	public static boolean match(String url, String tpl)
	{
		if (tpl == null || tpl.isEmpty())
			return false;
		
		if ("/*".equals(tpl))
			return true;
		
		if (tpl.endsWith("*"))
		{
			String tpll = tpl.substring(0, tpl.length() - 1);
			return url.startsWith(tpll);
		}
		
		return url.equals(tpl);
	}
	
}
