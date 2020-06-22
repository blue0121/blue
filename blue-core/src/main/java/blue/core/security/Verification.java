package blue.core.security;

/**
 * 验证对象
 * 
 * @author zhengj
 * @since 2012-8-12 1.0
 */
public class Verification
{
	// 验证码
	private String verify;
	// 远程表单保护
	private String uuid;

	// 错误信息
	private String message;

	public Verification()
	{
	}

	public String getVerify()
	{
		return verify;
	}

	public void setVerify(String verify)
	{
		this.verify = verify;
	}

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

}
