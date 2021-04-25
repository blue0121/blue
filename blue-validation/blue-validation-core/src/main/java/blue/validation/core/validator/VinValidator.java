package blue.validation.core.validator;

import blue.validation.core.annotation.Vin;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-23
 */
public class VinValidator implements ConstraintValidator<Vin, String> {

	private static Pattern pattern = Pattern.compile("^[A-Z0-9]{17}$");

	public VinValidator() {
	}

	@Override
	public void initialize(Vin constraintAnnotation) {
	}

	@Override
	public boolean isValid(String text, ConstraintValidatorContext context) {
		return isValid(text);
	}

	public static boolean isValid(String vin) {
		if (vin == null || vin.isEmpty()) {
			return true;
		}

		Matcher matcher = pattern.matcher(vin);
		return matcher.matches();
	}

}
