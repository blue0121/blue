package blue.base.internal.core.collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2021-02-08
 */
public class HashMultiMap<K, V> extends AbstractMultiMap<K, V> {
	private static Logger logger = LoggerFactory.getLogger(HashMultiMap.class);

	public HashMultiMap(Map<K, Set<V>> map) {
		this.map = map;
		if (logger.isDebugEnabled()) {
			logger.debug("Use {}", map.getClass().getSimpleName());
		}
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public V put(K key, V value) {
		Set<V> set = null;
		if (map instanceof HashMap) {
			set = map.computeIfAbsent(key, k -> new HashSet<>());
		}
		else {
			set = map.computeIfAbsent(key, k -> new ConcurrentHashSet<>());
		}
		set.add(value);
		return value;
	}

	@Override
	public boolean remove(K key) {
		Set<V> set = map.remove(key);
		return set != null;
	}
}
