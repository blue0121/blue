package blue.internal.redis.cache;

/**
 * @author Jin Zheng
 * @since 2020-12-05
 */
public class RedisCacheConfig
{
	private String name;
	private long ttl = 10 * 60 * 1000;
	private long localTtl = 50 * 1000;
	private long localMaxSize = 1000;
	private long timeout = 2 * 1000;

	public RedisCacheConfig()
	{
	}

	public RedisCacheConfig copy()
	{
		RedisCacheConfig config = new RedisCacheConfig();
		config.name = this.name;
		config.ttl = this.ttl;
		config.localTtl = this.localTtl;
		config.localMaxSize = this.localMaxSize;
		config.timeout = timeout;
		return config;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public long getTtl()
	{
		return ttl;
	}

	public void setTtl(long ttl)
	{
		this.ttl = ttl;
	}

	public long getLocalTtl()
	{
		return localTtl;
	}

	public void setLocalTtl(long localTtl)
	{
		this.localTtl = localTtl;
	}

	public long getLocalMaxSize()
	{
		return localMaxSize;
	}

	public void setLocalMaxSize(long localMaxSize)
	{
		this.localMaxSize = localMaxSize;
	}

	public long getTimeout()
	{
		return timeout;
	}

	public void setTimeout(long timeout)
	{
		this.timeout = timeout;
	}
}
