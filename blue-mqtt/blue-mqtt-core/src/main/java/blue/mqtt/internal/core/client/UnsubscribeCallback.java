package blue.mqtt.internal.core.client;

import org.fusesource.mqtt.client.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-04
 */
public class UnsubscribeCallback<T> implements Callback<T> {
	private static Logger logger = LoggerFactory.getLogger(UnsubscribeCallback.class);

	private final CountDownLatch latch;
	private final String topic;
	private final ConnectionSet connectionSet;

	public UnsubscribeCallback(CountDownLatch latch, String topic, ConnectionSet connectionSet) {
		this.latch = latch;
		this.topic = topic;
		this.connectionSet = connectionSet;
	}

	@Override
	public void onSuccess(T param) {
		latch.countDown();
		connectionSet.unsubscribe(topic);
		logger.info("Subscribe successful, topic: {}", topic);
	}

	@Override
	public void onFailure(Throwable cause) {
		latch.countDown();
		logger.error("MQTT error, ", cause);
	}
}
