package blue.redis.cache;

/**
 * @author Jin Zheng
 * @since 1.0 2020-12-08
 */
public class CacheConfig
{
	private long ttl = 10 * 60 * 1000;
	private long localTtl = 50 * 1000;
	private long localMaxSize = 1000;
	private long timeout = 2 * 1000;

	public CacheConfig()
	{
	}

	public CacheConfig(long ttl, long localTtl, long localMaxSize, long timeout)
	{
		this.ttl = ttl;
		this.localTtl = localTtl;
		this.localMaxSize = localMaxSize;
		this.timeout = timeout;
	}

	@Override
	public String toString()
	{
		return String.format("%s{ttl: %d, localTtl: %d, localMaxSize: %d, timeout: %d}",
				this.getClass().getSimpleName(), ttl, localTtl, localMaxSize, timeout);
	}

	public long ttl()
	{
		return ttl;
	}

	public long localTtl()
	{
		return localTtl;
	}

	public long localMaxSize()
	{
		return localMaxSize;
	}

	public long timeout()
	{
		return timeout;
	}
}
