package blue.kafka.core.options;

import blue.base.core.message.ProducerOptions;
import blue.kafka.core.codec.FastjsonSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-20
 */
public class KafkaProducerOptions extends ProducerOptions {
	private static Logger logger = LoggerFactory.getLogger(KafkaProducerOptions.class);

	private Class<?> keySerializer;
	private Class<?> valueSerializer;
	private Properties prop = new Properties();

	public KafkaProducerOptions() {
	}

	@Override
	public void check() {
		super.check();
		if (keySerializer != null) {
			prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
		}
		if (valueSerializer != null) {
			prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
		}
		if (!prop.containsKey(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG)) {
			logger.info("KafkaProducer '{}' config '{}' is empty, default: {}",
					id, ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
			prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		}
		if (!prop.containsKey(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG)) {
			logger.info("KafkaProducer '{}' config '{}' is empty, default: {}",
					id, ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, FastjsonSerializer.class.getName());
			prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, FastjsonSerializer.class.getName());
		}
	}

	public Properties getProp() {
		return prop;
	}

	public KafkaProducerOptions setProp(Properties prop) {
		if (prop == null || prop.isEmpty()) {
			return this;
		}
		this.prop.putAll(prop);
		return this;
	}

	public Class<?> getKeySerializer() {
		return keySerializer;
	}

	public KafkaProducerOptions setKeySerializer(Class<?> keySerializer) {
		this.keySerializer = keySerializer;
		return this;
	}

	public Class<?> getValueSerializer() {
		return valueSerializer;
	}

	public KafkaProducerOptions setValueSerializer(Class<?> valueSerializer) {
		this.valueSerializer = valueSerializer;
		return this;
	}
}
