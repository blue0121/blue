package blue.validation.core.validator;

import blue.validation.core.annotation.LicencePlate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-23
 */
public class LicencePlateValidator implements ConstraintValidator<LicencePlate, String> {
	private static Pattern pattern = Pattern.compile("^[\\u4E00-\\u9FA5][\\da-zA-Z]{6,7}$");

	public LicencePlateValidator() {
	}

	@Override
	public void initialize(LicencePlate anno) {
	}

	@Override
	public boolean isValid(String text, ConstraintValidatorContext context) {
		return isValid(text);
	}

	public static boolean isValid(String carNo) {
		if (carNo == null || carNo.isEmpty()) {
			return true;
		}

		Matcher matcher = pattern.matcher(carNo);
		return matcher.matches();
	}


}
