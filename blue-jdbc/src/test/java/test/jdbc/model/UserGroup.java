package test.jdbc.model;

import blue.jdbc.annotation.Mapper;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-22
 */
@Mapper
public class UserGroup
{
	private Integer userId;
	private String userName;
	private Integer groupId;
	private String groupName;

	public UserGroup()
	{
	}

	public Integer getUserId()
	{
		return userId;
	}

	public void setUserId(Integer userId)
	{
		this.userId = userId;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public Integer getGroupId()
	{
		return groupId;
	}

	public void setGroupId(Integer groupId)
	{
		this.groupId = groupId;
	}

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}
}
