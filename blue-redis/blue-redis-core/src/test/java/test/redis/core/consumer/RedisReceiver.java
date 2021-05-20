package test.redis.core.consumer;

import blue.base.core.collection.ConcurrentSet;
import blue.base.core.message.Topic;
import blue.redis.core.RedisConsumerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-12
 */
public class RedisReceiver implements RedisConsumerListener<String> {
	private static Logger logger = LoggerFactory.getLogger(RedisReceiver.class);

	private ConcurrentSet<String> set = ConcurrentSet.create(null);

	public RedisReceiver() {
	}

	@Override
	public void onReceive(Topic topic, String message) {
		logger.info("onReceive, {}={}", topic, message);
		set.add(message);
	}

	public Set<String> getMessage() {
		return set;
	}
}
