package blue.mqtt.internal.core.producer;

import blue.base.core.message.ProducerListener;
import blue.mqtt.core.MqttTopic;

import java.util.concurrent.CountDownLatch;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-13
 */
public class MqttProducerListener implements ProducerListener<MqttTopic, Object> {
	private final CountDownLatch latch;
	private final MqttTopic topic;
	private final Object message;
	private final ProducerListener listener;

	public MqttProducerListener(CountDownLatch latch, MqttTopic topic, Object message, ProducerListener listener) {
		this.latch = latch;
		this.topic = topic;
		this.message = message;
		this.listener = listener;
	}

	@Override
	public void onSuccess(MqttTopic topic, Object message) {
		latch.countDown();
		if (listener != null) {
			listener.onSuccess(topic, message);
		}
	}

	@Override
	public void onFailure(MqttTopic topic, Object message, Throwable cause) {
		latch.countDown();
		if (listener != null) {
			listener.onFailure(topic, message, cause);
		}
	}
}
