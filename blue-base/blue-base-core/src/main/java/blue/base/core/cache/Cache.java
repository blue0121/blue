package blue.base.core.cache;

import blue.base.internal.core.cache.CaffeineCacheBuilder;

import java.util.Collection;
import java.util.Map;

/**
 * 缓存
 *
 * @author Jin Zheng
 * @since 1.0 2021-04-21
 */
public interface Cache<K, V> {

	static CacheBuilder builder() {
		return new CaffeineCacheBuilder();
	}

	/**
	 * 获取缓存
	 *
	 * @param key
	 * @return 不存在返回null
	 */
	V get(K key);

	/**
	 * 批量获取缓存
	 *
	 * @param keys
	 * @return
	 */
	Map<K, V> getAll(Collection<? extends K> keys);

	/**
	 * 设置缓存
	 *
	 * @param key
	 * @param value
	 */
	void put(K key, V value);

	/**
	 * 批量设置缓存
	 *
	 * @param map
	 */
	void putAll(Map<? extends K, ? extends V> map);

	/**
	 * 刷新缓存
	 *
	 * @param key
	 */
	void refresh(K key);

	/**
	 * 批量刷新缓存
	 *
	 * @param keys
	 */
	void refreshAll(Collection<? extends K> keys);

	/**
	 * 删除缓存
	 *
	 * @param key
	 */
	void remove(K key);

	/**
	 * 批量删除缓存
	 *
	 * @param keys
	 */
	void removeAll(Collection<? extends K> keys);

	/**
	 * 清除缓存
	 */
	void clear();

}
