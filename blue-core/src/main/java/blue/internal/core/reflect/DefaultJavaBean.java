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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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
	private final List<Class<?>> superClassList = new ArrayList<>();
	private final List<Class<?>> interfaceList = new ArrayList<>();
	private final List<Class<?>> superList = new ArrayList<>();
	private final Map<Class<?>, Map<Class<?>, Annotation>> annotationMapMap = new HashMap<>();
	private final Map<Class<?>, List<Annotation>> annotationListMap = new HashMap<>();
	private final Map<String, BeanField> beanFieldMap = new HashMap<>();

	public DefaultJavaBean(Class<?> targetClass)
	{
		this(null, targetClass);
	}

	public DefaultJavaBean(Object target, Class<?> targetClass)
	{
		this.target = target;
		this.targetClass = targetClass;
		this.parseClass();
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

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass)
	{
		Map<Class<?>, Annotation> annotationMap = annotationMapMap.get(targetClass);
		if (annotationMap == null || annotationMap.isEmpty())
			return null;

		return (T) annotationMap.get(annotationClass);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass)
	{
		for (var clazz : superList)
		{
			Map<Class<?>, Annotation> map = annotationMapMap.get(clazz);
			Annotation annotation = map.get(annotationClass);
			if (annotation != null)
				return (T) annotation;
		}
		return null;
	}

	@Override
	public List<Annotation> getDeclaredAnnotations()
	{
		List<Annotation> annotationList = annotationListMap.get(targetClass);
		if (annotationList == null)
			return List.of();

		return annotationList;
	}

	@Override
	public List<Annotation> getAnnotations()
	{
		Map<Class<?>, Annotation> annotationMap = new HashMap<>();
		for (var clazz : superList)
		{
			Map<Class<?>, Annotation> map = annotationMapMap.get(clazz);
			for (var entry : map.entrySet())
			{
				if (!annotationMap.containsKey(entry.getKey()))
				{
					annotationMap.put(entry.getKey(), entry.getValue());
				}
			}
		}
		return List.copyOf(annotationMap.values());
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

	private void parseClass()
	{
		Queue<Class<?>> queue = new LinkedList<>();
		List<Class<?>> clazzList = new ArrayList<>();
		queue.offer(targetClass.getSuperclass());
		this.parseAnnotation(targetClass);
		Class<?> clazz = null;
		while ((clazz = queue.poll()) != null)
		{
			if (clazz == Object.class)
				continue;

			superClassList.add(clazz);
			superList.add(clazz);
			clazzList.add(clazz);
			this.parseAnnotation(clazz);
			queue.offer(clazz.getSuperclass());
		}
		for (var cls : clazzList)
		{
			for (var inter : cls.getInterfaces())
			{
				interfaceList.add(inter);
				superList.add(inter);
				this.parseAnnotation(inter);
			}
		}
	}

	private void parseAnnotation(Class<?> clazz)
	{
		List<Annotation> annotationList = Arrays.asList(clazz.getDeclaredAnnotations());
		annotationListMap.put(clazz, List.copyOf(annotationList));
		Map<Class<?>, Annotation> annotationMap = new HashMap<>();
		annotationList.forEach(e -> annotationMap.put(e.annotationType(), e));
		annotationMapMap.put(clazz, Map.copyOf(annotationMap));
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
