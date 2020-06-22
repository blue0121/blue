package test.validation.annotation;

import blue.core.security.DigestUtil;
import blue.validation.ValidationUtil;
import blue.validation.group.UpdateModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.validation.model.User;

import javax.validation.ValidationException;

public class PasswordTest
{
	public PasswordTest()
	{
	}
	
	@Test
	public void test1()
	{
		User user = new User();
		user.setPassword(DigestUtil.md5Hex("password"));
		user.setRePassword(DigestUtil.sha1Hex("password"));
		ValidationUtil.valid(user, UpdateModel.class);
	}
	
	@Test
	public void test2()
	{
		User user = new User();
		user.setPassword("password");
		user.setRePassword("password");
		Assertions.assertThrows(ValidationException.class, () -> ValidationUtil.valid(user, UpdateModel.class));
	}

	@Test
	public void test3()
	{
		User user = new User();
		user.setPassword("012345678901234567890123456789tf");
		user.setRePassword(DigestUtil.sha1Hex("password"));
		Assertions.assertThrows(ValidationException.class, () -> ValidationUtil.valid(user, UpdateModel.class));
	}
	
}
