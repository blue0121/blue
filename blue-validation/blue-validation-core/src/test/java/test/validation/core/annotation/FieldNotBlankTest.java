package test.validation.core.annotation;

import blue.validation.core.ValidationUtil;
import blue.validation.core.group.GetGroup;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.validation.core.model.User;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-23
 */
public class FieldNotBlankTest {
	public FieldNotBlankTest() {
	}

	@Test
	public void test1() {
		User user = new User();
		user.setId(1);
		ValidationUtil.valid(user, GetGroup.class);
	}

	@Test
	public void test2() {
		User user = new User();
		Assertions.assertThrows(ValidationException.class, () -> ValidationUtil.valid(user, GetGroup.class));
	}

}
