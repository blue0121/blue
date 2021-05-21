package blue.kafka.internal.core.producer;

import blue.base.core.message.ProducerListener;
import blue.base.core.util.AssertUtil;
import blue.base.core.util.WaitUtil;
import blue.base.internal.core.message.AbstractProducer;
import blue.kafka.core.KafkaProducer;
import blue.kafka.core.KafkaTopic;
import blue.kafka.core.options.KafkaProducerOptions;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-12
 */
public class DefaultKafkaProducer extends AbstractProducer<KafkaTopic> implements KafkaProducer {
	private static Logger logger = LoggerFactory.getLogger(DefaultKafkaProducer.class);

	private final org.apache.kafka.clients.producer.Producer<String, Object> producer;

	public DefaultKafkaProducer(KafkaProducerOptions options) {
		super(options);
		this.producer = new org.apache.kafka.clients.producer.KafkaProducer<>(options.getProp());
	}

	@Override
	public void sendSync(KafkaTopic topic, List<Object> messageList) {
		AssertUtil.notEmpty(messageList, "message list");
		CountDownLatch latch = new CountDownLatch(messageList.size());
		for (Object message : messageList) {
			ProducerRecord<String, Object> record = this.createProducerRecord(topic.getTopic(), topic.getKey(), message);
			Callback callback = new SyncCallback(latch, options.getProducerListener(), topic, message);
			producer.send(record, callback);
		}
		WaitUtil.await(latch);
	}

	@Override
	public void sendAsync(KafkaTopic topic, List<Object> messageList) {
		AssertUtil.notEmpty(messageList, "message list");
		for (Object message : messageList) {
			this.sendAsync(topic, message, null);
		}
	}

	@Override
	public void sendAsync(KafkaTopic topic, Object message, ProducerListener<KafkaTopic, Object> listener) {
		ProducerRecord<String, Object> record = this.createProducerRecord(topic.getTopic(), topic.getKey(), message);
		Callback callback = this.createCallback(record, listener);
		producer.send(record, callback);
	}

	@Override
	public void sendAsync(KafkaTopic topic, List<Object> messageList, ProducerListener<KafkaTopic, Object> listener) {
		AssertUtil.notEmpty(messageList, "message list");
		for (Object message : messageList) {
			this.sendAsync(topic, message, listener);
		}
	}

	public List<PartitionInfo> partitionsFor(String topic) {
		AssertUtil.notEmpty(topic, "topic");
		return producer.partitionsFor(topic);
	}

	public Map<MetricName, ? extends Metric> metrics() {
		return producer.metrics();
	}

	public void flush() {
		producer.flush();
	}

	private Callback createCallback(ProducerRecord<String, Object> record, ProducerListener<KafkaTopic, Object> listener) {
		List<ProducerListener<KafkaTopic, Object>> listenerList = new ArrayList<>();
		if (listener == null) {
			listener = options.getProducerListener();
		}

		Callback callback = new AsyncCallback(listener, record.topic(), record.partition(), record.key(), record.value());
		return callback;
	}

	private ProducerRecord<String, Object> createProducerRecord(String topic, String key, Object value) {
		AssertUtil.notEmpty(topic, "topic");
		AssertUtil.notNull(value, "value");
		ProducerRecord<String, Object> record = new ProducerRecord<>(topic, key, value);
		return record;
	}

	@Override
	public void disconnect() {
		if (producer != null) {
			producer.close();
		}
	}

}
