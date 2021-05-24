package blue.redis.internal.core.producer;

import blue.base.core.message.ProducerListener;
import blue.base.core.message.ProducerOptions;
import blue.base.core.message.Topic;
import blue.base.core.util.AssertUtil;
import blue.base.core.util.WaitUtil;
import blue.base.internal.core.message.AbstractProducer;
import blue.redis.core.RedisProducer;
import org.redisson.api.RFuture;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author Jin Zheng
 * @since 2019-11-03
 */
public class DefaultRedisProducer extends AbstractProducer<Topic> implements RedisProducer {
	private static Logger logger = LoggerFactory.getLogger(DefaultRedisProducer.class);

	private final RedissonClient redisson;

	public DefaultRedisProducer(ProducerOptions options, RedissonClient redisson) {
		super(options);
		this.redisson = redisson;
	}

	@Override
	public void sendSync(Topic topic, List<Object> messageList) {
		AssertUtil.notNull(topic, "Topic");
		AssertUtil.notEmpty(messageList, "Message list");
		RTopic rtopic = redisson.getTopic(topic.getTopic());
		List<ProducerListener<Topic, Object>> listenerList = this.getProducerListener(null);
		CountDownLatch latch = new CountDownLatch(messageList.size());
		for (Object message : messageList) {
			RFuture<Long> future = rtopic.publishAsync(message);
			future.onComplete(new DefaultCallback(latch, topic, message, listenerList));
		}
		WaitUtil.await(latch);
	}

	@Override
	public void sendAsync(Topic topic, List<Object> messageList, ProducerListener<Topic, Object> listener) {
		AssertUtil.notNull(topic, "Topic");
		AssertUtil.notEmpty(messageList, "Message list");
		List<ProducerListener<Topic, Object>> listenerList = this.getProducerListener(listener);
		RTopic rtopic = redisson.getTopic(topic.getTopic());
		for (Object message : messageList) {
			RFuture<Long> future = rtopic.publishAsync(message);
			future.onComplete(new DefaultCallback(null, topic, message, listenerList));
		}
	}

	private List<ProducerListener<Topic, Object>> getProducerListener(ProducerListener<Topic, Object> listener) {
		List<ProducerListener<Topic, Object>> listenerList = new ArrayList<>();
		if (listener != null) {
			listenerList.add(listener);
		}
		if (options.getProducerListener() != null) {
			listenerList.add(options.getProducerListener());
		}
		return listenerList;
	}

}
