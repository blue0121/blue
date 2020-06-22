package blue.core.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 货币工具类
 * 
 * @author zhengj
 * @since 2014-12-5 1.0
 */
public class MoneyUtil
{
	private MoneyUtil()
	{
	}
	
	/**
	 * 格式化金额，2位小数
	 * 
	 * @param money 金额
	 * @return 2位小数字符串
	 */
	public static String format(double money)
	{
		DecimalFormat format = new DecimalFormat("#######################0.00");
		return format.format(money);
	}
	
	public static int compare(double d1, double d2)
	{
		DecimalFormat format = new DecimalFormat("#######################0.00");
		BigDecimal big1 = new BigDecimal(format.format(d1));
		BigDecimal big2 = new BigDecimal(format.format(d2));
		return big1.compareTo(big2);
	}
	
	/**
	 * 转换微信支付金额
	 * 
	 * @param d 金额
	 * @return
	 */
	public static int wxpayFee(double d)
	{
		DecimalFormat format = new DecimalFormat("#######################0.00");
		BigDecimal big = new BigDecimal(format.format(d));
		BigDecimal big2 = big.multiply(new BigDecimal(100));
		int i = big2.intValue();
		return i;
	}
	
}
