package blue.internal.validation;


import blue.validation.annotation.StringIn;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StringInValidator implements ConstraintValidator<StringIn, String>
{
	private String[] in;
	
	public StringInValidator()
	{
	}

	@Override
	public void initialize(StringIn anno)
	{
		in = anno.in();
	}

	@Override
	public boolean isValid(String text, ConstraintValidatorContext context)
	{
		if (text == null || text.isEmpty())
			return true;
		
		if (in == null || in.length == 0)
			return true;
		
		for (String t : in)
		{
			if (t.equals(text))
				return true;
		}
		return false;
	}
}
