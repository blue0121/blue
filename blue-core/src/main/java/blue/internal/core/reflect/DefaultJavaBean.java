package blue.internal.core.reflect;

import blue.core.reflect.BeanField;
import blue.core.reflect.JavaBean;
import blue.core.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-24
 */
public class DefaultJavaBean implements JavaBean
{
	private static Logger logger = LoggerFactory.getLogger(DefaultJavaBean.class);

	private final Object target;
	private final Class<?> targetClass;
	private final Map<String, BeanField> beanFieldMap = new HashMap<>();

	public DefaultJavaBean(Object target, Class<?> targetClass)
	{
		this.target = target;
		this.targetClass = targetClass;
		this.parse();
	}

	@Override
	public Class<?> getTargetClass()
	{
		return targetClass;
	}

	@Override
	public Object getTarget()
	{
		return target;
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass)
	{
		return targetClass.getAnnotation(annotationClass);
	}

	@Override
	public Map<String, BeanField> getAllFields()
	{
		return Map.copyOf(beanFieldMap);
	}

	@Override
	public BeanField getField(String fieldName)
	{
		return beanFieldMap.get(fieldName);
	}

	private void parse()
	{
		Set<String> fieldNameSet = new HashSet<>();
		Map<String, Field> fieldMap = new HashMap<>();
		Map<String, Method> getterMap = new HashMap<>();
		Map<String, Method> setterMap = new HashMap<>();
		Class<?> clazz = this.getTargetClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields)
		{
			int mod = field.getModifiers();
			if (Modifier.isFinal(mod) || Modifier.isStatic(mod))
				continue;

			fieldMap.put(field.getName(), field);
			fieldNameSet.add(field.getName());
		}

		Method[] methods = clazz.getMethods();
		for (Method method : methods)
		{
			String methodName = method.getName();
			String fieldName = ReflectionUtil.field(methodName);
			if (fieldName == null || fieldName.isEmpty())
				continue;

			if (methodName.startsWith("set") && method.getParameterTypes().length == 1)
			{
				setterMap.put(fieldName, method);
				fieldNameSet.add(fieldName);
				continue;
			}
			if (methodName.equals("getClass") || method.getParameterTypes().length != 0)
			{
				continue;
			}
			Class<?> returnClazz = method.getReturnType();
			if (methodName.startsWith("is") && (returnClazz == Boolean.class || returnClazz == boolean.class))
			{
				getterMap.put(fieldName, method);
				fieldNameSet.add(fieldName);
			}
			else if (methodName.startsWith("get"))
			{
				getterMap.put(fieldName, method);
				fieldNameSet.add(fieldName);
			}
		}
		for (String fieldName : fieldNameSet)
		{
			BeanField beanField = new DefaultBeanField(fieldName, target, fieldMap.get(fieldName),
					getterMap.get(fieldName), setterMap.get(fieldName));
			beanFieldMap.put(fieldName, beanField);
		}
		if (logger.isDebugEnabled() && !fieldNameSet.isEmpty())
		{
			logger.debug("{} fields: {}", clazz.getSimpleName(), fieldNameSet);
		}
	}

}
