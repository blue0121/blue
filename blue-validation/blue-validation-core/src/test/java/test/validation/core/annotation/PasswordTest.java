package test.validation.core.annotation;

import blue.base.core.security.Digest;
import blue.base.core.security.DigestType;
import blue.base.core.security.SecurityFactory;
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
	private Digest md5Digest = SecurityFactory.createDigest(DigestType.MD5);
	private Digest sha1Digest = SecurityFactory.createDigest(DigestType.SHA1);

	public PasswordTest() {
	}

	@Test
	public void test1() {
		User user = new User();
		user.setPassword(md5Digest.digestToHex("password"));
		user.setRePassword(sha1Digest.digestToHex("password"));
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
		user.setRePassword(sha1Digest.digestToHex("password"));
		Assertions.assertThrows(ValidationException.class, () -> ValidationUtil.valid(user, UpdateGroup.class));
	}

}
