package blue.internal.mqtt.producer;

import blue.core.util.AssertUtil;
import blue.core.util.WaitUtil;
import blue.internal.core.message.AbstractProducer;
import blue.internal.core.message.LoggerProducerListener;
import blue.internal.core.message.ProducerListener;
import blue.mqtt.MqttException;
import blue.mqtt.MqttProducer;
import blue.mqtt.MqttQos;
import blue.mqtt.MqttTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author Jin Zheng
 * @since 2019-06-30
 */
public class DefaultMqttProducer extends AbstractProducer<MqttTopic> implements MqttProducer, InitializingBean
{
	private static Logger logger = LoggerFactory.getLogger(DefaultMqttProducer.class);

	private LongAdder longAdder = new LongAdder();
	private List<MqttClient> mqttClientList = new ArrayList<>();
	private MqttClient mqttClient;
	private MqttQos defaultQos;
	private int batch = 1;
	private ProducerListener<MqttTopic, Object> listener;

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
			this.getBatchMqttClient().publish(topic, message, new MqttProducerListener(latch, topic, message, this.listener));
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
			this.getBatchMqttClient().publish(topic, message, listener);
		}
	}

	@Override
	public void setProducerListener(ProducerListener<MqttTopic, Object> listener)
	{
		this.listener = listener;
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		AssertUtil.notNull(mqttClient, "MqttClient");
		AssertUtil.notNull(defaultQos, "DefaultQos");

		if (batch < 1)
			throw new MqttException("batch is less than 1");
		if (batch > 200)
			throw new MqttException("batch is more than 200");

		if (batch > 1)
		{
			mqttClientList.add(mqttClient);
			for (int i = 1; i < batch; i++)
			{
				this.initBatchMqttClient(i);
			}
		}

		if (this.listener == null)
		{
			this.listener = new LoggerProducerListener<>();
			logger.info("'{}' Default ProducerListener is empty, use LoggerProducerListener", name);
		}
	}

	private void initBatchMqttClient(int index) throws Exception
	{
		MqttClient client = new MqttClient();
		client.setBroker(mqttClient.getBroker());
		client.setUsername(mqttClient.getUsername());
		client.setPassword(mqttClient.getPassword());
		client.setTimeout(mqttClient.getTimeout());
		client.setKeepAliveInterval(mqttClient.getKeepAliveInterval());
		client.setReconnectDelay(mqttClient.getReconnectDelay());
		client.setSslContext(mqttClient.getSslContext());
		if (mqttClient.getClientId() != null && !mqttClient.getClientId().isEmpty())
		{
			client.setClientId(mqttClient.getClientId() + index);
		}
		client.afterPropertiesSet();
		mqttClientList.add(client);
	}

	private void setMqttTopic(MqttTopic topic)
	{
		if (topic.getQos() == null)
		{
			topic.setQos(defaultQos);
		}
	}

	private MqttClient getBatchMqttClient()
	{
		if (batch == 1)
			return mqttClient;

		int index = (int)(longAdder.longValue() % batch);
		longAdder.increment();
		if (logger.isDebugEnabled())
		{
			logger.debug("current longAdder is: {}, index: {}", longAdder.longValue(), index);
		}
		return mqttClientList.get(index);
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

	public int getBatch()
	{
		return batch;
	}

	public void setBatch(int batch)
	{
		this.batch = batch;
	}
}
