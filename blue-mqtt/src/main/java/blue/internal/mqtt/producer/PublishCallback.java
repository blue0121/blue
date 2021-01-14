package blue.internal.mqtt.producer;

import blue.internal.core.message.ProducerListener;
import blue.mqtt.MqttTopic;
import org.fusesource.mqtt.client.Callback;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-14
 */
public class PublishCallback implements Callback<Void>
{
	private MqttTopic topic;
	private Object message;
	private ProducerListener<MqttTopic, Object> listener;

	public PublishCallback(MqttTopic topic, Object message, ProducerListener<MqttTopic, Object> listener)
	{
		this.topic = topic;
		this.message = message;
		this.listener = listener;
	}

	@Override
	public void onSuccess(Void unused)
	{
		if (listener != null)
		{
			listener.onSuccess(topic, message);
		}
	}

	@Override
	public void onFailure(Throwable cause)
	{
		if (listener != null)
		{
			listener.onFailure(topic, message, cause);
		}
	}
}
