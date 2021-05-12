package blue.redis.core;

import blue.base.core.message.ProducerListener;
import blue.base.core.message.Topic;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-11
 */
public interface RedisProducerListener<T> extends ProducerListener<Topic, T> {
}
