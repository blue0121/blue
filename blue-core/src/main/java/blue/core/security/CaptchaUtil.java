package blue.core.security;


import blue.core.util.RandomUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;


public class CaptchaUtil
{
	public static final String SESSION_KEY = "_CAPTCHA_CODE_";

	private static Random random = new Random();
	
	public CaptchaUtil()
	{
	}

	/**
	 * 生成验证码
	 * 
	 * @param type 随机字符类型
	 * @param os 输出流
	 * @return 验证码
	 * @throws IOException
	 */
	public static String gen(RandomUtil.RandomType type, OutputStream os) throws IOException
	{
		return gen(26, 4, type, os);
	}
	
	/**
	 * 生成验证码
	 * 
	 * @param num 验证码个数，最少4个
	 * @param type 随机字符类型
	 * @param os 输出流
	 * @return 验证码
	 * @throws IOException
	 */
	public static String gen(int height, int num, RandomUtil.RandomType type, OutputStream os) throws IOException
	{
		if (num < 4)
			num = 4;
		
		//int width = 16 * num + 12;
		int width = (height /2+height/6) * num + height / 4;
		//int height = 26;
		
		// 在内存中创建图象
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 获取图形上下文
		Graphics g = image.getGraphics();
		// 设定背景色
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		// 随机产生height*5条干扰线，使图象中的认证码不易被其它程序探测到
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < height * 5; i++)
		{
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(height/2);
			int yl = random.nextInt(height/2);
			g.drawLine(x, y, x + xl, y + yl);
		}
		
		// 设定字体
		g.setFont(new Font("Times New Roman", Font.PLAIN, height/3));
		// 随机产生10个干扰字体，使图象中的认证码不易被其它程序探测到
		String tmpArr = RandomUtil.rand(RandomUtil.RandomType.UPPER_LOWER_CASE_NUMBER, num *3);
		for (int i = 0; i < tmpArr.length(); i++) {
			String rand = String.valueOf(tmpArr.charAt(random.nextInt(tmpArr.length())));
			// 将认证码显示到图象中
			g.setColor(getRandColor(60, 250));
			// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
			g.drawString(rand,random.nextInt(width),random.nextInt(height));
		}

		// 设定字体
		//g.setFont(new Font("TimesRoman", Font.BOLD, 20));
		g.setFont(new Font("TimesRoman", Font.BOLD, height));
		// 取随机产生的认证码(4位数字)
		String str = RandomUtil.rand(type, num);
		for (int i = 0; i < str.length(); i++)
		{
			String rand = String.valueOf(str.charAt(i));
			// 将认证码显示到图象中
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
			//g.drawString(rand, 16 * i + 6, 21);
			g.drawString(rand, (height /2+height/6)  * i + 6, height/2 + height/3);
		}

		// 图象生效
		g.dispose();
		try
		{
			ImageIO.write(image, "JPEG", os);
		}
		finally
		{
			try
			{
				os.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return str;
	}

	/**
	 * 给定范围获得随机颜色
	 */
	private static Color getRandColor(int fc, int bc)
	{
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

}
