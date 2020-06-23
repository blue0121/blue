package blue.internal.validation.validator;

import blue.validation.annotation.Fax;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FaxValidator implements ConstraintValidator<Fax, String>
{
	private static Pattern pattern = Pattern.compile(
			"^((\\(\\d{2,3}\\))|(\\d{3}\\-))?(\\(0\\d{2,3}\\)|0\\d{2,3}-)?[1-9]\\d{6,7}(\\-\\d{1,4})?$");

	public FaxValidator()
	{
	}

	@Override
	public void initialize(Fax anno)
	{
	}

	@Override
	public boolean isValid(String text, ConstraintValidatorContext context)
	{
		return isValid(text);
	}

	public static boolean isValid(String fax)
	{
		if (fax == null || fax.isEmpty())
			return true;

		Matcher matcher = pattern.matcher(fax);
		return matcher.matches();
	}
}
