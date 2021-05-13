package blue.redis.core;

import java.util.function.Supplier;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-13
 */
public interface RedisLock {

	long LEASE_MILLIS = 5000;

	/**
	 * 分步式锁
	 *
	 * @param key
	 * @param f 执行方法
	 * @param <T>
	 * @return
	 */
	default <T> T lock(String key, Supplier<T> f) {
		return this.lock(key, LEASE_MILLIS, f);
	}

	/**
	 * 分步式锁
	 *
	 * @param key
	 * @param leaseMillis
	 * @param f 执行方法
	 * @param <T>
	 * @return
	 */
	<T> T lock(String key, long leaseMillis, Supplier<T> f);

}
