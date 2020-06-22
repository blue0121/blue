package test.validation.model;


import blue.validation.annotation.FieldEqual;
import blue.validation.annotation.FieldNotBlank;
import blue.validation.annotation.Password;
import blue.validation.annotation.PasswordType;
import blue.validation.group.GetModel;
import blue.validation.group.SaveModel;
import blue.validation.group.UpdateModel;

@FieldEqual(fields={"password", "rePassword"}, groups={SaveModel.class}, message="2次密码不一致")
@FieldNotBlank(fields={"id", "name"}, groups={GetModel.class}, message="ID和用户名不能全部为空")
public class User
{
	private Integer id;
	private String name;

	@Password(type = PasswordType.MD5, groups = {UpdateModel.class})
	private String password;
	@Password(type = PasswordType.SHA1, groups = {UpdateModel.class})
	private String rePassword;

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

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getRePassword()
	{
		return rePassword;
	}

	public void setRePassword(String rePassword)
	{
		this.rePassword = rePassword;
	}

}
