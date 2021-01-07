package blue.kafka;

import blue.internal.core.message.ConsumerListener;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-07
 */
public interface KafkaConsumerListener<T> extends ConsumerListener<KafkaTopic, T>
{
}
