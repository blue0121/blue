package blue.internal.redis.cache;

/**
 * @author Jin Zheng
 * @since 2020-12-05
 */
public class CacheConfig
{
	private long ttl;
	private long maxIdleTime;
	private int maxSize;

	public CacheConfig()
	{
	}

	public long getTtl()
	{
		return ttl;
	}

	public void setTtl(long ttl)
	{
		this.ttl = ttl;
	}

	public long getMaxIdleTime()
	{
		return maxIdleTime;
	}

	public void setMaxIdleTime(long maxIdleTime)
	{
		this.maxIdleTime = maxIdleTime;
	}

	public int getMaxSize()
	{
		return maxSize;
	}

	public void setMaxSize(int maxSize)
	{
		this.maxSize = maxSize;
	}
}
