package blue.redis.internal.core.lock;

import blue.base.core.util.AssertUtil;
import blue.redis.core.RedisLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-13
 */
public class DefaultRedisLock implements RedisLock {
    private static Logger logger = LoggerFactory.getLogger(DefaultRedisLock.class);

    private final RedissonClient client;

    public DefaultRedisLock(RedissonClient client) {
        this.client = client;
    }

    @Override
    public <T> T lock(String key, long leaseMillis, Supplier<T> f) {
        AssertUtil.notEmpty(key, "Key");
        AssertUtil.positive(leaseMillis, "Lease Milliseconds");
        AssertUtil.notNull(f, "Function");
        T result = null;
        RLock lock = null;
        try {
            lock = client.getLock(key);
            lock.lock(leaseMillis, TimeUnit.MILLISECONDS);
            logger.info("Redis lock, key: {}, {} ms", key, leaseMillis);
            result = f.get();
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
        return result;
    }
}
