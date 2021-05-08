package blue.mqtt.internal.core.client;

import org.fusesource.mqtt.client.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-04
 */
public class SubscribeCallback<T> implements Callback<T> {
	private static Logger logger = LoggerFactory.getLogger(SubscribeCallback.class);

	private final CountDownLatch latch;
	private final String topic;
	private final String clientId;
	private final ConnectionSet connectionSet;

	public SubscribeCallback(CountDownLatch latch, String topic, String clientId, ConnectionSet connectionSet) {
		this.latch = latch;
		this.topic = topic;
		this.clientId = clientId;
		this.connectionSet = connectionSet;
	}

	@Override
	public void onSuccess(T param) {
		latch.countDown();
		connectionSet.subscribe(topic, clientId);
		logger.info("Subscribe successful, topic: {}, clientId: {}", topic, clientId);
	}

	@Override
	public void onFailure(Throwable cause) {
		latch.countDown();
		logger.error("MQTT error, ", cause);
	}
}
