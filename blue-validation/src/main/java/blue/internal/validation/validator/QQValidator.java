package blue.internal.validation.validator;


import blue.validation.annotation.QQ;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QQValidator implements ConstraintValidator<QQ, String>
{
	private static Pattern pattern = Pattern.compile("^[1-9]\\d{4,11}$");
	
	public QQValidator()
	{
	}

	@Override
	public void initialize(QQ anno)
	{
	}

	@Override
	public boolean isValid(String text, ConstraintValidatorContext context)
	{
		return isValid(text);
	}

	public static boolean isValid(String qq)
	{
		if (qq == null || qq.isEmpty())
			return true;

		Matcher matcher = pattern.matcher(qq);
		return matcher.matches();
	}

}
