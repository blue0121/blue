package blue.base.internal.core.cache;

import com.github.benmanes.caffeine.cache.Expiry;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-23
 */
public class DefaultExpiry<K, V> implements Expiry<K, V> {
    private static final long NANO = 1_000_000;

    private final blue.base.core.cache.Expiry<K, V> expiry;

    public DefaultExpiry(blue.base.core.cache.Expiry<K, V> expiry) {
        this.expiry = expiry;
    }

    @Override
    public long expireAfterCreate(K key, V value, long currentTime) {
        long currentTimeMillis = currentTime / NANO;
        return expiry.expireAfterCreate(key, value, currentTimeMillis) * NANO;
    }

    @Override
    public long expireAfterUpdate(K key, V value, long currentTime, long currentDuration) {
        long currentTimeMillis = currentTime / NANO;
        long currentDurationMillis = currentDuration / NANO;
        return expiry.expireAfterUpdate(key, value, currentTimeMillis, currentDurationMillis) * NANO;
    }

    @Override
    public long expireAfterRead(K key, V value, long currentTime, long currentDuration) {
        long currentTimeMillis = currentTime / NANO;
        long currentDurationMillis = currentDuration / NANO;
        return expiry.expireAfterRead(key, value, currentTimeMillis, currentDurationMillis) * NANO;
    }
}
