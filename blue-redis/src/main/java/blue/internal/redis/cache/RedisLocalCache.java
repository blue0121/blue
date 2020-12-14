package blue.internal.redis.cache;

import blue.redis.cache.L2Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractValueAdaptingCache;

import java.util.concurrent.Callable;

/**
 * @author Jin Zheng
 * @since 2020-12-06
 */
public class RedisLocalCache extends AbstractValueAdaptingCache
{
	private static Logger logger = LoggerFactory.getLogger(RedisLocalCache.class);

	private final L2Cache cache;
	private final String name;

	public RedisLocalCache(L2Cache cache, String name, boolean allowNullValues)
	{
		super(allowNullValues);
		this.cache = cache;
		this.name = name;
	}

	@Override
	protected Object lookup(Object key)
	{
		return cache.getSync(key.toString());
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public Object getNativeCache()
	{
		return cache;
	}

	@Override
	public <T> T get(Object key, Callable<T> valueLoader)
	{
		Object value = cache.getSync(key.toString());
		if (value == null)
		{
			value = putValue(key, valueLoader);
		}
		return (T) fromStoreValue(value);
	}

	@Override
	public void put(Object key, Object value)
	{
		if (!this.isAllowNullValues() && value == null)
		{
			cache.removeSync(key.toString());
			return;
		}

		Object newValue = toStoreValue(value);
		cache.setSync(key.toString(), newValue);
	}

	private <T> Object putValue(Object key, Callable<T> valueLoader)
	{
		Object value = null;
		try
		{
			value = valueLoader.call();
		}
		catch (Exception ex)
		{
			return new Cache.ValueRetrievalException(key, valueLoader, ex);
		}
		this.put(key, value);
		return value;
	}

	@Override
	public void evict(Object key)
	{
		cache.removeSync(key.toString());
	}

	@Override
	public void clear()
	{
		cache.clearSync();
	}
}
