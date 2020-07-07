package blue.internal.core.message;

import blue.core.message.ProducerListener;
import blue.core.message.Topic;
import blue.core.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2020-02-10
 */
public class LoggerProducerListener<T extends Topic, V> implements ProducerListener<T, V>
{
	private static Logger logger = LoggerFactory.getLogger(LoggerProducerListener.class);

	public LoggerProducerListener()
	{
	}

	@Override
	public void onSuccess(T topic, V message)
	{
		String json = message instanceof CharSequence ? message.toString() : JsonUtil.output(message);
		logger.info("Success, {}, value: {}", topic, json);
	}

	@Override
	public void onFailure(T topic, V message, Throwable cause)
	{
		String json = message instanceof CharSequence ? message.toString() : JsonUtil.output(message);
		logger.error("Error, {}, value: {}", topic, json);
		logger.error("Error: ", cause);
	}
}
