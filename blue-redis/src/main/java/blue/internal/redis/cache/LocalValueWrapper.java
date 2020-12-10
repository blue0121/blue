package blue.internal.redis.cache;

/**
 * @author Jin Zheng
 * @since 1.0 2020-12-08
 */
public class LocalValueWrapper
{
	private final Object value;
	private final long localTtl;

	public LocalValueWrapper(Object value, long localTtl)
	{
		this.value = value;
		this.localTtl = localTtl;
	}

	public Object value()
	{
		return value;
	}

	public long localTtl()
	{
		return localTtl;
	}
}
