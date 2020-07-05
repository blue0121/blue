package blue.redis.lock;

import blue.core.util.WaitUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author Jin Zheng
 * @since 2020-07-05
 */
public interface LockService
{

	default <T> T lock(String key, LockCallback<T> callback)
	{
		return this.lock(key, callback, WaitUtil.DEFAULT_TIMEOUT, WaitUtil.DEFAULT_TIME_UNIT);
	}

	<T> T lock(String key, LockCallback<T> callback, long leaseTime, TimeUnit timeUnit);

}
