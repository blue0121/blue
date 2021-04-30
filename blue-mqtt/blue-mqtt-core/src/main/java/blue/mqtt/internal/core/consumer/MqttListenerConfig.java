package blue.mqtt.internal.core.consumer;

import blue.base.core.util.AssertUtil;
import blue.base.internal.core.message.ConsumerListenerConfig;
import blue.mqtt.core.MqttQos;

/**
 * @author Jin Zheng
 * @since 2019-06-30
 */
public class MqttListenerConfig extends ConsumerListenerConfig {
	protected MqttQos qos;

	public MqttListenerConfig() {
	}

	@Override
	public void init() {
		super.init();
		AssertUtil.notNull(qos, "MqttQos");
	}

	@Override
	public String toString() {
		return String.format("MqttListenerConfig[topic=%s, qos: %s, multi-thread=%s]",
				topic, qos.getType(), multiThread);
	}

	public MqttQos getQos() {
		return qos;
	}

	public void setQos(int qos) {
		this.qos = MqttQos.valueOf(qos);
	}
}
