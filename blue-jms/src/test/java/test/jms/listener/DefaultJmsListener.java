package test.jms.listener;

import blue.core.util.JsonUtil;
import blue.internal.core.message.ConsumerListener;
import blue.jms.model.JmsTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.jms.model.User;

/**
 * @author Jin Zheng
 * @since 2019-06-30
 */
public class DefaultJmsListener implements ConsumerListener<JmsTopic, User>
{
	private static Logger logger = LoggerFactory.getLogger(DefaultJmsListener.class);

	@Override
	public void onReceive(JmsTopic topic, User message)
	{
		logger.info("onReceive, topic: {}, type: {}, message: {}", topic.getTopic(), topic.getType(), JsonUtil.toString(message));
	}
}
