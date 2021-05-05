package blue.mqtt.core;

import blue.mqtt.internal.core.producer.DefaultMqttClient;

/**
 * @author Jin Zheng
 * @since 2021-05-03
 */
public interface MqttClient {

	static MqttClient create(MqttClientOptions options) {
		DefaultMqttClient client = new DefaultMqttClient(options);
		client.connect();
		return client;
	}

	void disconnect();

	MqttProducer createProducer(MqttProducerOptions options);

	MqttConsumer createConsumer(MqttConsumerOptions options);

}
