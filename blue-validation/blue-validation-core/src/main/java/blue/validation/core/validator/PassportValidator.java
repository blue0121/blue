package blue.validation.core.validator;

import blue.validation.core.annotation.Passport;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-23
 */
public class PassportValidator implements ConstraintValidator<Passport, String> {
	private static Pattern pattern = Pattern.compile("^1[45][0-9]{7}|G[0-9]{8}|P[0-9]{7}|S[0-9]{7,8}|D[0-9]+$");

	public PassportValidator() {
	}

	@Override
	public void initialize(Passport anno) {
	}

	@Override
	public boolean isValid(String text, ConstraintValidatorContext context) {
		return isValid(text);
	}

	public static boolean isValid(String passport) {
		if (passport == null || passport.isEmpty()) {
			return true;
		}

		Matcher matcher = pattern.matcher(passport);
		return matcher.matches();
	}

}
