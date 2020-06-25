package test.jdbc.model2;

import blue.jdbc.annotation.Entity;
import blue.jdbc.annotation.Id;
import blue.jdbc.annotation.Version;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-22
 */
@Entity
public class User2
{
	@Id
	private Integer id;
	@Version
	private Integer version;
	@Version
	private Integer version2;

	public User2()
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

	public Integer getVersion()
	{
		return version;
	}

	public void setVersion(Integer version)
	{
		this.version = version;
	}

	public Integer getVersion2()
	{
		return version2;
	}

	public void setVersion2(Integer version2)
	{
		this.version2 = version2;
	}
}
