package blue.validation.core.validator;

import blue.validation.core.annotation.NumberIn;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-23
 */
public class NumberInValidator implements ConstraintValidator<NumberIn, Number> {
	private long[] in;

	public NumberInValidator() {
	}

	@Override
	public void initialize(NumberIn anno) {
		in = anno.in();
	}

	@Override
	public boolean isValid(Number num, ConstraintValidatorContext context) {
		if (num == null) {
			return true;
		}

		if (in == null || in.length == 0) {
			return true;
		}

		long val = num.longValue();
		for (long t : in) {
			if (val == t) {
				return true;
			}
		}
		return false;
	}
}
