package test.core.model;

/**
 * @author Jin Zheng
 * @since 2020-07-25
 */
public class TestModel2
{
	@CoreTest
	private String username;
	@CoreTest
	private String password;

	public TestModel2()
	{
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
}
