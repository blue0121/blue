package blue.kafka.internal.core.consumer;

import blue.base.core.message.ConsumerListener;
import blue.base.core.util.AssertUtil;
import blue.base.internal.core.message.AbstractConsumer;
import blue.kafka.core.KafkaConsumer;
import blue.kafka.core.KafkaTopic;
import blue.kafka.core.options.KafkaConsumerOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-07
 */
public class DefaultKafkaConsumer extends AbstractConsumer<KafkaTopic> implements KafkaConsumer {
	private static Logger logger = LoggerFactory.getLogger(DefaultKafkaConsumer.class);

	private final KafkaConsumerOptions options;
	private final Properties prop;
	private final Map<String, List<ConsumerRunnable>> consumerMap = new ConcurrentHashMap<>();

	public DefaultKafkaConsumer(KafkaConsumerOptions options) {
		super(options);
		this.options = options;
		this.prop = options.getProp();
	}

	@Override
	public void subscribe(Collection<KafkaTopic> topicList, ConsumerListener<KafkaTopic, ?> listener) {
		AssertUtil.notEmpty(topicList, "Topic list");
		AssertUtil.notNull(listener, "ConsumerListener");
		List<KafkaListenerConfig> configList = new ArrayList<>();
		for (var topic : topicList) {
			var config = new KafkaListenerConfig();
			config.setTopic(topic.getTopic());
			config.setGroup(options.getGroup());
			config.setCount(options.getCount());
			config.setDuration(options.getDuration());
			config.setMultiThread(options.isMultiThread());
			config.setListener(listener);
			config.setExceptionHandler(options.getExceptionHandler());
			config.setExecutor(options.getExecutor());
			config.setOffsetManager(options.getOffsetManager());
			config.init();
			configList.add(config);
		}
		this.subscribe(configList);
	}

	protected void subscribe(List<KafkaListenerConfig> configList) {
		for (KafkaListenerConfig config : configList) {
			for (int i = 0; i < config.getCount(); i++) {
				ConsumerRunnable consumer = new ConsumerThread(this.prop, config);
				List<ConsumerRunnable> consumerList = consumerMap.computeIfAbsent(config.getTopic(), k -> new ArrayList<>());
				consumerList.add(consumer);
				Thread thread = new Thread(consumer);
				thread.start();
			}
		}
	}

	@Override
	public void unsubscribe(Collection<String> topicList) {
		AssertUtil.notEmpty(topicList, "Topic list");
		for (String topic : topicList) {
			List<ConsumerRunnable> consumerList = consumerMap.remove(topic);
			if (consumerList != null && !consumerList.isEmpty()) {
				consumerList.forEach(e -> e.destroy());
			}
		}
	}

	@Override
	public void disconnect() {
		for (var entry : consumerMap.entrySet()) {
			if (entry.getValue() != null && !entry.getValue().isEmpty()) {
				entry.getValue().forEach(e -> e.destroy());
			}
		}
		logger.info("KafkaConsumer '{}' disconnect", options.getId());
	}

}
