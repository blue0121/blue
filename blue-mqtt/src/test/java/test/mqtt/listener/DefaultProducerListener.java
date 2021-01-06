package test.mqtt.listener;

import blue.core.util.JsonUtil;
import blue.mqtt.MqttProducerListener;
import blue.mqtt.MqttTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-30
 */
public class DefaultProducerListener implements MqttProducerListener<Object>
{
	private static Logger logger = LoggerFactory.getLogger(DefaultProducerListener.class);

	public DefaultProducerListener()
	{
	}

	@Override
	public void onSuccess(MqttTopic topic, Object message)
	{
		logger.info("produce successful, topic: {}, message: {}", topic, JsonUtil.toString(message));
	}

	@Override
	public void onFailure(MqttTopic topic, Object message, Throwable cause)
	{
		logger.warn("produce failure, topic: {}, message: {}", topic, JsonUtil.toString(message));
		logger.error("Error, ", cause);
	}
}
