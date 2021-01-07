package blue.kafka;

import blue.internal.core.message.ProducerListener;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-07
 */
public interface KafkaProducerListener<T> extends ProducerListener<KafkaTopic, T>
{
}
