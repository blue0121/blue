package blue.core.common;

import blue.core.util.AssertUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 单例对象池
 *
 * @author Jin Zheng
 * @since 1.0 2021-01-22
 */
public class Singleton
{
	private static final ConcurrentMap<String, Object> POOL = new ConcurrentHashMap<>();

	private Singleton()
	{
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(String key)
	{
		AssertUtil.notEmpty(key, "Key");
		return (T)POOL.get(key);
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> clazz, Object...params)
	{
		AssertUtil.notNull(clazz, "Class");
		String key = buildKey(clazz, params);
		return (T)POOL.computeIfAbsent(key, k -> newInstance(clazz, params));
	}

	private static <T> T newInstance(Class<T> clazz, Object...params)
	{
		try
		{
			return clazz.getConstructor().newInstance();
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException(e);
		}
	}

	private static <T> String buildKey(Class<T> clazz, Object...params)
	{
		StringBuilder sb = new StringBuilder(64);
		sb.append(clazz.getName());
		return sb.toString();
	}

	public static void put(String key, Object object)
	{
		AssertUtil.notEmpty(key, "Key");
		AssertUtil.notNull(object, "Object");
		POOL.put(key, object);
	}

	public static void put(Object object)
	{
		AssertUtil.notNull(object, "Object");
		POOL.put(object.getClass().getName(), object);
	}

	public static void remove(String key)
	{
		AssertUtil.notEmpty(key, "Key");
		POOL.remove(key);
	}

	public static void remove(Object object)
	{
		AssertUtil.notNull(object, "Object");
		if (object instanceof Class)
		{
			POOL.remove(((Class)object).getName());
		}
		else
		{
			POOL.remove(object.getClass().getName());
		}
	}

	public static void clear()
	{
		POOL.clear();
	}

}
