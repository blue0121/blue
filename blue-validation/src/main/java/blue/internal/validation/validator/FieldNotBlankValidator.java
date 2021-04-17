package blue.internal.validation.validator;


import blue.validation.annotation.FieldNotBlank;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;

import java.lang.reflect.Method;
import java.util.Map;

public class FieldNotBlankValidator extends FieldValidator implements ConstraintValidator<FieldNotBlank, Object>
{
	
	public FieldNotBlankValidator()
	{
	}

	@Override
	public void initialize(FieldNotBlank anno)
	{
		this.fields = anno.fields();

		if (fields == null || fields.length == 0)
			throw new IllegalArgumentException("@FieldNotBlank 字段不能少于1个");
	}

	@Override
	public boolean isValid(Object obj, ConstraintValidatorContext context)
	{
		if (obj == null)
			return true;
		
		Map<String, Method> methodMap = this.init(obj.getClass());
		
		for (int i = 0; i < fields.length; i++)
		{
			String field = fields[i];
			Method method = methodMap.get(field);
			if (method == null)
				throw new ValidationException(field + " 字段不存在");
			
			Object current = null;
			try
			{
				current = method.invoke(obj);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			if (current != null && !current.equals(""))
				return true;
		}
		
		return false;
	}
	
	
}
