package blue.internal.kafka.producer;

import blue.core.message.ProducerListener;
import blue.core.util.AssertUtil;
import blue.core.util.WaitUtil;
import blue.internal.core.message.LoggerProducerListener;
import blue.internal.kafka.codec.FastjsonSerializer;
import blue.kafka.model.KafkaTopic;
import blue.kafka.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-12
 */
public class DefaultKafkaProducer implements KafkaProducer, InitializingBean, DisposableBean
{
	private static Logger logger = LoggerFactory.getLogger(DefaultKafkaProducer.class);

	private org.apache.kafka.clients.producer.Producer<String, Object> producer;
	private Properties config;
	private ProducerListener<KafkaTopic, Object> defaultListener;

	public DefaultKafkaProducer()
	{
	}

	@Override
	public void sendSync(KafkaTopic topic, List<Object> messageList)
	{
		AssertUtil.notEmpty(messageList, "message list");
		CountDownLatch latch = new CountDownLatch(messageList.size());
		for (Object message : messageList)
		{
			ProducerRecord<String, Object> record = this.createProducerRecord(topic.getTopic(), topic.getKey(), message);
			Callback callback = new SyncCallback(latch, defaultListener, topic, message);
			producer.send(record, callback);
		}
		WaitUtil.await(latch);
	}

	@Override
	public void sendAsync(KafkaTopic topic, Object message)
	{
		this.sendAsync(topic, message, null);
	}

	@Override
	public void sendAsync(KafkaTopic topic, List<Object> messageList)
	{
		AssertUtil.notEmpty(messageList, "message list");
		for (Object message : messageList)
		{
			this.sendAsync(topic, message, null);
		}
	}

	@Override
	public void sendAsync(KafkaTopic topic, Object message, ProducerListener<KafkaTopic, Object> listener)
	{
		ProducerRecord<String, Object> record = this.createProducerRecord(topic.getTopic(), topic.getKey(), message);
		Callback callback = this.createCallback(record, listener);
		producer.send(record, callback);
	}

	@Override
	public void sendAsync(KafkaTopic topic, List<Object> messageList, ProducerListener<KafkaTopic, Object> listener)
	{
		AssertUtil.notEmpty(messageList, "message list");
		for (Object message : messageList)
		{
			this.sendAsync(topic, message, listener);
		}
	}

	public List<PartitionInfo> partitionsFor(String topic)
	{
		AssertUtil.notEmpty(topic, "topic");
		return producer.partitionsFor(topic);
	}

	public Map<MetricName, ? extends Metric> metrics()
	{
		return producer.metrics();
	}

	public void flush()
	{
		producer.flush();
	}

	private Callback createCallback(ProducerRecord<String, Object> record, ProducerListener<KafkaTopic, Object> listener)
	{
		List<ProducerListener<KafkaTopic, Object>> listenerList = new ArrayList<>();
		if (listener == null)
		{
			listener = defaultListener;
		}

		Callback callback = new AsyncCallback(listener, record.topic(), record.partition(), record.key(), record.value());
		return callback;
	}

	private ProducerRecord<String, Object> createProducerRecord(String topic, String key, Object value)
	{
		AssertUtil.notEmpty(topic, "topic");
		AssertUtil.notNull(value, "value");
		ProducerRecord<String, Object> record = new ProducerRecord<>(topic, key, value);
		return record;
	}

	@Override
	public void setProducerListener(ProducerListener<KafkaTopic, Object> listener)
	{
		this.defaultListener = listener;
	}

	@Override
	public void destroy() throws Exception
	{
		if (producer != null)
		{
			producer.close();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		AssertUtil.notNull(config, "kafka config");

		if (!config.containsKey(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG)) {
			logger.info("config '{}' is empty, default: {}", ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
			config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		}
		if (!config.containsKey(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG)) {
			logger.info("config '{}' is empty, default: {}", ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, FastjsonSerializer.class.getName());
			config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, FastjsonSerializer.class.getName());
		}
		producer = new org.apache.kafka.clients.producer.KafkaProducer<>(config);

		if (defaultListener == null)
		{
			defaultListener = new LoggerProducerListener<>();
			logger.info("Default ProducerListener is empty, use LoggerProducerListener");
		}
	}

	public void setConfig(Properties config)
	{
		this.config = config;
	}
}
