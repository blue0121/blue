package blue.mqtt.core;

import blue.base.core.message.Topic;

/**
 * @author Jin Zheng
 * @since 2019-06-30
 */
public class MqttTopic extends Topic {
	protected MqttQos qos;

	public MqttTopic(String topic) {
		this(topic, null);
	}

	public MqttTopic(String topic, MqttQos qos) {
		super(topic);
		this.qos = qos;
	}

	public MqttTopic(String topic, int qos) {
		super(topic);
		this.qos = MqttQos.valueOf(qos);
	}

	public MqttQos getQos() {
		return qos;
	}

	public void setQos(MqttQos qos) {
		this.qos = qos;
	}

	@Override
	public String toString() {
		return String.format("MqttTopic[topic: %s, qos: %s]", topic, qos);
	}

}
