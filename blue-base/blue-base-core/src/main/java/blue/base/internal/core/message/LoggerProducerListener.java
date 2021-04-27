package blue.base.internal.core.message;

import blue.base.core.message.ProducerListener;
import blue.base.core.message.Topic;
import blue.base.core.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2020-02-10
 */
public class LoggerProducerListener<T extends Topic, V> implements ProducerListener<T, V> {
	private static Logger logger = LoggerFactory.getLogger(LoggerProducerListener.class);

	public LoggerProducerListener() {
	}

	@Override
	public void onSuccess(T topic, V message) {
		if (logger.isDebugEnabled()) {
			String json = JsonUtil.output(message);
			logger.debug("Success, {}, value: {}", topic, json);
		}
	}

	@Override
	public void onFailure(T topic, V message, Throwable cause) {
		String json = JsonUtil.output(message);
		logger.error("Error, {}, value: {}", topic, json);
		logger.error("Error: ", cause);
	}
}
