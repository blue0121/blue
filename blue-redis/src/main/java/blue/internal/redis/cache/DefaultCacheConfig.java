package blue.internal.redis.cache;

import blue.redis.cache.CacheConfig;

/**
 * @author Jin Zheng
 * @since 2020-12-05
 */
public class DefaultCacheConfig implements CacheConfig
{
	private String name;
	private long ttl = 10 * 60 * 1000;
	private long localTtl = 50 * 1000;
	private long localMaxSize = 1000;
	private long timeout = 2 * 1000;

	public DefaultCacheConfig()
	{
		this.name = "default";
	}

	public DefaultCacheConfig copy()
	{
		DefaultCacheConfig config = new DefaultCacheConfig();
		config.name = this.name;
		config.ttl = this.ttl;
		config.localTtl = this.localTtl;
		config.localMaxSize = this.localMaxSize;
		config.timeout = this.timeout;
		return config;
	}

	@Override
	public String toString()
	{
		return String.format("%s{name: %s, ttl: %dms, localTtl: %dms, localMaxSize: %dms, timeout: %dms}",
				this.getClass().getSimpleName(), name, ttl, localTtl, localMaxSize, timeout);
	}

	public String name()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public long ttl()
	{
		return ttl;
	}

	public void setTtl(long ttl)
	{
		if (ttl > 0)
		{
			this.ttl = ttl;
		}
	}

	@Override
	public long localTtl()
	{
		return localTtl;
	}

	public void setLocalTtl(long localTtl)
	{
		if (localTtl > 0)
		{
			this.localTtl = localTtl;
		}
	}

	@Override
	public long localMaxSize()
	{
		return localMaxSize;
	}

	public void setLocalMaxSize(long localMaxSize)
	{
		if (localMaxSize > 0)
		{
			this.localMaxSize = localMaxSize;
		}
	}

	@Override
	public long timeout()
	{
		return timeout;
	}

	public void setTimeout(long timeout)
	{
		if (timeout > 0)
		{
			this.timeout = timeout;
		}
	}
}
