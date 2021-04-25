package test.validation.core.annotation;

import blue.validation.core.ValidationUtil;
import blue.validation.core.group.SaveGroup;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.validation.core.model.Group;
import test.validation.core.model.User;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-23
 */
public class FieldEqualTest {
	public FieldEqualTest() {
	}

	@Test
	public void test1() {
		User user = new User();
		user.setPassword("123");
		user.setRePassword("123");
		ValidationUtil.valid(user, SaveGroup.class);
	}

	@Test
	public void test2() {
		Group group = new Group();
		group.setName("123");
		Assertions.assertThrows(ValidationException.class, () -> ValidationUtil.valid(group, SaveGroup.class));
	}

	@Test
	public void test3() {
		User user = new User();
		user.setPassword("123");
		user.setRePassword("12");
		Assertions.assertThrows(ValidationException.class, () -> ValidationUtil.valid(user, SaveGroup.class));

	}

}
