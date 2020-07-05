package blue.internal.core.message;

import blue.core.message.ExceptionHandler;
import blue.core.message.Topic;
import blue.core.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2019-06-29
 */
public class LoggerExceptionHandler<V> implements ExceptionHandler<Topic, V>
{
	private static Logger logger = LoggerFactory.getLogger(LoggerExceptionHandler.class);

	public LoggerExceptionHandler()
	{
	}

	@Override
	public void onError(Topic topic, V message, Exception e)
	{
		String json = message instanceof CharSequence ? message.toString() : JsonUtil.output(message);
		logger.error("Error, topic: {}, value: {}", topic, json);
		logger.error("Error: ", e);
	}

}