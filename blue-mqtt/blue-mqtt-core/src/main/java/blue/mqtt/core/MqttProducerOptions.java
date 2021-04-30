package blue.mqtt.core;

import blue.base.core.message.ProducerOptions;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-29
 */
public class MqttProducerOptions extends ProducerOptions {
    private MqttQos defaultQos;

	public MqttProducerOptions() {
	}

    public MqttQos getDefaultQos() {
        return defaultQos;
    }

    public MqttProducerOptions setDefaultQos(MqttQos defaultQos) {
        this.defaultQos = defaultQos;
        return this;
    }
}
