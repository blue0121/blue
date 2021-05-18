package blue.redis.core;

import java.util.Collection;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-18
 */
public interface RedisCacheWriter<T> {

	/**
	 * delete keys
	 *
	 * @param keys
	 */
	void delete(Collection<T> keys);

	/**
	 * write map
	 *
	 * @param map
	 */
	void write(Map<String, T> map);

}
