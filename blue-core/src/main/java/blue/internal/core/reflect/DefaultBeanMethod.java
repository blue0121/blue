package blue.internal.core.reflect;

import blue.core.reflect.BeanMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2021-02-02
 */
public class DefaultBeanMethod extends DefaultExecutableOperation implements BeanMethod
{
	private static Logger logger = LoggerFactory.getLogger(DefaultBeanMethod.class);

	private final static String SET_PREFIX = "set";
	private final static String GET_PREFIX = "get";
	private final static String IS_PREFIX = "is";

	private final Object target;
	private final Method method;

	private boolean setter = false;
	private boolean getter = false;
	private String representField;


	public DefaultBeanMethod(Object target, Method method,
	                         List<Class<?>> superClassList, List<Class<?>> interfaceList)
	{
		super(method, superClassList, interfaceList);
		this.target = target;
		this.method = method;
		this.parseRepresentField();
		if (logger.isDebugEnabled())
		{
			logger.debug("method: {}, setter: {}, getter: {}, representField: {}, annotation: {}",
					this.getName(), setter, getter, representField, this.getAnnotations());
		}
	}

	private void parseRepresentField()
	{
		String field = this.fieldName();
		if (field != null && !field.isEmpty())
		{
			int paramSize = this.getParamClassList().size();
			if (paramSize == 0)
			{
				this.getter = true;
				this.representField = field;
			}
			else if (paramSize == 1)
			{
				this.setter = true;
				this.representField = field;
			}
		}
	}

	@Override
	protected Executable findExecutable(Executable src, Class<?> clazz)
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
	public Class<?> getReturnClass()
	{
		return method.getReturnType();
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
		String methodName = this.getName();
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
