package test.kafka.listener;

import blue.core.util.JsonUtil;
import blue.internal.core.message.ProducerListener;
import blue.kafka.model.KafkaTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-07
 */
public class DefaultProducerListener implements ProducerListener<KafkaTopic, Object>
{
	private static Logger logger = LoggerFactory.getLogger(DefaultProducerListener.class);

	public DefaultProducerListener()
	{
	}

	@Override
	public void onSuccess(KafkaTopic topic, Object message)
	{
		logger.info("produce successful, topic: {}, message: {}", topic, JsonUtil.toString(message));
	}

	@Override
	public void onFailure(KafkaTopic topic, Object message, Throwable cause)
	{
		logger.warn("produce failure, topic: {}, message: {}", topic, JsonUtil.toString(message));
		logger.error("Error, ", cause);
	}
}
