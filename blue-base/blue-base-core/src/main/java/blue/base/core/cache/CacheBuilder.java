package blue.base.core.cache;

import blue.base.internal.core.cache.DefaultCache;
import blue.base.internal.core.cache.DefaultCacheLoader;
import blue.base.internal.core.cache.DefaultExpiry;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-21
 */
public class CacheBuilder {
    private Caffeine<Object, Object> caffeine = Caffeine.newBuilder();

	private CacheBuilder() {
	}

	public static CacheBuilder create() {
	    return new CacheBuilder();
    }

    public CacheBuilder expireAfterWrite(long val, TimeUnit unit) {
        caffeine.expireAfterWrite(val, unit);
        return this;
    }

    public CacheBuilder expireAfterAccess(long val, TimeUnit unit) {
        caffeine.expireAfterAccess(val, unit);
        return this;
    }

    public <K, V> CacheBuilder expire(Expiry<K, V> expiry) {
		caffeine.expireAfter(new DefaultExpiry<>(expiry));
		return this;
    }

    public CacheBuilder maximumSize(long size) {
        caffeine.maximumSize(size);
	    return this;
    }

    public <K, V> Cache<K, V> build() {
        return new DefaultCache<>(caffeine.build());
    }

    public <K, V> Cache<K, V> build(CacheLoader<K, V> loader) {
        return new DefaultCache<>(caffeine.build(new DefaultCacheLoader<>(loader)));
    }
}
