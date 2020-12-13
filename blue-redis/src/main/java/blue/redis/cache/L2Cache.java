package blue.redis.cache;

import blue.internal.redis.cache.DefaultL2Cache;
import org.redisson.api.RedissonClient;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Jin Zheng
 * @since 1.0 2020-12-08
 */
public interface L2Cache
{
	long DEFAULT_LOCAL_TTL = -1;

	static L2Cache create(RedissonClient redisson, String keyPrefix, CacheConfig cacheConfig)
	{
		DefaultL2Cache cache = new DefaultL2Cache();
		cache.setRedisson(redisson);
		cache.setKeyPrefix(keyPrefix);
		cache.setTtl(cacheConfig.ttl());
		cache.setLocalTtl(cacheConfig.localTtl());
		cache.setLocalMaxSize(cacheConfig.localMaxSize());
		cache.setTimeout(cacheConfig.timeout());
		cache.afterPropertiesSet();
		return cache;
	}

	/**
	 * key prefix
	 *
	 * @return
	 */
	String keyPrefix();

	long ttl();

	long localTtl();

	long localMaxSize();

	long timeout();

	/**
	 * get value by name
	 *
	 * @param name
	 * @param <T>
	 * @return
	 */
	<T> T getSync(String name);

	<T> Map<String, T> getSync(String...names);

	<T> void getAsync(Consumer<Map.Entry<String, T>> f, String name);

	<T> void getAsync(Consumer<Map<String, T>> f, String...names);

	/**
	 * set value with name asynchronously
	 *
	 * @param name
	 * @param value
	 */
	default void setAsync(String name, Object value)
	{
		this.setAsync(name, value, this.ttl(), DEFAULT_LOCAL_TTL);
	}

	default void setAsync(String name, Object value, long ttl)
	{
		this.setAsync(name, value, ttl, DEFAULT_LOCAL_TTL);
	}

	default void setAsync(String name, Object value, long ttl, long localTtl)
	{
		this.setAsync(List.of(name), List.of(value), ttl, localTtl);
	}

	default void setAsync(List<String> nameList, List<?> valueList)
	{
		this.setAsync(nameList, valueList, this.ttl(), DEFAULT_LOCAL_TTL);
	}

	default void setAsync(List<String> nameList, List<?> valueList, long ttl)
	{
		this.setAsync(nameList, valueList, ttl, DEFAULT_LOCAL_TTL);
	}

	void setAsync(List<String> nameList, List<?> valueList, long ttl, long localTtl);

	default void setSync(String name, Object value)
	{
		this.setSync(name, value, this.ttl(), DEFAULT_LOCAL_TTL);
	}

	default void setSync(String name, Object value, long ttl)
	{
		this.setSync(name, value, ttl, DEFAULT_LOCAL_TTL);
	}

	default void setSync(String name, Object value, long ttl, long localTtl)
	{
		this.setSync(List.of(name), List.of(value), ttl, localTtl);
	}

	default void setSync(List<String> nameList, List<?> valueList)
	{
		this.setSync(nameList, valueList, this.ttl(), DEFAULT_LOCAL_TTL);
	}

	default void setSync(List<String> nameList, List<?> valueList, long ttl)
	{
		this.setSync(nameList, valueList, ttl, DEFAULT_LOCAL_TTL);
	}

	void setSync(List<String> nameList, List<?> valueList, long ttl, long localTtl);

	/**
	 * remove
	 *
	 * @param names
	 */
	void removeAsync(String...names);

	void removeSync(String...names);

	/**
	 * clear
	 */
	void clearAsync();

	void clearSync();

}
