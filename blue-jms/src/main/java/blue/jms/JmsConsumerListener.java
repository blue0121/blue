package blue.jms;

import blue.internal.core.message.ConsumerListener;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-07
 */
public interface JmsConsumerListener<T> extends ConsumerListener<JmsTopic, T>
{
}
