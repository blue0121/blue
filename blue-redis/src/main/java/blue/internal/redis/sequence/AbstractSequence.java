package blue.internal.redis.sequence;

import blue.core.util.AssertUtil;
import blue.redis.Sequence;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-08
 */
public abstract class AbstractSequence implements Sequence, InitializingBean
{
	private static Logger logger = LoggerFactory.getLogger(AbstractSequence.class);
	
	protected RedissonClient redisson;
	protected RAtomicLong increasement;
	protected String key;
	protected int length;
	protected String prefix;
	protected String suffix;

	public AbstractSequence()
	{
	}

	@Override
	public final String nextValue()
	{
		int len = length - prefix.length() - suffix.length();
		StringBuilder strVal = new StringBuilder(length).append(prefix);

		long val = increasement.incrementAndGet();
		logger.debug("current counter: {}, key: {}", val, key);

		this.append(strVal, len, val);

		strVal.append(suffix);
		String nextValue = strVal.toString();
		logger.debug("next value: {}", nextValue);
		return nextValue;
	}

	@Override
	public final void reset()
	{
		increasement.setAsync(0L);
		logger.info("reset sequence, key: {}", key);
	}

	protected abstract void append(StringBuilder strVal, int len, long val);

	@Override
	public void afterPropertiesSet() throws Exception
	{
		AssertUtil.notNull(redisson, "Redisson");
		AssertUtil.notEmpty(key, "Key");
		AssertUtil.positive(length, "Length");
		if (prefix == null)
		{
			prefix = "";
		}
		if (suffix == null)
		{
			suffix = "";
		}
		this.increasement = redisson.getAtomicLong(key);
	}

	public RedissonClient getRedisson()
	{
		return redisson;
	}

	public void setRedisson(RedissonClient redisson)
	{
		this.redisson = redisson;
	}

	public RAtomicLong getIncreasement()
	{
		return increasement;
	}

	public void setIncreasement(RAtomicLong increasement)
	{
		this.increasement = increasement;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}

	public String getPrefix()
	{
		return prefix;
	}

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	public String getSuffix()
	{
		return suffix;
	}

	public void setSuffix(String suffix)
	{
		this.suffix = suffix;
	}
}
