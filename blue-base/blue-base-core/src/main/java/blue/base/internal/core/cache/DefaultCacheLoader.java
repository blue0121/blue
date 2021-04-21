package blue.base.internal.core.cache;

import blue.base.core.cache.CacheLoader;

import java.util.Map;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-21
 */
public class DefaultCacheLoader<K, V> implements com.github.benmanes.caffeine.cache.CacheLoader<K, V> {
	private final CacheLoader<K, V> loader;

	public DefaultCacheLoader(CacheLoader<K, V> loader) {
		this.loader = loader;
	}

	@Override
	public V load(K key) throws Exception {
		return loader.load(key);
	}

	@Override
	public Map<? extends K, ? extends V> loadAll(Set<? extends K> keys) throws Exception {
		return loader.loadAll(keys);
	}
}
