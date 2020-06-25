package test.test.jdbc.model;

import blue.jdbc.annotation.Entity;
import blue.jdbc.annotation.Id;

/**
 * @author Jin Zheng
 * @since 1.0 2019-12-17
 */
@Entity(table = "group")
public class Group
{
	@Id
	private Integer id;
	private String name;
	private Integer count = 0;

	public Group()
	{
	}

	public Group(String name)
	{
		this.name = name;
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

	public Integer getCount()
	{
		return count;
	}

	public void setCount(Integer count)
	{
		this.count = count;
	}
}
