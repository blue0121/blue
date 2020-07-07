package test.kafka.listener;

import blue.core.message.ConsumerListener;
import blue.core.util.JsonUtil;
import blue.kafka.model.KafkaTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.kafka.model.User;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-07
 */
public class DefaultKafkaListener implements ConsumerListener<KafkaTopic, User>
{
	private static Logger logger = LoggerFactory.getLogger(DefaultKafkaListener.class);

	public DefaultKafkaListener()
	{
	}

	@Override
	public void onReceive(KafkaTopic topic, User message)
	{
		logger.info("onReceive, {}, message: {}", topic, JsonUtil.toString(message));
	}
}
