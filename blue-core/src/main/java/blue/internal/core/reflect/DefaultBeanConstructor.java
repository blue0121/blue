package blue.internal.core.reflect;

import blue.core.reflect.BeanConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2021-02-20
 */
public class DefaultBeanConstructor extends DefaultExecutableOperation implements BeanConstructor
{
	private static Logger logger = LoggerFactory.getLogger(DefaultBeanConstructor.class);

	private final Constructor<?> constructor;

	public DefaultBeanConstructor(Constructor<?> constructor, List<Class<?>> superClassList)
	{
		super(constructor, superClassList, null);
		this.constructor = constructor;
		if (logger.isDebugEnabled())
		{
			logger.debug("constructor, params: {}, annotation: {}", this.getParamClassList(), this.getAnnotations());
		}
	}

	@Override
	protected Executable findExecutable(Executable src, Class<?> clazz)
	{
		if (clazz.isInterface())
			return null;

		for (var constructor : clazz.getConstructors())
		{
			if (Arrays.equals(src.getParameterTypes(), constructor.getParameterTypes()))
				return constructor;
		}
		return null;
	}

	@Override
	public Object newInstance(Object...params) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException
	{
		if (!constructor.canAccess(null))
		{
			constructor.setAccessible(true);
		}
		return constructor.newInstance(params);
	}

	@Override
	public Object newInstanceQuietly(Object...params)
	{
		Object value = null;
		try
		{
			value = this.newInstance(params);
		}
		catch (Exception e)
		{
			logger.error("New instance error,", e);
		}
		return value;
	}

}
