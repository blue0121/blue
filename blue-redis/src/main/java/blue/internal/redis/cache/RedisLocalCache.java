package blue.internal.redis.cache;

import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.AbstractValueAdaptingCache;

import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;

/**
 * @author Jin Zheng
 * @since 2020-12-06
 */
public class RedisLocalCache extends AbstractValueAdaptingCache
{
	private static Logger logger = LoggerFactory.getLogger(RedisLocalCache.class);

	private final RLocalCachedMap<Object, Object> cache;

	public RedisLocalCache(RLocalCachedMap<Object, Object> cache, boolean allowNullValues)
	{
		super(allowNullValues);
		this.cache = cache;
	}

	@Override
	protected Object lookup(Object key)
	{
		return cache.get(key);
	}

	@Override
	public String getName()
	{
		return cache.getName();
	}

	@Override
	public Object getNativeCache()
	{
		return cache;
	}

	@Override
	public <T> T get(Object key, Callable<T> valueLoader)
	{
		Object value = cache.get(key);
		if (value == null)
		{
			RLock lock = cache.getLock(key);
			lock.lock();
			try
			{
				value = cache.get(key);
				if (value == null)
				{
					value = putValue(key, valueLoader, value);
				}
			}
			finally
			{
				lock.unlock();
			}
		}
		return (T) fromStoreValue(value);
	}

	@Override
	public void put(Object key, Object value)
	{
		if (!this.isAllowNullValues() && value == null)
		{
			cache.remove(key);
			return;
		}

		value = toStoreValue(value);
		cache.fastPut(key, value);
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value)
	{
		Object prevValue;
		if (!this.isAllowNullValues() && value == null)
		{
			prevValue = cache.get(key);
		}
		else
		{
			prevValue = cache.putIfAbsent(key, this.toStoreValue(value));
		}
		return toValueWrapper(prevValue);
	}

	private <T> Object putValue(Object key, Callable<T> valueLoader, Object value)
	{
		try
		{
			value = valueLoader.call();
		}
		catch (Exception ex)
		{
			RuntimeException exception;
			try
			{
				Class<?> c = Class.forName("org.springframework.cache.Cache$ValueRetrievalException");
				Constructor<?> constructor = c.getConstructor(Object.class, Callable.class, Throwable.class);
				exception = (RuntimeException) constructor.newInstance(key, valueLoader, ex);
			}
			catch (Exception e)
			{
				throw new IllegalStateException(e);
			}
			throw exception;
		}
		this.put(key, value);
		return value;
	}

	@Override
	public void evict(Object key)
	{
		cache.fastRemove(key);
	}

	@Override
	public void clear()
	{
		cache.clear();
	}
}
