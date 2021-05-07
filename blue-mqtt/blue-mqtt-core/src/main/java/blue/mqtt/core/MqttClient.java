package blue.mqtt.core;

import blue.mqtt.internal.core.client.DefaultMqttClient;

/**
 * @author Jin Zheng
 * @since 2021-05-03
 */
public interface MqttClient {

	/**
	 * 创建MQTT客户端
	 *
	 * @param options
	 * @return
	 */
	static MqttClient create(MqttClientOptions options) {
		DefaultMqttClient client = new DefaultMqttClient(options);
		client.connect();
		return client;
	}

	/**
	 * 断开连接
	 */
	void disconnect();

	/**
	 * 创建MQTT生产者
	 *
	 * @param options
	 * @return
	 */
	MqttProducer createProducer(MqttProducerOptions options);

	/**
	 * 创建MQTT消费者
	 *
	 * @param options
	 * @return
	 */
	MqttConsumer createConsumer(MqttConsumerOptions options);

}
