package blue.internal.mqtt.consumer;

import blue.core.util.AssertUtil;
import blue.internal.core.message.AbstractConsumer;
import blue.internal.core.message.ConsumerListener;
import blue.internal.core.message.ConsumerListenerConfig;
import blue.internal.mqtt.producer.MqttClient;
import blue.mqtt.MqttConsumer;
import blue.mqtt.MqttQos;
import blue.mqtt.MqttTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-04
 */
public class DefaultMqttConsumer extends AbstractConsumer<MqttTopic> implements MqttConsumer
{
	private static Logger logger = LoggerFactory.getLogger(DefaultMqttConsumer.class);

	private MqttClient mqttClient;
	private MqttQos defaultQos;

	public DefaultMqttConsumer()
	{
	}

	@Override
	public void subscribe(Collection<MqttTopic> topicList, ConsumerListener<MqttTopic, ?> listener)
	{
		AssertUtil.notEmpty(topicList, "Topic list");
		List<ConsumerListenerConfig> configList = new ArrayList<>();
		for (var topic : topicList)
		{
			var config = new MqttListenerConfig();
			config.setTopic(topic.getTopic());
			config.setQos(topic.getQos() == null ? defaultQos.getType() : topic.getQos().getType());
			config.setMultiThread(taskExecutor != null);
			config.setListener(listener);
			config.afterPropertiesSet();
			configList.add(config);
		}
		this.checkHandler(configList);
		this.subscribe(configList);
	}

	@Override
	public void unsubscribe(Collection<String> topicList)
	{
		AssertUtil.notEmpty(topicList, "Topic list");
		mqttClient.unsubscribe(topicList);
	}

	@Override
	protected void subscribe(List<ConsumerListenerConfig> configList)
	{
		List<MqttListenerConfig> list = new ArrayList<>();
		for (var config : configList)
		{
			list.add((MqttListenerConfig) config);
		}
		mqttClient.subscribe(list);
	}

	public void setMqttClient(MqttClient mqttClient)
	{
		this.mqttClient = mqttClient;
	}

	public void setDefaultQos(int defaultQos)
	{
		this.defaultQos = MqttQos.valueOf(defaultQos);
	}
}
