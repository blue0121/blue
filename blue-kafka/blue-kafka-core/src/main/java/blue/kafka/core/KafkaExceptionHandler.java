package blue.kafka.core;

import blue.base.core.message.ExceptionHandler;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-07
 */
public interface KafkaExceptionHandler<T> extends ExceptionHandler<KafkaTopic, T> {
}
