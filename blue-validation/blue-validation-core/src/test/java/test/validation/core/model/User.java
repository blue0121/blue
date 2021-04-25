package test.validation.core.model;

import blue.validation.core.annotation.FieldEqual;
import blue.validation.core.annotation.FieldNotBlank;
import blue.validation.core.annotation.Password;
import blue.validation.core.annotation.PasswordType;
import blue.validation.core.group.GetGroup;
import blue.validation.core.group.SaveGroup;
import blue.validation.core.group.UpdateGroup;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-23
 */
@FieldEqual(fields = {"password", "rePassword"}, groups = {SaveGroup.class}, message = "2次密码不一致")
@FieldNotBlank(fields = {"id", "name"}, groups = {GetGroup.class}, message = "ID和用户名不能全部为空")
public class User {
	private Integer id;
	private String name;

	@Password(type = PasswordType.MD5, groups = {UpdateGroup.class})
	private String password;
	@Password(type = PasswordType.SHA1, groups = {UpdateGroup.class})
	private String rePassword;

	public User() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRePassword() {
		return rePassword;
	}

	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}

}
