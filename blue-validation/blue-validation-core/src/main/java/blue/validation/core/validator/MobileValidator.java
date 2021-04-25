package blue.validation.core.validator;

import blue.validation.core.annotation.Mobile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-23
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {
	private static Pattern pattern = Pattern.compile("^1\\d{10}$");

	public MobileValidator() {
	}

	@Override
	public void initialize(Mobile anno) {
	}

	@Override
	public boolean isValid(String text, ConstraintValidatorContext context) {
		return isValid(text);
	}

	public static boolean isValid(String mobile) {
		if (mobile == null || mobile.isEmpty()) {
			return true;
		}

		Matcher matcher = pattern.matcher(mobile);
		return matcher.matches();
	}


}
