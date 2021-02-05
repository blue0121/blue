package blue.internal.core.reflect;

import blue.core.reflect.BeanField;
import blue.core.reflect.BeanMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private final BeanMethod getterMethod;
	private final BeanMethod setterMethod;

	public DefaultBeanField(String fieldName, Object target, Field field,
	                        BeanMethod getterMethod, BeanMethod setterMethod)
	{
		this.fieldName = fieldName;
		this.target = target;
		this.field = field;
		this.getterMethod = getterMethod;
		this.setterMethod = setterMethod;
		if (logger.isDebugEnabled())
		{
			logger.debug("field: {}, {}, setter: {}, getter: {}, annotations: {}", fieldName,
					field != null, setterMethod != null ? setterMethod.getName() : null,
					getterMethod != null ? getterMethod.getName() : null,
					this.getAnnotations());
		}
	}

	@Override
	public String getName()
	{
		return fieldName;
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

		Object value = getterMethod != null ? getterMethod.invoke() : null;
		if (value == null && field != null)
		{
			try
			{
				if (!field.canAccess(target))
				{
					field.setAccessible(true);
				}
				value = field.get(target);
			}
			catch (Exception e)
			{
				logger.error("field get error,", e);
			}
		}
		return value;
	}

	@Override
	public boolean setFieldValue(Object value)
	{
		if (target == null || value == null)
		{
			logger.warn("bean or value is null");
			return false;
		}

		boolean flag = false;
		if (setterMethod != null)
		{
			setterMethod.invoke(value);
			flag = true;
		}
		else if (field != null)
		{
			try
			{
				if (!field.canAccess(target))
				{
					field.setAccessible(true);
				}
				field.set(target, value);
				flag = true;
			}
			catch (Exception e)
			{
				logger.error("field set error,", e);
				flag = false;
			}
		}
		return flag;
	}

	@Override
	public BeanMethod getGetterMethod()
	{
		return getterMethod;
	}

	@Override
	public BeanMethod getSetterMethod()
	{
		return setterMethod;
	}

	@Override
	public <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass)
	{
		T annotation = null;
		if (setterMethod == null)
		{
			annotation = setterMethod.getDeclaredAnnotation(annotationClass);
		}
		if (annotation == null && field != null)
		{
			annotation = field.getDeclaredAnnotation(annotationClass);
		}
		return annotation;
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

	@Override
	public List<Annotation> getDeclaredAnnotations()
	{
		Map<Class<?>, Annotation> map = new HashMap<>();
		if (setterMethod != null)
		{
			List<Annotation> list = setterMethod.getDeclaredAnnotations();
			this.mergeAnnotationList(map, list);
		}
		if (field != null)
		{
			List<Annotation> list = Arrays.asList(field.getDeclaredAnnotations());
			this.mergeAnnotationList(map, list);
		}
		return List.copyOf(map.values());
	}

	private void mergeAnnotationList(Map<Class<?>, Annotation> map, List<Annotation> list)
	{
		for (var annotation : list)
		{
			if (map.containsKey(annotation.annotationType()))
				continue;

			map.put(annotation.annotationType(), annotation);
		}
	}

	@Override
	public List<Annotation> getAnnotations()
	{
		Map<Class<?>, Annotation> map = new HashMap<>();
		if (setterMethod != null)
		{
			List<Annotation> list = setterMethod.getAnnotations();
			this.mergeAnnotationList(map, list);
		}
		if (field != null)
		{
			List<Annotation> list = Arrays.asList(field.getAnnotations());
			this.mergeAnnotationList(map, list);
		}
		return List.copyOf(map.values());
	}
}
