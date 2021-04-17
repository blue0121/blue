package blue.internal.validation.validator;


import blue.validation.annotation.IdCard;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdCardValidator implements ConstraintValidator<IdCard, String>
{
	private static Pattern pattern = Pattern.compile("^\\d{15}(\\d{2}[A-Za-z0-9])?$");

	private boolean strict;
	
	public IdCardValidator()
	{
	}

	@Override
	public void initialize(IdCard anno)
	{
		this.strict = anno.strict();
	}

	@Override
	public boolean isValid(String text, ConstraintValidatorContext context)
	{
		return isValid(text, strict);
	}

	public static boolean isValid(String idCard, boolean strict)
	{
		if (idCard == null || idCard.isEmpty())
			return true;

		if (strict)
		{
			return StrictIdCardValidator.isValid(idCard);
		}
		else
		{
			Matcher matcher = pattern.matcher(idCard);
			return matcher.matches();
		}
	}

}
