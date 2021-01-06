package blue.mqtt;

import blue.internal.core.message.ConsumerListener;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-06
 */
public interface MqttConsumerListener<T> extends ConsumerListener<MqttTopic, T>
{
}
