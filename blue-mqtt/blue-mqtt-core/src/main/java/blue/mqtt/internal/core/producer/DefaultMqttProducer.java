package blue.mqtt.internal.core.producer;

import blue.base.core.message.ProducerListener;
import blue.base.core.message.ProducerOptions;
import blue.base.core.util.AssertUtil;
import blue.base.core.util.WaitUtil;
import blue.base.internal.core.message.AbstractProducer;
import blue.mqtt.core.MqttProducer;
import blue.mqtt.core.MqttQos;
import blue.mqtt.core.MqttTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author Jin Zheng
 * @since 2019-06-30
 */
public class DefaultMqttProducer extends AbstractProducer<MqttTopic> implements MqttProducer {
	private static Logger logger = LoggerFactory.getLogger(DefaultMqttProducer.class);

	private MqttClient mqttClient;
	private MqttQos defaultQos;

	public DefaultMqttProducer(String name, ProducerOptions options) {
		super(name, options);
	}

	@Override
	public void sendSync(MqttTopic topic, List<Object> messageList) {
		AssertUtil.notNull(topic, "Topic");
		AssertUtil.notEmpty(messageList, "Message list");
		this.setMqttTopic(topic);
		CountDownLatch latch = new CountDownLatch(messageList.size());
		for (Object message : messageList) {
			var listener = new MqttProducerListener(latch, topic, message, options.getProducerListener());
			this.mqttClient.publish(topic, message, listener);
		}
		WaitUtil.await(latch);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void sendAsync(MqttTopic topic, List<Object> messageList, ProducerListener<MqttTopic, Object> listener) {
		AssertUtil.notNull(topic, "Topic");
		AssertUtil.notEmpty(messageList, "Message list");
		this.setMqttTopic(topic);
		if (listener == null) {
			listener = this.options.getProducerListener();
		}
		for (Object message : messageList) {
			this.mqttClient.publish(topic, message, listener);
		}
	}

	@Override
	public void init() {
		AssertUtil.notNull(mqttClient, "MqttClient");
		AssertUtil.notNull(defaultQos, "DefaultQos");

		super.init();
	}

	private void setMqttTopic(MqttTopic topic) {
		if (topic.getQos() == null) {
			topic.setQos(defaultQos);
		}
	}

	public MqttClient getMqttClient() {
		return mqttClient;
	}

	public void setMqttClient(MqttClient mqttClient) {
		this.mqttClient = mqttClient;
	}

	public void setDefaultQos(int defaultQos) {
		this.defaultQos = MqttQos.valueOf(defaultQos);
	}

	public MqttQos getDefaultQos() {
		return defaultQos;
	}
}
