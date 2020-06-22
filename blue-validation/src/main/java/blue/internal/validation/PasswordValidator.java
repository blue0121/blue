package blue.internal.validation;


import blue.validation.annotation.Password;
import blue.validation.annotation.PasswordType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<Password, String>
{
	private static Pattern md5Pattern = Pattern.compile("^[\\da-fA-F]{32}$");
	private static Pattern sha1Pattern = Pattern.compile("^[\\da-fA-F]{40}$");

	private PasswordType type;

	public PasswordValidator()
	{
	}

	@Override
	public void initialize(Password anno)
	{
		this.type = anno.type();
	}

	@Override
	public boolean isValid(String text, ConstraintValidatorContext context)
	{
		return isValid(text, type);
	}

	public static boolean isValid(String pwd, PasswordType type)
	{
		if (pwd == null || pwd.isEmpty())
			return true;

		switch (type)
		{
			case PLAIN:
				return true;
			case MD5:
				return md5Pattern.matcher(pwd).matches();
			case SHA1:
				return sha1Pattern.matcher(pwd).matches();

		}

		return true;
	}


}
