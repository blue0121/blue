package blue.internal.validation;


import blue.validation.annotation.NumberIn;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NumberInValidator implements ConstraintValidator<NumberIn, Number>
{
	private long[] in;
	
	public NumberInValidator()
	{
	}

	@Override
	public void initialize(NumberIn anno)
	{
		in = anno.in();
	}

	@Override
	public boolean isValid(Number num, ConstraintValidatorContext context)
	{
		if (num == null)
			return true;
		
		if (in == null || in.length == 0)
			return true;
		
		long val = num.longValue();
		for (long t : in)
		{
			if (val == t)
				return true;
		}
		return false;
	}
}
