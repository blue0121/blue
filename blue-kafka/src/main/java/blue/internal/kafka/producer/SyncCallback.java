package blue.internal.kafka.producer;

import blue.core.message.ProducerListener;
import blue.kafka.model.KafkaTopic;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.CountDownLatch;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-12
 */
public class SyncCallback implements Callback
{
	private CountDownLatch latch;
	private ProducerListener<KafkaTopic, Object> listener;
	private KafkaTopic topic;
	private Object message;

	public SyncCallback(CountDownLatch latch, ProducerListener<KafkaTopic, Object> listener, KafkaTopic topic, Object message)
	{
		this.latch = latch;
		this.listener = listener;
		this.topic = topic;
		this.message = message;
	}

	@Override
	public void onCompletion(RecordMetadata recordMetadata, Exception e)
	{
		latch.countDown();
		if (listener != null)
		{
			if (e != null)
			{
				listener.onFailure(topic, message, e);
			}
			else
			{
				listener.onSuccess(topic, message);
			}
		}
	}
}
