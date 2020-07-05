package blue.internal.redis.consumer;

import blue.core.message.Topic;
import blue.internal.core.message.ConsumerListenerConfig;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2019-04-06
 */
public class MessageListenerImpl<T> implements MessageListener<T>
{
	private static Logger logger = LoggerFactory.getLogger(MessageListenerImpl.class);
	private final ConsumerListenerConfig config;

	public MessageListenerImpl(ConsumerListenerConfig config)
	{
		this.config = config;
	}

	@Override
	public void onMessage(CharSequence channel, T msg)
	{
		final String topic = channel.toString();
		logger.info("onMessage: {}", topic);

		if (config.isMultiThread())
		{
			config.getTaskExecutor().execute(() ->
			{
				this.invoke(topic, msg);
			});
		}
		else
		{
			this.invoke(topic, msg);
		}
	}

	private void invoke(String topic, T msg)
	{
		try
		{
			config.getListener().onReceive(new Topic(topic), msg);
		}
		catch (Exception e)
		{
			config.getExceptionHandler().onError(new Topic(topic), msg, e);
		}
	}
}
