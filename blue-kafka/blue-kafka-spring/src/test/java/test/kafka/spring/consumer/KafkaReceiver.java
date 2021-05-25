package test.kafka.spring.consumer;

import blue.base.core.collection.ConcurrentSet;
import blue.base.core.util.JsonUtil;
import blue.kafka.core.KafkaConsumerListener;
import blue.kafka.core.KafkaTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.kafka.spring.model.User;

import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-21
 */
public class KafkaReceiver implements KafkaConsumerListener<User> {
	private static Logger logger = LoggerFactory.getLogger(KafkaReceiver.class);

	private ConcurrentSet<User> set = ConcurrentSet.create(null);

	public KafkaReceiver() {
	}

	@Override
	public void onReceive(KafkaTopic topic, User message) {
		logger.info("Receive: {}={}", topic, JsonUtil.output(message));
		set.add(message);
	}

	public Set<User> getMessage() {
		return set;
	}
}
