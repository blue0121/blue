package blue.validation.core.validator;

import blue.validation.core.annotation.StringIn;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-23
 */
public class StringInValidator implements ConstraintValidator<StringIn, String> {
	private String[] in;

	public StringInValidator() {
	}

	@Override
	public void initialize(StringIn anno) {
		in = anno.in();
	}

	@Override
	public boolean isValid(String text, ConstraintValidatorContext context) {
		if (text == null || text.isEmpty()) {
			return true;
		}

		if (in == null || in.length == 0) {
			return true;
		}

		for (String t : in) {
			if (t.equals(text)) {
				return true;
			}
		}
		return false;
	}
}
