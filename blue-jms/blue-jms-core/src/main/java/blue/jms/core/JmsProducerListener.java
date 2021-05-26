package blue.jms.core;

import blue.base.core.message.ProducerListener;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-26
 */
public interface JmsProducerListener<T> extends ProducerListener<JmsTopic, T> {
}
