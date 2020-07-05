package blue.internal.redis.lock;

import blue.core.util.AssertUtil;
import blue.redis.lock.LockCallback;
import blue.redis.lock.LockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * Redisson Distributed Locker
 *
 * @author Jin Zheng
 * @since 1.0 2019-10-25
 */
public class DefaultLockService implements LockService
{
	private RedissonClient redisson;

	public DefaultLockService()
	{
	}

	@Override
	public <T> T lock(String key, LockCallback<T> callback, long leaseTime, TimeUnit timeUnit)
	{
		AssertUtil.notEmpty(key, "Key");
		AssertUtil.notNull(callback, "Callback");
		AssertUtil.notNull(timeUnit, "TimeUnit");
		AssertUtil.positive(leaseTime, "leaseTime");

		RLock lock = null;
		try
		{
			lock = redisson.getLock(key);
			lock.lock(leaseTime, timeUnit);
			return callback.process();
		}
		finally
		{
			if (lock != null)
			{
				lock.unlock();
			}
		}
	}

	public void setRedisson(RedissonClient redisson)
	{
		this.redisson = redisson;
	}
}
