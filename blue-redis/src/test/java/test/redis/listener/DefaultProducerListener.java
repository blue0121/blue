package test.redis.listener;

import blue.core.message.ProducerListener;
import blue.core.message.Topic;
import blue.core.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2019-11-03
 */
public class DefaultProducerListener implements ProducerListener<Topic, Object>
{
	private static Logger logger = LoggerFactory.getLogger(DefaultProducerListener.class);

	public DefaultProducerListener()
	{
	}

	@Override
	public void onSuccess(Topic topic, Object message)
	{
		logger.info("produce successful, topic: {}, message: {}", topic, JsonUtil.toString(message));
	}

	@Override
	public void onFailure(Topic topic, Object message, Throwable cause)
	{
		logger.warn("produce failure, topic: {}, message: {}", topic, JsonUtil.toString(message));
		logger.error("Error, ", cause);
	}
}
