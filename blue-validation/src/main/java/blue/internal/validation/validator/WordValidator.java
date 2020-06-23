package blue.internal.validation.validator;


import blue.validation.annotation.Word;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordValidator implements ConstraintValidator<Word, String>
{
	private static Pattern pattern = Pattern.compile("^\\w*$");
	
	public WordValidator()
	{
	}

	@Override
	public void initialize(Word anno)
	{
	}

	@Override
	public boolean isValid(String text, ConstraintValidatorContext context)
	{
		return isValid(text);
	}

	public static boolean isValid(String text)
	{
		if (text == null || text.isEmpty())
			return true;

		Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}

}
