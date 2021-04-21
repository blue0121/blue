package blue.base.core.cache;

import blue.base.core.util.AssertUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-21
 */
@FunctionalInterface
public interface CacheLoader<K, V> {

	/**
	 * 加载缓存
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	V load(K key) throws Exception;

	/**
	 * 批量加载缓存
	 *
	 * @param keys
	 * @return
	 * @throws Exception
	 */
	default Map<? extends K, ? extends V> loadAll(Set<? extends K> keys) throws Exception {
		AssertUtil.notEmpty(keys, "Keys");
		Map<K, V> map = new HashMap<>();
		for (K key : keys) {
			map.put(key, this.load(key));
		}
		return map;
	}

}
