package blue.validation.core.validator;

import blue.validation.core.annotation.IdCard;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-23
 */
public class IdCardValidator implements ConstraintValidator<IdCard, String> {
	private static Pattern pattern = Pattern.compile("^\\d{15}(\\d{2}[A-Za-z0-9])?$");

	private boolean strict;

	public IdCardValidator() {
	}

	@Override
	public void initialize(IdCard anno) {
		this.strict = anno.strict();
	}

	@Override
	public boolean isValid(String text, ConstraintValidatorContext context) {
		return isValid(text, strict);
	}

	public static boolean isValid(String idCard, boolean strict) {
		if (idCard == null || idCard.isEmpty()) {
			return true;
		}

		if (strict) {
			return StrictIdCardValidator.isValid(idCard);
		}
		else {
			Matcher matcher = pattern.matcher(idCard);
			return matcher.matches();
		}
	}

}
