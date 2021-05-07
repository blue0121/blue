package blue.mqtt.internal.core.client;

import org.fusesource.mqtt.client.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-04
 */
public class SubscribeCallback<T> implements Callback<T> {
	private static Logger logger = LoggerFactory.getLogger(SubscribeCallback.class);

	private final CountDownLatch latch;
	private final String topic;
	private final ConcurrentMap<String, String> topicClientIdMap;
	private final String clientId;

	public SubscribeCallback(CountDownLatch latch, String topic,
	                         ConcurrentMap<String, String> topicClientIdMap, String clientId) {
		this.latch = latch;
		this.topic = topic;
		this.topicClientIdMap = topicClientIdMap;
		this.clientId = clientId;
	}

	@Override
	public void onSuccess(T param) {
		latch.countDown();
		topicClientIdMap.put(topic, clientId);
		logger.info("Subscribe successful, topic: {}, clientId: {}", topic, clientId);
	}

	@Override
	public void onFailure(Throwable cause) {
		latch.countDown();
		logger.error("MQTT error, ", cause);
	}
}
