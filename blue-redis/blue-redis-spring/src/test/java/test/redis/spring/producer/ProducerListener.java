package test.redis.spring.producer;

import blue.base.core.message.Topic;
import blue.core.util.JsonUtil;
import blue.redis.core.RedisProducerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2019-11-03
 */
public class ProducerListener implements RedisProducerListener<Object> {
	private static Logger logger = LoggerFactory.getLogger(ProducerListener.class);

	public ProducerListener() {
	}

	@Override
	public void onSuccess(Topic topic, Object message) {
		logger.info("produce successful, topic: {}, message: {}", topic, JsonUtil.toString(message));
	}

	@Override
	public void onFailure(Topic topic, Object message, Throwable cause)
	{
		logger.warn("produce failure, topic: {}, message: {}", topic, JsonUtil.toString(message));
		logger.error("Error, ", cause);
	}
}
