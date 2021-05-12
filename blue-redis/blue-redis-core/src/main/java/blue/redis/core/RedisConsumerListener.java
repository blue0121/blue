package blue.redis.core;

import blue.base.core.message.ConsumerListener;
import blue.base.core.message.Topic;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-12
 */
public interface RedisConsumerListener<T> extends ConsumerListener<Topic, T> {
}
