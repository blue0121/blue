package blue.internal.core.reflect;

import blue.core.reflect.BeanMethod;
import blue.core.reflect.MethodParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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

	private List<Class<?>> paramClassList;
	private List<Method> superMethodList;

	private boolean setter = false;
	private boolean getter = false;
	private String representField;

	private Map<Class<?>, Annotation> annotationMap;
	private List<Annotation> annotationList;
	private List<MethodParam> methodParamList;

	public DefaultBeanMethod(Object target, Method method,
	                         List<Class<?>> superClassList, List<Class<?>> interfaceList)
	{
		this.target = target;
		this.method = method;
		this.superClassList = superClassList;
		this.interfaceList = interfaceList;
		this.parse();
		this.parseAnnotation();
		this.parseParam();
		if (logger.isDebugEnabled())
		{
			logger.debug("method: {}, setter: {}, getter: {}, representField: {}, annotation: {}",
					this.getName(), setter, getter, representField, annotationList);
		}
	}

	private void parse()
	{
		var paramClasses = method.getParameterTypes();
		List<Class<?>> paramList = new ArrayList<>();
		for (var paramClass : paramClasses)
		{
			paramList.add(paramClass);
		}
		this.paramClassList = List.copyOf(paramList);

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
		List<Method> methodList = new ArrayList<>();
		Map<Class<?>, Annotation> annoMap = new HashMap<>();
		List<Annotation> annoList = new ArrayList<>();
		this.parseAnnotation(method, methodList, annoMap, annoList);

		for (var clazz : superClassList)
		{
			Method dest = this.findMethod(method, clazz);
			if (dest == null)
				continue;

			this.parseAnnotation(dest, methodList, annoMap, annoList);
		}
		for (var inter : interfaceList)
		{
			Method dest = this.findMethod(method, inter);
			if (dest == null)
				continue;

			this.parseAnnotation(dest, methodList, annoMap, annoList);
		}

		this.superMethodList = List.copyOf(methodList);
		this.annotationMap = Map.copyOf(annoMap);
		this.annotationList = List.copyOf(annoList);
	}

	private void parseAnnotation(Method method, List<Method> methodList,
	                             Map<Class<?>, Annotation> annoMap, List<Annotation> annoList)
	{
		methodList.add(method);
		Annotation[] annos = method.getDeclaredAnnotations();
		for (var anno : annos)
		{
			if (annoMap.containsKey(anno.annotationType()))
				continue;

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

	private void parseParam()
	{
		int count = method.getParameterCount();
		List<MethodParam> paramList = new ArrayList<>();
		for (int i = 0; i < count; i++)
		{
			List<Parameter> list = new ArrayList<>();
			for (var superMethod : superMethodList)
			{
				var params = superMethod.getParameters();
				list.add(params[i]);
			}
			MethodParam param = new DefaultMethodParam(list);
			paramList.add(param);
		}
		this.methodParamList = List.copyOf(paramList);
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
	public String getName()
	{
		return method.getName();
	}

	@Override
	public List<Class<?>> getParamClassList()
	{
		return paramClassList;
	}

	@Override
	public List<MethodParam> getParamList()
	{
		return methodParamList;
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
	public Object invoke(Object...params) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException
	{
		if (target == null)
		{
			logger.warn("bean is null");
			return null;
		}

		return method.invoke(target, params);
	}

	@Override
	public Object invokeQuietly(Object...params)
	{
		Object value = null;
		try
		{
			value = this.invoke(params);
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
