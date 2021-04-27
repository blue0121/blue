package blue.base.internal.core.message;

import blue.base.core.message.ExceptionHandler;
import blue.base.core.message.Topic;
import blue.base.core.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2019-06-29
 */
public class LoggerExceptionHandler<T extends Topic, V> implements ExceptionHandler<T, V> {
	private static Logger logger = LoggerFactory.getLogger(LoggerExceptionHandler.class);

	public LoggerExceptionHandler() {
	}

	@Override
	public void onError(T topic, V message, Exception e) {
		String json = JsonUtil.output(message);
		logger.error("Error, {}, value: {}", topic, json);
		logger.error("Error: ", e);
	}

}
