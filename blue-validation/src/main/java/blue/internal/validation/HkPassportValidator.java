package blue.internal.validation;


import blue.validation.annotation.HkPassport;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HkPassportValidator implements ConstraintValidator<HkPassport, String>
{
	private static Pattern pattern = Pattern.compile("^[A-Za-z]\\d{1,15}$");
	
	public HkPassportValidator()
	{
	}

	@Override
	public void initialize(HkPassport anno)
	{
	}

	@Override
	public boolean isValid(String text, ConstraintValidatorContext context)
	{
		return isValid(text);
	}

	public static boolean isValid(String hkPassport)
	{
		if (hkPassport == null || hkPassport.isEmpty())
			return true;

		Matcher matcher = pattern.matcher(hkPassport);
		return matcher.matches();
	}

}
