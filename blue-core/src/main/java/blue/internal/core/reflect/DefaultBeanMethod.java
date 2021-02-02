package blue.internal.core.reflect;

import blue.core.reflect.BeanMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2021-02-02
 */
public class DefaultBeanMethod implements BeanMethod
{
	private static Logger logger = LoggerFactory.getLogger(DefaultBeanMethod.class);

	private final static String SET_PREFIX = "set";
	private final static String GET_PREFIX = "get";
	private final static String IS_PREFIX = "is";

	private final Object target;
	private final Method method;
	private final List<Class<?>> superClassList;
	private final List<Class<?>> interfaceList;
	private final List<Class<?>> paramClassList = new ArrayList<>();

	private boolean setter = false;
	private boolean getter = false;
	private String representField;

	private Map<Class<?>, Annotation> annotationMap;
	private List<Annotation> annotationList;

	public DefaultBeanMethod(Object target, Method method,
	                         List<Class<?>> superClassList, List<Class<?>> interfaceList)
	{
		this.target = target;
		this.method = method;
		this.superClassList = superClassList;
		this.interfaceList = interfaceList;
		this.parse();
		this.parseAnnotation();
		if (logger.isDebugEnabled())
		{
			logger.debug("method: {}, setter: {}, getter: {}, representField: {}, annotation: {}",
					method.getName(), setter, getter, representField, annotationList);
		}
	}

	private void parse()
	{
		var paramClasses = method.getParameterTypes();
		for (var paramClass : paramClasses)
		{
			paramClassList.add(paramClass);
		}
		String field = this.fieldName();
		if (field != null)
		{
			if (paramClasses.length == 0)
			{
				this.getter = true;
				this.representField = field;
			}
			else if (paramClasses.length == 1)
			{
				this.setter = true;
				this.representField = field;
			}
		}
	}

	private void parseAnnotation()
	{
		Map<Class<?>, Annotation> annoMap = new HashMap<>();
		List<Annotation> annoList = new ArrayList<>();
		this.parseAnnotation(method, annoMap, annoList);

		for (var clazz : superClassList)
		{
			Method dest = this.findMethod(method, clazz);
			if (dest == null)
				continue;

			this.parseAnnotation(dest, annoMap, annoList);
		}
		for (var inter : interfaceList)
		{
			Method dest = this.findMethod(method, inter);
			if (dest == null)
				continue;

			this.parseAnnotation(dest, annoMap, annoList);
		}

		this.annotationMap = Map.copyOf(annoMap);
		this.annotationList = List.copyOf(annoList);
	}

	private void parseAnnotation(Method method, Map<Class<?>, Annotation> annoMap, List<Annotation> annoList)
	{
		Annotation[] annos = method.getDeclaredAnnotations();
		for (var anno : annos)
		{
			annoMap.put(anno.annotationType(), anno);
			annoList.add(anno);
		}
	}

	private Method findMethod(Method src, Class<?> clazz)
	{
		for (var method : clazz.getMethods())
		{
			if (src.getName().equals(method.getName())
				&& Arrays.equals(src.getParameterTypes(), method.getParameterTypes()))
				return method;
		}
		return null;
	}

	@Override
	public <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass)
	{
		return method.getDeclaredAnnotation(annotationClass);
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
		Annotation[] annotations = method.getDeclaredAnnotations();
		return Arrays.asList(annotations);
	}

	@Override
	public List<Annotation> getAnnotations()
	{
		return annotationList;
	}

	@Override
	public String getMethodName()
	{
		return method.getName();
	}

	@Override
	public List<Class<?>> getParamClassList()
	{
		return List.copyOf(paramClassList);
	}

	@Override
	public Class<?> getReturnClass()
	{
		return method.getReturnType();
	}

	@Override
	public int getModifiers()
	{
		return method.getModifiers();
	}

	@Override
	public Object invoke(Object... params)
	{
		if (target == null)
		{
			logger.warn("bean is null");
			return null;
		}

		Object value = null;
		try
		{
			value = method.invoke(target, params);
		}
		catch (Exception e)
		{
			logger.error("Invoke method error,", e);
		}
		return value;
	}

	@Override
	public String getRepresentField()
	{
		return representField;
	}

	@Override
	public boolean isSetter()
	{
		return setter;
	}

	@Override
	public boolean isGetter()
	{
		return getter;
	}

	private String fieldName()
	{
		String methodName = method.getName();
		String name = null;
		if (methodName.startsWith(IS_PREFIX))
		{
			Class<?> returnType = method.getReturnType();
			if (returnType == Boolean.class || returnType == boolean.class)
			{
				name = methodName.substring(2);
			}
		}
		else if (methodName.startsWith(SET_PREFIX) || methodName.startsWith(GET_PREFIX))
		{
			name = methodName.substring(3);
		}
		if (name == null || name.isEmpty())
			return null;

		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}
}
