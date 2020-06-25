package test.jdbc.model;

import blue.jdbc.annotation.Entity;
import blue.jdbc.annotation.Id;
import blue.jdbc.annotation.Transient;
import blue.jdbc.annotation.Version;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-22
 */
@Entity(table = "usr_user")
public class User
{
	@Id
	private Integer id;
	private Integer groupId;
	@Version
	private Integer version;
	private String name;
	private String password;
	private Integer count;

	@Transient
	private String groupName;

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

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
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
