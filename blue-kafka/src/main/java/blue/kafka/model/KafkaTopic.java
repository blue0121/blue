package blue.kafka.model;

import blue.core.message.Topic;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-12
 */
public class KafkaTopic extends Topic
{
	private Integer partition;
	private String key;

	public KafkaTopic(String topic)
	{
		super(topic);
	}

	public KafkaTopic(String topic, Integer partition, String key)
	{
		super(topic);
		this.partition = partition;
		this.key = key;
	}

	public Integer getPartition()
	{
		return partition;
	}

	public void setPartition(Integer partition)
	{
		this.partition = partition;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	@Override
	public String toString()
	{
		return String.format("KafkaTopic[topic: %s, partition: %d, key: %s]", topic, partition, key);
	}

}
