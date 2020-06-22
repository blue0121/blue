package blue.core.util;

/**
 * 字符串掩码
 * 
 * @author zhengj
 * @since 1.0 2015-5-23
 */
public class MaskUtil
{
	private MaskUtil()
	{
	}
	
	/**
	 * 手机号码掩码
	 * 
	 * @param mobile
	 * @return
	 */
	public static String mobile(String mobile)
	{
		if (mobile == null || mobile.equals(""))
			return "没有填写";
		
		if (mobile.length() < 9)
			return "手机号码错误";
		
		StringBuilder sb = new StringBuilder(16);
		sb.append(mobile.substring(0, 3));
		sb.append("****");
		sb.append(mobile.substring(7));
		return sb.toString();
	}
	
	/**
	 * 电子邮箱掩码
	 * 
	 * @param email
	 * @return
	 */
	public static String email(String email)
	{
		if (email == null || email.equals(""))
			return "没有填写";
		
		int at = email.indexOf('@');
		if (at < 0)
			return "电子邮箱错误";

		String pre = email.substring(0, at);
		StringBuilder sb = new StringBuilder(16);
		sb.append(pre.substring(0, pre.length() / 2));
		sb.append("****");
		sb.append(email.substring(at));
		return sb.toString();
	}
	
	/**
	 * 令牌掩码
	 * 
	 * @param token
	 * @return
	 */
	public static String token(String token)
	{
		if (token == null || token.isEmpty())
			return "没有填写";

		int start = token.length() / 2;
		StringBuilder t = new StringBuilder(token);
		t.replace(start, t.length() - start, "*****");
		return t.toString();
	}
	
}
