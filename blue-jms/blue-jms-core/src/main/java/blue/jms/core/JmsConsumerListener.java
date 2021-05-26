package blue.jms.core;

import blue.base.core.message.ConsumerListener;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-26
 */
public interface JmsConsumerListener<T> extends ConsumerListener<JmsTopic, T> {
}
