package test.redis.spring.consumer;

import blue.base.core.collection.ConcurrentSet;
import blue.base.core.message.Topic;
import blue.base.core.util.JsonUtil;
import blue.redis.core.RedisConsumerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.redis.spring.model.User;

import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-12
 */
public class RedisReceiver implements RedisConsumerListener<User> {
	private static Logger logger = LoggerFactory.getLogger(RedisReceiver.class);

	private ConcurrentSet<User> set = ConcurrentSet.create(null);

	public RedisReceiver() {
	}

	@Override
	public void onReceive(Topic topic, User message) {
		logger.info("onReceive, {}={}", topic, JsonUtil.output(message));
		set.add(message);
	}

	public Set<User> getMessage() {
		return set;
	}
}
