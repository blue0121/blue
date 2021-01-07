package test.jms.listener;

import blue.core.util.JsonUtil;
import blue.jms.JmsConsumerListener;
import blue.jms.JmsTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.jms.model.User;

/**
 * @author Jin Zheng
 * @since 2019-06-30
 */
public class DefaultJmsListener implements JmsConsumerListener<User>
{
	private static Logger logger = LoggerFactory.getLogger(DefaultJmsListener.class);

	@Override
	public void onReceive(JmsTopic topic, User message)
	{
		logger.info("onReceive, topic: {}, type: {}, message: {}", topic.getTopic(), topic.getType(), JsonUtil.toString(message));
	}
}
