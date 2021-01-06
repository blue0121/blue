package blue.internal.core.message;

import blue.core.message.Topic;

import java.util.Collection;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2021-01-01
 */
public interface Consumer<T extends Topic>
{

	default void subscribe(T topic, ConsumerListener<T, ?> listener)
	{
		this.subscribe(List.of(topic), listener);
	}

	void subscribe(Collection<T> topicList, ConsumerListener<T, ?> listener);

	default void unsubscribe(String topic)
	{
		this.unsubscribe(List.of(topic));
	}

	void unsubscribe(Collection<String> topicList);

}
