package test.mqtt.listener;

import blue.core.message.ConsumerListener;
import blue.core.util.JsonUtil;
import blue.mqtt.model.MqttTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.mqtt.model.User;

/**
 * @author Jin Zheng
 * @since 2019-06-30
 */
public class DefaultMqttListener implements ConsumerListener<MqttTopic, User>
{
	private static Logger logger = LoggerFactory.getLogger(DefaultMqttListener.class);

	@Override
	public void onReceive(MqttTopic topic, User message)
	{
		logger.info("onReceive, topic: {}, qos: {}, message: {}", topic.getTopic(), topic.getQos(), JsonUtil.toString(message));
	}
}
