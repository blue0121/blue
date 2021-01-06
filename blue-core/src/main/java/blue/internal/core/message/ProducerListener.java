package blue.internal.core.message;

import blue.core.message.Topic;

/**
 * @author Jin Zheng
 * @since 2019-06-29
 */
public interface ProducerListener<T extends Topic, V>
{

	void onSuccess(T topic, V message);

	void onFailure(T topic, V message, Throwable cause);

}
