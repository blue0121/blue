package blue.jms;

import blue.core.message.Topic;

import java.util.Objects;

/**
 * @author Jin Zheng
 * @since 1.0 2019-08-02
 */
public class JmsTopic extends Topic
{
	private final JmsType type;

	public JmsTopic(String topic, JmsType type)
	{
		super(topic);
		this.type = type;
	}

	public JmsType getType()
	{
		return type;
	}

	@Override
	public String toString()
	{
		return String.format("JmsTopic[topic: %s, type: %s]", topic, type);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		JmsTopic jmsTopic = (JmsTopic) o;
		return topic.equals(jmsTopic.topic);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(topic);
	}

}
