package blue.base.core.collection;

import blue.base.internal.core.collection.HashMultiMap;
import blue.base.internal.core.collection.ImmutableMultiMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jin Zheng
 * @since 1.0 2021-02-08
 */
public interface MultiMap<K, V> {
	/**
	 * create HashMap
	 *
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	static <K, V> MultiMap<K, V> create() {
		return new HashMultiMap<>(new HashMap<>());
	}

	/**
	 * create ConcurrentHashMap
	 *
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	static <K, V> MultiMap<K, V> createConcurrent() {
		return new HashMultiMap<>(new ConcurrentHashMap<>());
	}

	/**
	 * clear map
	 */
	void clear();

	/**
	 * contains key
	 *
	 * @param key
	 * @return
	 */
	boolean containsKey(K key);

	/**
	 * entry set view
	 *
	 * @return
	 */
	Set<Map.Entry<K, Set<V>>> entrySet();

	/**
	 * get by key
	 *
	 * @param key
	 * @return
	 */
	Set<V> get(K key);

	/**
	 * get one value by key, if not exists return null
	 *
	 * @param key
	 * @return
	 */
	V getOne(K key);

	/**
	 * is empty
	 *
	 * @return
	 */
	boolean isEmpty();

	/**
	 * put value
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	V put(K key, V value);

	/**
	 * remove
	 *
	 * @param key
	 * @return
	 */
	boolean remove(K key);

	/**
	 * size
	 *
	 * @return
	 */
	int size();

	/**
	 * copy to unmodifiable MultiMap
	 *
	 * @param map
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	static <K, V> MultiMap<K, V> copyOf(MultiMap<K, V> map) {
		return new ImmutableMultiMap<>(map);
	}

}
