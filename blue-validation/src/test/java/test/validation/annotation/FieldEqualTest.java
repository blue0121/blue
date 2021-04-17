package test.validation.annotation;

import blue.validation.ValidationUtil;
import blue.validation.group.SaveModel;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.validation.model.Group;
import test.validation.model.User;

public class FieldEqualTest
{
	public FieldEqualTest()
	{
	}
	
	@Test
	public void test1()
	{
		User user = new User();
		user.setPassword("123");
		user.setRePassword("123");
		ValidationUtil.valid(user, SaveModel.class);
	}
	
	@Test
	public void test2()
	{
		Group group = new Group();
		group.setName("123");
		Assertions.assertThrows(ValidationException.class, () -> ValidationUtil.valid(group, SaveModel.class));
	}
	
	@Test
	public void test3()
	{
		User user = new User();
		user.setPassword("123");
		user.setRePassword("12");
		Assertions.assertThrows(ValidationException.class, () -> ValidationUtil.valid(user, SaveModel.class));

	}
	
}
