package blue.kafka.core.options;

import blue.base.core.message.ConsumerOptions;
import blue.kafka.core.KafkaException;
import blue.kafka.core.OffsetManager;
import blue.kafka.core.codec.FastjsonDeserializer;
import blue.kafka.internal.core.offset.MemoryOffsetManager;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Properties;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-20
 */
public class KafkaConsumerOptions extends ConsumerOptions {
	private static Logger logger = LoggerFactory.getLogger(KafkaConsumerOptions.class);

	public static final int MIN_COUNT = 1;
	public static final int MAX_COUNT = 100;
	public static final int DURATION = 1000;

	private Class<?> keyDeserializer;
	private Class<?> valueDeserializer;
	private int count = MIN_COUNT;
	private Duration duration;
	private String group;
	private OffsetManager offsetManager;
	private Properties prop = new Properties();

	public KafkaConsumerOptions() {
	}

	@Override
	public void check() {
		super.check();
		if (count < MIN_COUNT || count > MAX_COUNT) {
			throw new KafkaException("Kafka count must be between " + MIN_COUNT + " and " + MAX_COUNT);
		}
		if (duration == null) {
			this.duration = Duration.ofMillis(DURATION);
		}
		if (group != null && !group.isEmpty()) {
			prop.put(ConsumerConfig.GROUP_ID_CONFIG, group);
		}
		if (keyDeserializer != null) {
			prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
		}
		if (valueDeserializer != null) {
			prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
		}
		if (!prop.containsKey(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG)) {
			logger.info("KafkaConsumer '{}' config '{}' is empty, default: {}",
					id, ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
			prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		}
		if (!prop.containsKey(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG)) {
			logger.info("KafkaConsumer '{}' config '{}' is empty, default: {}",
					id, ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, FastjsonDeserializer.class.getName());
			prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, FastjsonDeserializer.class.getName());
		}
		if (offsetManager == null) {
			offsetManager = new MemoryOffsetManager();
			logger.info("KafkaConsumer '{}' OffsetManager is null，default: MemoryOffsetManager", id);
		}
	}

	public Class<?> getKeyDeserializer() {
		return keyDeserializer;
	}

	public KafkaConsumerOptions setKeyDeserializer(Class<?> keyDeserializer) {
		this.keyDeserializer = keyDeserializer;
		return this;
	}

	public Class<?> getValueDeserializer() {
		return valueDeserializer;
	}

	public KafkaConsumerOptions setValueDeserializer(Class<?> valueDeserializer) {
		this.valueDeserializer = valueDeserializer;
		return this;
	}

	public int getCount() {
		return count;
	}

	public KafkaConsumerOptions setCount(int count) {
		this.count = count;
		return this;
	}

	public Duration getDuration() {
		return duration;
	}

	public KafkaConsumerOptions setDuration(Duration duration) {
		this.duration = duration;
		return this;
	}

	public String getGroup() {
		return group;
	}

	public KafkaConsumerOptions setGroup(String group) {
		this.group = group;
		return this;
	}

	public Properties getProp() {
		return prop;
	}

	public KafkaConsumerOptions setProp(Properties prop) {
		if (prop == null || prop.isEmpty()) {
			return this;
		}
		this.prop.putAll(prop);
		return this;
	}

	public OffsetManager getOffsetManager() {
		return offsetManager;
	}

	public KafkaConsumerOptions setOffsetManager(OffsetManager offsetManager) {
		this.offsetManager = offsetManager;
		return this;
	}
}
