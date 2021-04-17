package blue.internal.validation.validator;

import blue.validation.annotation.FieldEqual;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;

import java.lang.reflect.Method;
import java.util.Map;

public class FieldEqualValidator extends FieldValidator implements ConstraintValidator<FieldEqual, Object>
{
	public FieldEqualValidator()
	{
	}

	@Override
	public void initialize(FieldEqual anno)
	{
		this.fields = anno.fields();

		if (fields == null || fields.length < 2)
			throw new IllegalArgumentException("@FieldEqual 验证字段不能少于2个");
	}

	@Override
	public boolean isValid(Object obj, ConstraintValidatorContext context)
	{
		if (obj == null)
			return true;

		Map<String, Method> methodMap = this.init(obj.getClass());

		Object value = null;
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

			if (i > 0)
			{
				if (value == null)
				{
					if (current != null)
						return false;
				}
				else
				{
					if (!value.equals(current))
						return false;
				}
			}
			value = current;
		}

		return true;
	}
}
