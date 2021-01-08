package blue.redis;

import blue.core.message.Topic;
import blue.internal.core.message.ProducerListener;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-08
 */
public interface RedisProducerListener<T> extends ProducerListener<Topic, T>
{
}
