package blue.validation.core.validator;

import blue.validation.core.annotation.Password;
import blue.validation.core.annotation.PasswordType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-23
 */
public class PasswordValidator implements ConstraintValidator<Password, String> {
	private static Pattern md5Pattern = Pattern.compile("^[\\da-fA-F]{32}$");
	private static Pattern sha1Pattern = Pattern.compile("^[\\da-fA-F]{40}$");

	private PasswordType type;

	public PasswordValidator() {
	}

	@Override
	public void initialize(Password anno) {
		this.type = anno.type();
	}

	@Override
	public boolean isValid(String text, ConstraintValidatorContext context) {
		return isValid(text, type);
	}

	public static boolean isValid(String pwd, PasswordType type) {
		if (pwd == null || pwd.isEmpty()) {
			return true;
		}

		switch (type) {
			case MD5:
				return md5Pattern.matcher(pwd).matches();
			case SHA1:
				return sha1Pattern.matcher(pwd).matches();
			default:
				return true;
		}
	}


}
