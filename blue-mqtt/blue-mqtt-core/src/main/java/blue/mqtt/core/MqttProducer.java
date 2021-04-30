package blue.mqtt.core;

import blue.base.core.message.Producer;
import blue.mqtt.internal.core.producer.DefaultMqttProducer;

/**
 * @author Jin Zheng
 * @since 2020-07-05
 */
public interface MqttProducer extends Producer<MqttTopic> {

	static MqttProducer create(String name, MqttProducerOptions options) {
		DefaultMqttProducer producer = new DefaultMqttProducer(name, options);
		producer.init();
		return producer;
	}
}
