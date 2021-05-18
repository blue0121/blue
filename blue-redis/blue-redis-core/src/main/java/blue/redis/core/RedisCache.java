package blue.redis.core;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-17
 */
public interface RedisCache<T> {

	/**
	 * get value
	 *
	 * @param key
	 * @return
	 */
	T get(String key);

	/**
	 * get value map
	 *
	 * @param keys
	 * @return
	 */
	Map<String, T> getMap(String... keys);

	/**
	 * set value
	 *
	 * @param key
	 * @param value
	 */
	void set(String key, T value);

	/**
	 * set value map
	 *
	 * @param map
	 */
	void setMap(Map<String, T> map);

	/**
	 * remove
	 *
	 * @param keys
	 */
	void remove(String... keys);

	/**
	 * clear
	 */
	void clear();

	/**
	 * disconnect
	 */
	void disconnect();

}
