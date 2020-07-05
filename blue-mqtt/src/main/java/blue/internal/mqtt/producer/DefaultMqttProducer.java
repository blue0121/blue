package blue.internal.mqtt.producer;

import blue.core.message.ProducerListener;
import blue.core.util.AssertUtil;
import blue.core.util.WaitUtil;
import blue.mqtt.exception.MqttException;
import blue.mqtt.model.MqttQos;
import blue.mqtt.model.MqttTopic;
import blue.mqtt.producer.MqttProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author Jin Zheng
 * @since 2019-06-30
 */
public class DefaultMqttProducer implements MqttProducer, InitializingBean
{
	private static Logger logger = LoggerFactory.getLogger(DefaultMqttProducer.class);

	private LongAdder longAdder = new LongAdder();
	private List<MqttClient> mqttClientList = new ArrayList<>();
	private MqttClient mqttClient;
	private MqttQos defaultQos;
	private int batch;
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
			this.getBatchMqttClient().publish(topic, message,
					Arrays.asList(new MqttProducerListener(latch, topic, message, this.listener)));
		}
		WaitUtil.await(latch);
	}

	@Override
	public void sendAsync(MqttTopic topic, List<Object> messageList, ProducerListener<MqttTopic, Object> listener)
	{
		AssertUtil.notNull(topic, "Topic");
		AssertUtil.notEmpty(messageList, "Message list");
		this.setMqttTopic(topic);
		List<ProducerListener<MqttTopic, Object>> listenerList = this.getProducerListener(listener);
		for (Object message : messageList)
		{
			this.getBatchMqttClient().publish(topic, message, listenerList);
		}
	}

	private List<ProducerListener<MqttTopic, Object>> getProducerListener(ProducerListener<MqttTopic, Object> listener)
	{
		List<ProducerListener<MqttTopic, Object>> listenerList = new ArrayList<>();
		if (this.listener != null)
		{
			listenerList.add(this.listener);
		}
		if (listener != null)
		{
			listenerList.add(listener);
		}
		return listenerList;
	}

	@Override
	public void setProducerListener(ProducerListener<MqttTopic, Object> listener)
	{
		this.listener = listener;
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (mqttClient == null)
			throw new MqttException("mqttClient is null");
		if (defaultQos == null)
			throw new MqttException("defaultQos is empty");
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
	}

	private void initBatchMqttClient(int index) throws Exception
	{
		MqttClient client = new MqttClient();
		client.setUrl(mqttClient.getUrl());
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
