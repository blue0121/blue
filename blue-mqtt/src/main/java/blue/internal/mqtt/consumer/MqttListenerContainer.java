package blue.internal.mqtt.consumer;

import blue.internal.core.message.AbstractListenerContainer;
import blue.internal.core.message.ConsumerListenerConfig;
import blue.internal.mqtt.producer.MqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2019-07-28
 */
public class MqttListenerContainer extends AbstractListenerContainer
{
	private static Logger logger = LoggerFactory.getLogger(MqttListenerContainer.class);

	private MqttClient mqttClient;

	public MqttListenerContainer()
	{
	}

	@Override
	protected void start()
	{
		List<MqttListenerConfig> list = new ArrayList<>();
		for (ConsumerListenerConfig config : configList)
		{
			list.add((MqttListenerConfig) config);
		}
		mqttClient.subscribe(list);
	}

	public MqttClient getMqttClient()
	{
		return mqttClient;
	}

	public void setMqttClient(MqttClient mqttClient)
	{
		this.mqttClient = mqttClient;
	}
}
