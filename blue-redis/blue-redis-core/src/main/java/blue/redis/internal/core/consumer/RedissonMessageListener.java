package blue.redis.internal.core.consumer;

import blue.base.core.message.Topic;
import blue.base.internal.core.message.ConsumerListenerConfig;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2019-04-06
 */
public class RedissonMessageListener<T> implements MessageListener<T> {
	private static Logger logger = LoggerFactory.getLogger(RedissonMessageListener.class);
	private final ConsumerListenerConfig config;

	public RedissonMessageListener(ConsumerListenerConfig config) {
		this.config = config;
	}

	@Override
	public void onMessage(CharSequence channel, T msg) {
		final String topic = channel.toString();
		logger.debug("onMessage: {}", topic);

		if (config.isMultiThread()) {
			config.getExecutor().execute(() -> this.invoke(topic, msg));
		}
		else {
			this.invoke(topic, msg);
		}
	}

	@SuppressWarnings("unchecked")
	private void invoke(String topic, T msg) {
		try {
			config.getListener().onReceive(new Topic(topic), msg);
		}
		catch (Exception e) {
			config.getExceptionHandler().onError(new Topic(topic), msg, e);
		}
	}
}
