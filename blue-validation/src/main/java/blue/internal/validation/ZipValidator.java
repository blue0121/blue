package blue.internal.validation;


import blue.validation.annotation.Zip;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZipValidator implements ConstraintValidator<Zip, String>
{
	private static Pattern pattern = Pattern.compile("^[1-9]\\d{5}$");
	
	public ZipValidator()
	{
	}

	@Override
	public void initialize(Zip anno)
	{
	}

	@Override
	public boolean isValid(String text, ConstraintValidatorContext context)
	{
		return isValid(text);
	}

	public static boolean isValid(String zip)
	{
		if (zip == null || zip.isEmpty())
			return true;

		Matcher matcher = pattern.matcher(zip);
		return matcher.matches();
	}

}
