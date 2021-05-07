package blue.mqtt.core;

import blue.base.core.message.ConsumerOptions;

/**
 * @author Jin Zheng
 * @since 2021-05-03
 */
public class MqttConsumerOptions extends ConsumerOptions {
	private MqttQos defaultQos;

	public MqttConsumerOptions() {
	}

	public MqttQos getDefaultQos() {
		return defaultQos;
	}

	public void setDefaultQos(MqttQos defaultQos) {
		this.defaultQos = defaultQos;
	}
}
