package blue.validation.core;

import blue.validation.core.validator.IdCardValidator;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-23
 */
public class IdCardUtil {
	private IdCardUtil() {
	}

	public static boolean isValid(String idNo, boolean strict) {
		return IdCardValidator.isValid(idNo, strict);
	}

}
