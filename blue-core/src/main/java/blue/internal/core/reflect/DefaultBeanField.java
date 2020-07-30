package blue.internal.core.reflect;

import blue.core.reflect.BeanField;
import blue.core.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-24
 */
public class DefaultBeanField implements BeanField
{
	private static Logger logger = LoggerFactory.getLogger(DefaultBeanField.class);

	private final String fieldName;
	private final Object target;
	private final Field field;
	private final Method getterMethod;
	private final Method setterMethod;

	public DefaultBeanField(String fieldName, Object target, Field field, Method getterMethod, Method setterMethod)
	{
		this.fieldName = fieldName;
		this.target = target;
		this.field = field;
		this.getterMethod = getterMethod;
		this.setterMethod = setterMethod;
	}

	@Override
	public String getFieldName()
	{
		return fieldName;
	}

	@Override
	public String getColumnName()
	{
		return ReflectionUtil.fieldToColumn(fieldName);
	}

	@Override
	public Object getTarget()
	{
		return target;
	}

	@Override
	public Object getFieldValue()
	{
		if (target == null)
		{
			logger.warn("bean is null");
			return null;
		}

		Object value = null;
		try
		{
			if (getterMethod != null)
			{
				value = getterMethod.invoke(target);
			}
			if (value == null && field != null)
			{
				field.setAccessible(true);
				value = field.get(target);
			}
		}
		catch (Exception e)
		{
			logger.error("Invoke getter method error,", e);
		}
		return value;
	}

	@Override
	public void setFieldValue(Object value)
	{
		if (target == null || value == null)
		{
			logger.warn("bean or value is null");
			return;
		}

		try
		{
			boolean flag = false;
			if (setterMethod != null)
			{
				setterMethod.invoke(target, value);
				flag = true;
			}
			if (!flag && field != null)
			{
				field.setAccessible(true);
				field.set(target, value);
			}
		}
		catch (Exception e)
		{
			logger.error("Invoke setter method error,", e);
		}
	}

	@Override
	public Method getGetterMethod()
	{
		return getterMethod;
	}

	@Override
	public Method getSetterMethod()
	{
		return setterMethod;
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass)
	{
		T annotation = null;
		if (setterMethod != null)
		{
			annotation = setterMethod.getAnnotation(annotationClass);
		}
		if (annotation == null && field != null)
		{
			annotation = field.getAnnotation(annotationClass);
		}
		return annotation;
	}
}
