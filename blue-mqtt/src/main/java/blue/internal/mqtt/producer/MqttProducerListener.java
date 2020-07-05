package blue.internal.mqtt.producer;

import blue.core.message.ProducerListener;
import blue.mqtt.model.MqttTopic;

import java.util.concurrent.CountDownLatch;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-13
 */
public class MqttProducerListener implements ProducerListener<MqttTopic, Object>
{
	private CountDownLatch latch;
	private MqttTopic topic;
	private Object message;
	private ProducerListener listener;

	public MqttProducerListener(CountDownLatch latch, MqttTopic topic, Object message, ProducerListener listener)
	{
		this.latch = latch;
		this.topic = topic;
		this.message = message;
		this.listener = listener;
	}

	@Override
	public void onSuccess(MqttTopic topic, Object message)
	{
		latch.countDown();
		if (listener != null)
		{
			listener.onSuccess(topic, message);
		}
	}

	@Override
	public void onFailure(MqttTopic topic, Object message, Throwable cause)
	{
		latch.countDown();
		if (listener != null)
		{
			listener.onFailure(topic, message, cause);
		}
	}
}
