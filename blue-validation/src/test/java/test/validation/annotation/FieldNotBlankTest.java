package test.validation.annotation;

import blue.validation.ValidationUtil;
import blue.validation.group.GetModel;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.validation.model.User;

public class FieldNotBlankTest
{
	public FieldNotBlankTest()
	{
	}
	
	@Test
	public void test1()
	{
		User user = new User();
		user.setId(1);
		ValidationUtil.valid(user, GetModel.class);
	}
	
	@Test
	public void test2()
	{
		User user = new User();
		Assertions.assertThrows(ValidationException.class, () -> ValidationUtil.valid(user, GetModel.class));
	}
	
}
