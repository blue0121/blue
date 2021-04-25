package blue.validation.core.validator;

import blue.validation.core.annotation.Entcode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-23
 */
public class EntcodeValidator implements ConstraintValidator<Entcode, String> {
	private static Pattern pattern = Pattern.compile("^([0-9a-zA-Z]){8}-[0-9a-zA-Z]+$");

	public EntcodeValidator() {
	}

	@Override
	public void initialize(Entcode anno) {
	}

	@Override
	public boolean isValid(String text, ConstraintValidatorContext context) {
		return isValid(text);
	}

	public static boolean isValid(String entcode) {
		if (entcode == null || entcode.isEmpty()) {
			return true;
		}

		Matcher matcher = pattern.matcher(entcode);
		return matcher.matches();
	}

}
