package test.redis.listener;

import blue.core.message.Topic;
import blue.core.util.JsonUtil;
import blue.internal.core.message.ConsumerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.redis.model.User;

/**
 * @author Jin Zheng
 * @since 2019-11-03
 */
public class DefaultListener implements ConsumerListener<Topic, User>
{
	private static Logger logger = LoggerFactory.getLogger(DefaultListener.class);

	public DefaultListener()
	{
	}

	@Override
	public void onReceive(Topic topic, User message)
	{
		logger.info("onReceive, topic: {}, message:{}", topic.getTopic(), JsonUtil.toString(message));
	}
}
