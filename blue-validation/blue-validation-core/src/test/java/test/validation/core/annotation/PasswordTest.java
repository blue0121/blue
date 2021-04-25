package test.validation.core.annotation;

import blue.base.core.security.DigestUtil;
import blue.validation.core.ValidationUtil;
import blue.validation.core.group.UpdateGroup;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.validation.core.model.User;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-23
 */
public class PasswordTest {
	public PasswordTest() {
	}

	@Test
	public void test1() {
		User user = new User();
		user.setPassword(DigestUtil.md5Hex("password"));
		user.setRePassword(DigestUtil.sha1Hex("password"));
		ValidationUtil.valid(user, UpdateGroup.class);
	}

	@Test
	public void test2() {
		User user = new User();
		user.setPassword("password");
		user.setRePassword("password");
		Assertions.assertThrows(ValidationException.class, () -> ValidationUtil.valid(user, UpdateGroup.class));
	}

	@Test
	public void test3() {
		User user = new User();
		user.setPassword("012345678901234567890123456789tf");
		user.setRePassword(DigestUtil.sha1Hex("password"));
		Assertions.assertThrows(ValidationException.class, () -> ValidationUtil.valid(user, UpdateGroup.class));
	}

}
