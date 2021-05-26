package test.blue.jms.consumer;

import blue.base.core.collection.ConcurrentSet;
import blue.base.core.util.JsonUtil;
import blue.jms.core.JmsConsumerListener;
import blue.jms.core.JmsTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.blue.jms.model.User;

import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-21
 */
public class JmsReceiver implements JmsConsumerListener<User> {
	private static Logger logger = LoggerFactory.getLogger(JmsReceiver.class);

	private ConcurrentSet<User> set = ConcurrentSet.create(null);

	public JmsReceiver() {
	}

	@Override
	public void onReceive(JmsTopic topic, User message) {
		logger.info("Receive: {}={}", topic, JsonUtil.output(message));
		set.add(message);
	}

	public Set<User> getMessage() {
		return set;
	}
}
