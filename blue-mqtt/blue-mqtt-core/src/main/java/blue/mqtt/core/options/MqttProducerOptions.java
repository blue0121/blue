package blue.mqtt.core.options;

import blue.base.core.message.ProducerOptions;
import blue.mqtt.core.MqttQos;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-29
 */
public class MqttProducerOptions extends ProducerOptions {
    private MqttQos defaultQos = MqttQos.AT_MOST_ONCE;

	public MqttProducerOptions() {
	}

    public MqttQos getDefaultQos() {
        return defaultQos;
    }

    public MqttProducerOptions setDefaultQos(int qos) {
		this.defaultQos = MqttQos.valueOf(qos);
		return this;
    }

    public MqttProducerOptions setDefaultQos(MqttQos defaultQos) {
        this.defaultQos = defaultQos;
        return this;
    }
}
