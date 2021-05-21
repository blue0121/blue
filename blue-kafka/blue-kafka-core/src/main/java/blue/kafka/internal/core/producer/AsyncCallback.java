package blue.kafka.internal.core.producer;

import blue.base.core.message.ProducerListener;
import blue.kafka.core.KafkaTopic;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-12
 */
public class AsyncCallback implements Callback {
	private ProducerListener<KafkaTopic, Object> listener;
	private String topic;
	private Integer partition;
	private String key;
	private Object value;

	public AsyncCallback(ProducerListener<KafkaTopic, Object> listener, String topic,
	                     Integer partition, String key, Object value) {
		this.listener = listener;
		this.topic = topic;
		this.partition = partition;
		this.key = key;
		this.value = value;
	}

	@Override
	public void onCompletion(RecordMetadata metadata, Exception exception) {
		if (listener == null) {
			return;
		}

		if (metadata != null) {
			topic = metadata.topic();
			partition = metadata.partition();
		}
		KafkaTopic kafkaTopic = new KafkaTopic(topic, partition, key);
		if (exception != null) {
			listener.onFailure(kafkaTopic, value, exception);
		}
		else {
			listener.onSuccess(kafkaTopic, value);
		}
	}
}
