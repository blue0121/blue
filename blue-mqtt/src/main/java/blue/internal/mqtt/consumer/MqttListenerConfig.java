package blue.internal.mqtt.consumer;

import blue.core.util.AssertUtil;
import blue.internal.core.message.ConsumerListenerConfig;
import blue.mqtt.MqttQos;

/**
 * @author Jin Zheng
 * @since 2019-06-30
 */
public class MqttListenerConfig extends ConsumerListenerConfig
{
	protected MqttQos qos;

	public MqttListenerConfig()
	{
	}

	@Override
	public void afterPropertiesSet()
	{
		super.afterPropertiesSet();
		AssertUtil.notNull(qos, "MqttQos");
	}

	@Override
	public String toString()
	{
		return String.format("MqttListenerConfig[topic=%s, qos: %s, multi-thread=%s]",
				topic, qos.getType(), multiThread);
	}

	public MqttQos getQos()
	{
		return qos;
	}

	public void setQos(int qos)
	{
		this.qos = MqttQos.valueOf(qos);
	}
}
