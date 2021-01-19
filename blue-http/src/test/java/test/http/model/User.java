package test.http.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-19
 */
public class User
{
	@NotNull(message = "Must be required")
	private Integer id;
	@NotEmpty(message = "Must be required")
	private String name;

	public User()
	{
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
