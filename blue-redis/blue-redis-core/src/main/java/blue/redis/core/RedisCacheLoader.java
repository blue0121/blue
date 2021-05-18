package blue.redis.core;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-18
 */
public interface RedisCacheLoader<T> {

	/**
	 * load value
	 *
	 * @param key
	 * @return
	 */
	T load(String key);

	/**
	 * load all keys
	 *
	 * @return
	 */
	Iterable<String> loadAllKeys();

}
