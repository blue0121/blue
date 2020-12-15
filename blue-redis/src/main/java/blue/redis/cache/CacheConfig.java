package blue.redis.cache;

import blue.internal.redis.cache.DefaultCacheConfig;

/**
 * @author Jin Zheng
 * @since 1.0 2020-12-08
 */
public interface CacheConfig
{

	static CacheConfig create(long ttl, long localTtl, long localMaxSize, long timeout)
	{
		DefaultCacheConfig config = new DefaultCacheConfig();
		config.setTtl(ttl);
		config.setLocalTtl(localTtl);
		config.setLocalMaxSize(localMaxSize);
		config.setTimeout(timeout);
		return config;
	}

	long ttl();

	long localTtl();

	long localMaxSize();

	long timeout();

}
