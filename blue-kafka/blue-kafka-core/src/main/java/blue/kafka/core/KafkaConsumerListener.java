package blue.kafka.core;

import blue.base.core.message.ConsumerListener;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-07
 */
public interface KafkaConsumerListener<T> extends ConsumerListener<KafkaTopic, T> {
}
