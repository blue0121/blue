package blue.base.core.cache;

import java.util.concurrent.TimeUnit;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-21
 */
public interface CacheBuilder {

    CacheBuilder expireAfterWrite(long val, TimeUnit unit);

    CacheBuilder expireAfterAccess(long val, TimeUnit unit);

    <K, V> CacheBuilder expire(Expiry<K, V> expiry);

    CacheBuilder maximumSize(long size);

    <K, V> Cache<K, V> build();

    <K, V> Cache<K, V> build(CacheLoader<K, V> loader);
}
