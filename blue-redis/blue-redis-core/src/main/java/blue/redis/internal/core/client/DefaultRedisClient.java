package blue.redis.internal.core.client;

import blue.redis.core.RedisClient;
import blue.redis.core.RedisConsumer;
import blue.redis.core.RedisLock;
import blue.redis.core.RedisProducer;
import blue.redis.core.options.RedisClientOptions;
import blue.redis.core.options.RedisConsumerOptions;
import blue.redis.core.options.RedisProducerOptions;
import blue.redis.internal.core.consumer.DefaultRedisConsumer;
import blue.redis.internal.core.lock.DefaultRedisLock;
import blue.redis.internal.core.producer.DefaultRedisProducer;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-12
 */
public class DefaultRedisClient implements RedisClient {
    private static Logger logger = LoggerFactory.getLogger(DefaultRedisClient.class);

    private final RedisClientOptions options;
    private final RedissonClient client;

    public DefaultRedisClient(RedisClientOptions options) {
        this.options = options;
        var config = new RedissonConfig(options);
        this.client = Redisson.create(config.getConfig());
        logger.info("Redis '{}' connect successful, broker: {}", options.getId(), options.getBroker());
    }

    @Override
    public void disconnect() {
        if (client != null && !client.isShutdown()) {
            client.shutdown();
            logger.info("Redis '{}' disconnect successful, broker: {}", options.getId(), options.getBroker());
        }
    }

    @Override
    public RedisProducer createProducer(RedisProducerOptions options) {
        return new DefaultRedisProducer(options, client);
    }

    @Override
    public RedisConsumer createConsumer(RedisConsumerOptions options) {
        return new DefaultRedisConsumer(options, client);
    }

    @Override
    public RedisLock createLock() {
        return new DefaultRedisLock(client);
    }

}
