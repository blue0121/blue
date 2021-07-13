package blue.base.internal.core.cache;

import blue.base.core.cache.Cache;
import blue.base.core.cache.CacheBuilder;
import blue.base.core.cache.CacheLoader;
import blue.base.core.cache.Expiry;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-21
 */
public class CaffeineCacheBuilder implements CacheBuilder {
    private Caffeine<Object, Object> caffeine = Caffeine.newBuilder();

	public CaffeineCacheBuilder() {
	}

	@Override
    public CaffeineCacheBuilder expireAfterWrite(long val, TimeUnit unit) {
        caffeine.expireAfterWrite(val, unit);
        return this;
    }

    @Override
    public CaffeineCacheBuilder expireAfterAccess(long val, TimeUnit unit) {
        caffeine.expireAfterAccess(val, unit);
        return this;
    }

	@Override
    public <K, V> CaffeineCacheBuilder expire(Expiry<K, V> expiry) {
		caffeine.expireAfter(new CaffeineExpiry<>(expiry));
		return this;
    }

	@Override
    public CaffeineCacheBuilder maximumSize(long size) {
        caffeine.maximumSize(size);
	    return this;
    }

	@Override
    public <K, V> Cache<K, V> build() {
        return new CaffeineCache<>(caffeine.build());
    }

	@Override
    public <K, V> Cache<K, V> build(CacheLoader<K, V> loader) {
        return new CaffeineCache<>(caffeine.build(new CaffeineCacheLoader<>(loader)));
    }
}
