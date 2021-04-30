package blue.mqtt.core;

import org.fusesource.mqtt.client.QoS;

/**
 * @author Jin Zheng
 * @since 2019-06-30
 */
public enum MqttQos {
	AT_MOST_ONCE(0),
	AT_LEAST_ONCE(1),
	EXACTLY_ONCE(2);

	private int type;

	MqttQos(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public static MqttQos valueOf(int type) {
		for (MqttQos qos : MqttQos.values()) {
			if (qos.type == type) {
				return qos;
			}
		}
		return null;
	}

	public QoS toQoS() {
		return QoS.valueOf(this.name());
	}

	@Override
	public String toString() {
		return String.format("%s{%d}", this.name(), type);
	}

}
