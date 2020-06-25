package test.jdbc.model2;

import blue.jdbc.annotation.Entity;
import blue.jdbc.annotation.Id;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-22
 */
@Entity
public class User3
{
	@Id
	private Short id;
	private String name;

	public User3()
	{
	}

	public Short getId()
	{
		return id;
	}

	public void setId(Short id)
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
