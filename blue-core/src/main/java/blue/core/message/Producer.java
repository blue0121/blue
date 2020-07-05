package blue.core.message;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 2019-06-29
 */
public interface Producer<T extends Topic>
{

	default void sendSync(T topic, Object message)
	{
		this.sendSync(topic, List.of(message));
	}

	void sendSync(T topic, List<Object> messageList);

	default void sendAsync(T topic, Object message)
	{
		this.sendAsync(topic, List.of(message), null);
	}

	default void sendAsync(T topic, List<Object> messageList)
	{
		this.sendAsync(topic, messageList, null);
	}

	default void sendAsync(T topic, Object message, ProducerListener<T, Object> listener)
	{
		this.sendAsync(topic, List.of(message), listener);
	}

	void sendAsync(T topic, List<Object> messageList, ProducerListener<T, Object> listener);

	void setProducerListener(ProducerListener<T, Object> listener);

}
