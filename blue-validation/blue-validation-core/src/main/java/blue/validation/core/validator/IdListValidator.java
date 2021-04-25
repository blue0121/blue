package blue.validation.core.validator;

import blue.validation.core.annotation.IdList;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-23
 */
public class IdListValidator implements ConstraintValidator<IdList, String> {
	private static Pattern pattern = Pattern.compile("^[+-]?\\d*[1-9]\\d*$");

	public IdListValidator() {
	}

	@Override
	public void initialize(IdList anno) {
	}

	@Override
	public boolean isValid(String text, ConstraintValidatorContext context) {
		return isValid(text);
	}

	public static boolean isValid(String text) {
		if (text == null || text.isEmpty()) {
			return true;
		}

		for (String strId : text.split(",")) {
			Matcher matcher = pattern.matcher(strId.trim());
			if (!matcher.matches()) {
				return false;
			}
		}

		return true;
	}

}
