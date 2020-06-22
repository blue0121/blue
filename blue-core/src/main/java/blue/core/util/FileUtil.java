package blue.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class FileUtil
{
	private FileUtil()
	{
	}
	
	/**
	 * 文件转 Base64 编码
	 * 
	 * @param file 文件
	 * @return Base64 编码
	 * @throws IOException
	 */
	public static String toBase64(File file) throws IOException
	{
		AssertUtil.notNull(file, "文件");
		return toBase64(new FileInputStream(file));
	}
	
	/**
	 * 输入流转换 Base64 编码
	 * 
	 * @param is 输入流
	 * @return Base64 编码
	 * @throws IOException
	 */
	public static String toBase64(InputStream is) throws IOException
	{
		AssertUtil.notNull(is, "输入流");
		try (InputStream iis = is)
		{
			byte[] data = new byte[iis.available()];
			iis.read(data);
			String imgBase64 = Base64.getEncoder().encodeToString(data);
			return imgBase64;
		}
	}
	
	/**
	 * Base64 编码转换为文件
	 * 
	 * @param base64 编码
	 * @param file 文件
	 * @throws IOException
	 */
	public static void toFile(String base64, File file) throws IOException
	{
		AssertUtil.notEmpty(base64, "Base64编码");
		AssertUtil.notNull(file, "文件");
		if (base64.indexOf('\n') != -1)
		{
			base64 = base64.replaceAll("\n", "");
		}
		byte[] bytes = Base64.getDecoder().decode(base64);
		try (FileOutputStream fos = new FileOutputStream(file);)
		{
			fos.write(bytes);
			fos.flush();
		}
		
	}
	
	/**
	 * 把路径转为化输入流
	 * 
	 * @param path 路径
	 * @return 输入流
	 * @throws IOException
	 */
	public static InputStream toInputStream(String path) throws IOException
	{
		AssertUtil.notEmpty(path, "路径");
		InputStream is = FileUtil.class.getResourceAsStream(path);
		if (is == null)
		{
			is = new FileInputStream(path);
		}
		return is;
	}
	
}
