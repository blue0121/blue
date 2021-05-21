package blue.kafka.core;

import blue.base.core.message.ProducerListener;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-20
 */
public interface KafkaProducerListener<T> extends ProducerListener<KafkaTopic, T> {
}
