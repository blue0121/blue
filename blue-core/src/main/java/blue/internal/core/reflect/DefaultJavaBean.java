package blue.internal.core.reflect;

import blue.core.reflect.BeanField;
import blue.core.reflect.BeanMethod;
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
	private static final Set<String> METHOD_SET = Set.of("wait", "equals", "toString", "hashCode", "getClass",
			"notify", "notifyAll");

	private final Object target;
	private final Class<?> targetClass;

	private List<Class<?>> superClassList;
	private List<Class<?>> interfaceList;

	private Map<Class<?>, Annotation> annotationMap;
	private List<Annotation> annotationList;

	private Map<String, BeanMethod> getterMap;
	private Map<String, BeanMethod> setterMap;
	private List<BeanMethod> allMethodList;
	private List<BeanMethod> otherMethodList;

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
		if (logger.isDebugEnabled())
		{
			logger.debug("super classes: {}, interfaces: {}, annotations: {}",
					superClassList, interfaceList, annotationList);
		}
		this.parseMethod();
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
	public List<BeanMethod> getAllMethods()
	{
		return allMethodList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass)
	{
		return targetClass.getDeclaredAnnotation(annotationClass);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass)
	{
		return (T) annotationMap.get(annotationClass);
	}

	@Override
	public List<Annotation> getDeclaredAnnotations()
	{
		Annotation[] annotations = targetClass.getDeclaredAnnotations();
		return Arrays.asList(annotations);
	}

	@Override
	public List<Annotation> getAnnotations()
	{
		return annotationList;
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
		List<Class<?>> superList = new ArrayList<>();
		List<Class<?>> interList = new ArrayList<>();
		Map<Class<?>, Annotation> annoMap = new HashMap<>();
		List<Annotation> annoList = new ArrayList<>();
		this.parseSuperClass(targetClass, superList, interList, annoMap, annoList);
		for (var cls : superList)
		{
			for (var inter : cls.getInterfaces())
			{
				this.parseSuperClass(inter, superList, interList, annoMap, annoList);
			}
		}
		this.superClassList = List.copyOf(superList);
		this.interfaceList = List.copyOf(interList);
		this.annotationMap = Map.copyOf(annoMap);
		this.annotationList = List.copyOf(annoList);
	}

	private void parseSuperClass(Class<?> cls, List<Class<?>> superList, List<Class<?>> interList,
	                             Map<Class<?>, Annotation> annoMap, List<Annotation> annoList)
	{
		Queue<Class<?>> queue = new LinkedList<>();
		queue.offer(cls);
		Class<?> clazz = null;
		while ((clazz = queue.poll()) != null)
		{
			if (clazz == Object.class)
				continue;

			if (clazz.isInterface())
			{
				interList.add(clazz);
			}
			else
			{
				superList.add(clazz);
			}
			this.parseAnnotation(clazz, annoMap, annoList);
			queue.offer(clazz.getSuperclass());
		}
	}

	private void parseAnnotation(Class<?> clazz, Map<Class<?>, Annotation> annoMap, List<Annotation> annoList)
	{
		Annotation[] annotations = clazz.getDeclaredAnnotations();
		for (var annotation : annotations)
		{
			if (annoMap.containsKey(annotation.annotationType()))
				continue;

			annoMap.put(annotation.annotationType(), annotation);
			annoList.add(annotation);
		}
	}

	private void parseMethod()
	{
		Map<String, BeanMethod> getter = new HashMap<>();
		Map<String, BeanMethod> setter = new HashMap<>();
		List<BeanMethod> all = new ArrayList<>();
		List<BeanMethod> other = new ArrayList<>();
		Method[] methods = targetClass.getMethods();
		for (var method : methods)
		{
			int mod = method.getModifiers();
			if (Modifier.isStatic(mod) || METHOD_SET.contains(method.getName()))
				continue;

			BeanMethod beanMethod = new DefaultBeanMethod(target, method, superClassList, interfaceList);
			all.add(beanMethod);
			if (beanMethod.isGetter())
			{
				getter.put(beanMethod.getRepresentField(), beanMethod);
			}
			else if (beanMethod.isSetter())
			{
				setter.put(beanMethod.getRepresentField(), beanMethod);
			}
			else
			{
				other.add(beanMethod);
			}
		}
		this.getterMap = Map.copyOf(getter);
		this.setterMap = Map.copyOf(setter);
		this.allMethodList = List.copyOf(all);
		this.otherMethodList = List.copyOf(other);
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
