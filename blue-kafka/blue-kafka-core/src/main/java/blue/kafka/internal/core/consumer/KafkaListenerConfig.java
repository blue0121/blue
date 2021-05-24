package blue.kafka.internal.core.consumer;

import blue.base.internal.core.message.ConsumerListenerConfig;
import blue.kafka.core.KafkaException;
import blue.kafka.core.options.KafkaConsumerOptions;
import blue.kafka.internal.core.offset.OffsetManager;

import java.time.Duration;
import java.util.regex.Pattern;

/**
 * Kafka 监听器配置
 *
 * @author Jin Zheng
 * @since 1.0 2019-04-10
 */
public class KafkaListenerConfig extends ConsumerListenerConfig {
	private Pattern topicPattern;
	private int count;
	private String group;
	private Duration duration;
	private OffsetManager offsetManager;

	public KafkaListenerConfig() {
	}

	@Override
	public void init() {
		super.init();

		if (count < KafkaConsumerOptions.MIN_COUNT || count > KafkaConsumerOptions.MAX_COUNT) {
			throw new KafkaException("Kafka count must be between " + KafkaConsumerOptions.MIN_COUNT
					+ " and " + KafkaConsumerOptions.MAX_COUNT);
		}
		if (duration == null) {
			this.duration = Duration.ofMillis(KafkaConsumerOptions.DURATION);
		}

		topicPattern = Pattern.compile(topic);
	}

	@Override
	public String toString() {
		return String.format("topic: %s, count: %d, group: %s, duration: %dms, value-class: %s, multi-thread: %s",
				topic, count, group, duration, clazz.getName(), multiThread);
	}

	public Pattern getTopicPattern() {
		return topicPattern;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public OffsetManager getOffsetManager() {
		return offsetManager;
	}

	public void setOffsetManager(OffsetManager offsetManager) {
		this.offsetManager = offsetManager;
	}
}
