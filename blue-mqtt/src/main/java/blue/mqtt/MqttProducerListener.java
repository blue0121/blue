package blue.mqtt;

import blue.internal.core.message.ProducerListener;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-06
 */
public interface MqttProducerListener<T> extends ProducerListener<MqttTopic, T>
{
}
