package blue.redis.internal.core.consumer;

import blue.base.core.message.ConsumerListener;
import blue.base.core.message.ConsumerOptions;
import blue.base.core.message.Topic;
import blue.base.core.util.AssertUtil;
import blue.base.internal.core.message.AbstractConsumer;
import blue.base.internal.core.message.ConsumerListenerConfig;
import blue.redis.core.RedisConsumer;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-08
 */
public class DefaultRedisConsumer extends AbstractConsumer<Topic> implements RedisConsumer {
	private static Logger logger = LoggerFactory.getLogger(DefaultRedisConsumer.class);
	private final RedissonClient redisson;

	public DefaultRedisConsumer(ConsumerOptions options, RedissonClient redisson) {
		super(options);
		this.redisson = redisson;
	}

	private void subscribe(List<ConsumerListenerConfig> configList) {
		if (configList == null || configList.isEmpty()) {
			return;
		}
		for (ConsumerListenerConfig config : configList) {
			RedissonMessageListener listener = new RedissonMessageListener(config);
			redisson.getTopic(config.getTopic()).addListener(config.getClazz(), listener);
			logger.info("RedisConsumer '{}' subscribe, topic: {}", options.getId(), config.getTopic());
		}
	}

	@Override
	public void subscribe(Collection<Topic> topicList, ConsumerListener<Topic, ?> listener) {
		AssertUtil.notEmpty(topicList, "Topic list");
		AssertUtil.notNull(listener, "ConsumerListener");
		List<ConsumerListenerConfig> configList = new ArrayList<>();
		for (Topic topic : topicList) {
			ConsumerListenerConfig config = new ConsumerListenerConfig();
			config.setTopic(topic.getTopic());
			config.setListener(listener);
			config.setMultiThread(options.isMultiThread());
			config.setExecutor(options.getExecutor());
			config.setExceptionHandler(options.getExceptionHandler());
			config.init();
			configList.add(config);
		}
		this.subscribe(configList);
	}

	@Override
	public void unsubscribe(Collection<String> topicList) {
		AssertUtil.notEmpty(topicList, "Topic list");
		for (String topic : topicList) {
			redisson.getTopic(topic).removeAllListeners();
			logger.info("RedisConsumer '{}' unsubscribe, topic: {}", options.getId(), topic);
		}
	}

}
