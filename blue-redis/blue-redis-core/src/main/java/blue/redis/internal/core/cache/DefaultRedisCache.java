package blue.redis.internal.core.cache;

import blue.base.core.util.AssertUtil;
import blue.redis.core.options.RedisCacheOptions;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-17
 */
public class DefaultRedisCache<T> extends AbstractRedisCache<T> {
    private static Logger logger = LoggerFactory.getLogger(DefaultRedisCache.class);

    public DefaultRedisCache(RedisCacheOptions options, RedissonClient client) {
        super(options, client);
    }

    @Override
    public T get(String key) {
        AssertUtil.notEmpty(key, "Key");
        T value = this.getMapCache().get(key);
        this.logOperation(RedisOperation.GET, key);
        return value;
    }

    @Override
    public Map<String, T> getMap(String... keys) {
        AssertUtil.notEmpty(keys, "Keys");
        Map<String, T> map = this.getMapCache().getAll(Set.of(keys));
        this.logOperation(RedisOperation.GET, keys);
        return map;
    }

    @Override
    public void set(String key, T value) {
        AssertUtil.notEmpty(key, "Key");
        this.getMapCache().fastPut(key, value, options.getTtl(), TimeUnit.MILLISECONDS);
        this.logOperation(RedisOperation.SET, key);
    }

    @Override
    public void setMap(Map<String, T> map) {
        AssertUtil.notEmpty(map, "Map");
        this.getMapCache().putAll(map, options.getTtl(), TimeUnit.MILLISECONDS);
        this.logOperation(RedisOperation.SET, map.keySet());
    }

    @Override
    public void remove(String... keys) {
        AssertUtil.notEmpty(keys, "Keys");
        this.getMapCache().fastRemove(keys);
        this.logOperation(RedisOperation.DELETE, keys);
    }

    @Override
    public void clear() {
        this.getMapCache().clear();
        this.logOperation(RedisOperation.CLEAR);
    }

    @Override
    public void disconnect() {
        this.logOperation(RedisOperation.DISCONNECT);
    }

}
