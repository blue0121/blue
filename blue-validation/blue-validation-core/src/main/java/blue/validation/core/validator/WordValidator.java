package blue.validation.core.validator;

import blue.validation.core.annotation.Word;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-23
 */
public class WordValidator implements ConstraintValidator<Word, String> {
	private static Pattern pattern = Pattern.compile("^\\w*$");

	public WordValidator() {
	}

	@Override
	public void initialize(Word anno) {
	}

	@Override
	public boolean isValid(String text, ConstraintValidatorContext context) {
		return isValid(text);
	}

	public static boolean isValid(String text) {
		if (text == null || text.isEmpty()) {
			return true;
		}

		Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}

}
