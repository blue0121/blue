package blue.validation.core.validator;

import blue.validation.core.annotation.Zip;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-23
 */
public class ZipValidator implements ConstraintValidator<Zip, String> {
	private static Pattern pattern = Pattern.compile("^[1-9]\\d{5}$");

	public ZipValidator() {
	}

	@Override
	public void initialize(Zip anno) {
	}

	@Override
	public boolean isValid(String text, ConstraintValidatorContext context) {
		return isValid(text);
	}

	public static boolean isValid(String zip) {
		if (zip == null || zip.isEmpty()) {
			return true;
		}

		Matcher matcher = pattern.matcher(zip);
		return matcher.matches();
	}

}
