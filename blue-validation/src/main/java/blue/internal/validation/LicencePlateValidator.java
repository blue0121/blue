package blue.internal.validation;


import blue.validation.annotation.LicencePlate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LicencePlateValidator implements ConstraintValidator<LicencePlate, String>
{
	private static Pattern pattern = Pattern.compile("^[\\u4E00-\\u9FA5][\\da-zA-Z]{6,7}$");

	public LicencePlateValidator()
	{
	}

	@Override
	public void initialize(LicencePlate anno)
	{
	}

	@Override
	public boolean isValid(String text, ConstraintValidatorContext context)
	{
		return isValid(text);
	}

	public static boolean isValid(String carNo)
	{
		if (carNo == null || carNo.isEmpty())
			return true;

		Matcher matcher = pattern.matcher(carNo);
		return matcher.matches();
	}


}
