package test.core.model;

/**
 * @author Jin Zheng
 * @since 2020-07-25
 */
public class TestModel
{
	private String username;
	private String password;

	public TestModel()
	{
	}

	public String getUsername()
	{
		return username;
	}

	@CoreTest
	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	@CoreTest
	public void setPassword(String password)
	{
		this.password = password;
	}
}
