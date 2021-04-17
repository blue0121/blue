package blue.internal.validation.validator;


import blue.validation.annotation.Phone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<Phone, String>
{
	private static Pattern pattern = Pattern.compile("(^[0-9]{3,4}\\-[0-9]{7,8}$)|(^[0-9]{7,8}$)|(^\\([0-9]{3,4}\\)[0-9]{3,8}$)|(^0{0,1}1\\d{10}$)");
	
	public PhoneValidator()
	{
	}

	@Override
	public void initialize(Phone anno)
	{
	}

	@Override
	public boolean isValid(String text, ConstraintValidatorContext context)
	{
		return isValid(text);
	}

	public static boolean isValid(String phone)
	{
		if (phone == null || phone.isEmpty())
			return true;

		Matcher matcher = pattern.matcher(phone);
		return matcher.matches();
	}

}
