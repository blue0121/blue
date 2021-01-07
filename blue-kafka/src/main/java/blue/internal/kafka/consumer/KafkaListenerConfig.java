package blue.internal.kafka.consumer;

import blue.internal.core.message.ConsumerListenerConfig;
import blue.internal.kafka.offset.OffsetManager;
import blue.kafka.exception.KafkaException;

import java.util.regex.Pattern;

/**
 * Kafka 监听器配置
 *
 * @author Jin Zheng
 * @since 1.0 2019-04-10
 */
public class KafkaListenerConfig extends ConsumerListenerConfig
{
	private Pattern topicPattern;
	private int count = 1;
	private String group;
	private int duration = 1000;
	private OffsetManager offsetManager;

	public KafkaListenerConfig()
	{
	}

	@Override
	public void afterPropertiesSet()
	{
		super.afterPropertiesSet();

		if (count < 1)
			throw new KafkaException(topic + " Consumer size less than 1");

		topicPattern = Pattern.compile(topic);
	}

	@Override
	public String toString()
	{
		return String.format("topic: %s, count: %d, group: %s, duration: %dms, value-class: %s, multi-thread: %s",
				topic, count, group, duration, clazz.getName(), multiThread);
	}

	public Pattern getTopicPattern()
	{
		return topicPattern;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public String getGroup()
	{
		return group;
	}

	public void setGroup(String group)
	{
		this.group = group;
	}

	public int getDuration()
	{
		return duration;
	}

	public void setDuration(int duration)
	{
		this.duration = duration;
	}

	public OffsetManager getOffsetManager()
	{
		return offsetManager;
	}

	public void setOffsetManager(OffsetManager offsetManager)
	{
		this.offsetManager = offsetManager;
	}
}
