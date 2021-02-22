package blue.internal.core.reflect;

import blue.core.reflect.BeanField;
import blue.core.reflect.BeanMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-24
 */
public class DefaultBeanField extends DefaultAnnotationOperation implements BeanField
{
	private static Logger logger = LoggerFactory.getLogger(DefaultBeanField.class);

	private final String fieldName;
	private final Object target;
	private final Field field;
	private final BeanMethod getterMethod;
	private final BeanMethod setterMethod;

	private Map<Class<?>, Annotation> getterAnnotationMap;
	private List<Annotation> getterAnnotationList;
	private Map<Class<?>, Annotation> setterAnnotationMap;
	private List<Annotation> setterAnnotationList;

	public DefaultBeanField(String fieldName, Object target, Field field,
	                        BeanMethod getterMethod, BeanMethod setterMethod)
	{
		super(field);
		this.fieldName = fieldName;
		this.target = target;
		this.field = field;
		this.getterMethod = getterMethod;
		this.setterMethod = setterMethod;
		this.parseAnnotation();
		if (logger.isDebugEnabled())
		{
			logger.debug("field: {}, {}, setter: {}, getter: {}, field annotations: {}," +
						" getter annotations: {}, setter annotations: {}",
					fieldName, field != null, setterMethod != null ? setterMethod.getName() : null,
					getterMethod != null ? getterMethod.getName() : null,
					this.getAnnotations(), getterAnnotationList, setterAnnotationList);
		}
	}

	private void parseAnnotation()
	{
		Map<Class<?>, Annotation> annotationMap = new HashMap<>();
		if (field != null)
		{
			Annotation[] annotations = field.getAnnotations();
			for (Annotation annotation : annotations)
			{
				annotationMap.put(annotation.annotationType(), annotation);
			}
		}
		this.initAnnotationMap(annotationMap);

		var getterMap = this.parseAnnotationWithMethod(annotationMap, getterMethod);
		this.getterAnnotationMap = Map.copyOf(getterMap);
		this.getterAnnotationList = List.copyOf(getterMap.values());

		var setterMap = this.parseAnnotationWithMethod(annotationMap, setterMethod);
		this.setterAnnotationMap = Map.copyOf(setterMap);
		this.setterAnnotationList = List.copyOf(setterMap.values());
	}

	private Map<Class<?>, Annotation> parseAnnotationWithMethod(Map<Class<?>, Annotation> annotationMap,
	                                                            BeanMethod method)
	{
		Map<Class<?>, Annotation> methodAnnotationMap = new HashMap<>();
		if (method != null)
		{
			method.getAnnotations().forEach(e -> methodAnnotationMap.put(e.annotationType(), e));
		}
		for (var entry : annotationMap.entrySet())
		{
			if (methodAnnotationMap.containsKey(entry.getKey()))
				continue;

			methodAnnotationMap.put(entry.getKey(), entry.getValue());
		}
		return methodAnnotationMap;
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

		Object value = getterMethod != null ? getterMethod.invokeQuietly() : null;
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
			setterMethod.invokeQuietly(value);
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

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Annotation> T getGetterAnnotation(Class<T> annotationClass)
	{
		return (T) getterAnnotationMap.get(annotationClass);
	}

	@Override
	public List<Annotation> getGetterAnnotations()
	{
		return getterAnnotationList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Annotation> T getSetterAnnotation(Class<T> annotationClass)
	{
		return (T) setterAnnotationMap.get(annotationClass);
	}

	@Override
	public List<Annotation> getSetterAnnotations()
	{
		return setterAnnotationList;
	}

}
