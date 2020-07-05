package blue.redis.lock;

/**
 * @author Jin Zheng
 * @since 2020-07-05
 */
@FunctionalInterface
public interface LockCallback<T>
{
	/**
	 * business process
	 */
	T process();
}
