package blue.mqtt.core.options;

import blue.base.core.message.ConsumerOptions;
import blue.mqtt.core.MqttQos;

/**
 * @author Jin Zheng
 * @since 2021-05-03
 */
public class MqttConsumerOptions extends ConsumerOptions {
	private MqttQos defaultQos = MqttQos.AT_MOST_ONCE;

	public MqttConsumerOptions() {
	}

	public MqttQos getDefaultQos() {
		return defaultQos;
	}

	public MqttConsumerOptions setDefaultQos(int qos) {
		this.defaultQos = MqttQos.valueOf(qos);
		return this;
	}

	public MqttConsumerOptions setDefaultQos(MqttQos defaultQos) {
		this.defaultQos = defaultQos;
		return this;
	}
}
