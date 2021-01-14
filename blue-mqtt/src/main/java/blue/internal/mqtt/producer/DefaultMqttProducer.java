package blue.internal.mqtt.producer;

import blue.core.util.AssertUtil;
import blue.core.util.WaitUtil;
import blue.internal.core.message.AbstractProducer;
import blue.internal.core.message.ProducerListener;
import blue.mqtt.MqttProducer;
import blue.mqtt.MqttQos;
import blue.mqtt.MqttTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author Jin Zheng
 * @since 2019-06-30
 */
public class DefaultMqttProducer extends AbstractProducer<MqttTopic> implements MqttProducer
{
	private static Logger logger = LoggerFactory.getLogger(DefaultMqttProducer.class);

	private MqttClient mqttClient;
	private MqttQos defaultQos;

	public DefaultMqttProducer()
	{
	}

	@Override
	public void sendSync(MqttTopic topic, List<Object> messageList)
	{
		AssertUtil.notNull(topic, "Topic");
		AssertUtil.notEmpty(messageList, "Message list");
		this.setMqttTopic(topic);
		CountDownLatch latch = new CountDownLatch(messageList.size());
		for (Object message : messageList)
		{
			this.mqttClient.publish(topic, message, new MqttProducerListener(latch, topic, message, this.listener));
		}
		WaitUtil.await(latch);
	}

	@Override
	public void sendAsync(MqttTopic topic, List<Object> messageList, ProducerListener<MqttTopic, Object> listener)
	{
		AssertUtil.notNull(topic, "Topic");
		AssertUtil.notEmpty(messageList, "Message list");
		this.setMqttTopic(topic);
		if (listener == null)
		{
			listener = this.listener;
		}
		for (Object message : messageList)
		{
			this.mqttClient.publish(topic, message, listener);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		AssertUtil.notNull(mqttClient, "MqttClient");
		AssertUtil.notNull(defaultQos, "DefaultQos");

		super.afterPropertiesSet();
	}

	private void setMqttTopic(MqttTopic topic)
	{
		if (topic.getQos() == null)
		{
			topic.setQos(defaultQos);
		}
	}

	public MqttClient getMqttClient()
	{
		return mqttClient;
	}

	public void setMqttClient(MqttClient mqttClient)
	{
		this.mqttClient = mqttClient;
	}

	public void setDefaultQos(int defaultQos)
	{
		this.defaultQos = MqttQos.valueOf(defaultQos);
	}

	public MqttQos getDefaultQos()
	{
		return defaultQos;
	}
}
