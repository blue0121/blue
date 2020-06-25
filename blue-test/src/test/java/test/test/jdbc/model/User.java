package test.test.jdbc.model;

import blue.core.util.JsonUtil;
import blue.jdbc.annotation.Entity;
import blue.jdbc.annotation.Id;
import blue.jdbc.annotation.Version;

@Entity(table = "user")
public class User
{
	@Id
	private Integer id;
	private Integer groupId;
	@Version
	private Integer version;
	private String name;
	private String password;
	private Integer state;

	public User()
	{
	}

	public User(String name, String password)
	{
		this(name, password, null);
	}

	public User(String name, String password, Integer state)
	{
		this.name = name;
		this.password = password;
		this.state = state;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getGroupId()
	{
		return groupId;
	}

	public void setGroupId(Integer groupId)
	{
		this.groupId = groupId;
	}

	public Integer getVersion()
	{
		return version;
	}

	public void setVersion(Integer version)
	{
		this.version = version;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public Integer getState()
	{
		return state;
	}

	public void setState(Integer state)
	{
		this.state = state;
	}

	@Override
	public String toString()
	{
		return JsonUtil.output(this);
	}
}
