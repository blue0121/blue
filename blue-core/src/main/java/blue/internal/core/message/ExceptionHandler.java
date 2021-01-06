package blue.internal.core.message;

import blue.core.message.Topic;

/**
 * @author Jin Zheng
 * @since 1.0 2019-06-28
 */
public interface ExceptionHandler<T extends Topic, V>
{

	void onError(T topic, V message, Exception e);

}
