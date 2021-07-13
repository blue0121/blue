package blue.base.internal.core.cache;

import blue.base.core.cache.Cache;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.util.Collection;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-21
 */
public class CaffeineCache<K, V> implements Cache<K, V> {
    private final com.github.benmanes.caffeine.cache.Cache<K, V> cache;

	public CaffeineCache(com.github.benmanes.caffeine.cache.Cache<K, V> cache) {
	    this.cache = cache;
	}

    @Override
    public V get(K key) {
	    if (cache instanceof LoadingCache) {
	        return ((LoadingCache<K, V>)cache).get(key);
        }
        return cache.getIfPresent(key);
    }

    @Override
    public Map<K, V> getAll(Collection<? extends K> keys) {
        if (cache instanceof LoadingCache) {
            return ((LoadingCache<K, V>)cache).getAll(keys);
        }
        return cache.getAllPresent(keys);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        cache.putAll(map);
    }

    @Override
    public void refresh(K key) {
        if (cache instanceof LoadingCache) {
            ((LoadingCache<K, V>) cache).refresh(key);
        }
    }

    @Override
    public void refreshAll(Collection<? extends K> keys) {
        if (cache instanceof LoadingCache) {
            ((LoadingCache<K, V>) cache).refreshAll(keys);
        }
    }

    @Override
    public void remove(K key) {
        cache.invalidate(key);
    }

    @Override
    public void removeAll(Collection<? extends K> keys) {
        cache.invalidateAll(keys);
    }

    @Override
    public void clear() {
        cache.invalidateAll();
    }
}
