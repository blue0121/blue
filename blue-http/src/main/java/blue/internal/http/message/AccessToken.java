package blue.internal.http.message;


public class AccessToken
{
	private String token;
	private int expire = 0;

	public AccessToken()
	{
	}

	public AccessToken(String token)
	{
		this.token = token;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public int getExpire()
	{
		return expire;
	}

	public void setExpire(int expire)
	{
		this.expire = expire;
	}

}
