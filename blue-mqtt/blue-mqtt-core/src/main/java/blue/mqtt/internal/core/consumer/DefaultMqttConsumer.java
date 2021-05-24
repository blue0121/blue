package blue.mqtt.internal.core.consumer;

import blue.base.core.message.ConsumerListener;
import blue.base.core.util.AssertUtil;
import blue.base.internal.core.message.AbstractConsumer;
import blue.mqtt.core.MqttConsumer;
import blue.mqtt.core.MqttQos;
import blue.mqtt.core.MqttTopic;
import blue.mqtt.core.options.MqttConsumerOptions;
import blue.mqtt.internal.core.client.DefaultMqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-04
 */
public class DefaultMqttConsumer extends AbstractConsumer<MqttTopic> implements MqttConsumer {
	private static Logger logger = LoggerFactory.getLogger(DefaultMqttConsumer.class);

	private final DefaultMqttClient mqttClient;
	private final MqttQos defaultQos;

	public DefaultMqttConsumer(MqttConsumerOptions options, DefaultMqttClient mqttClient) {
		super(options);
		this.mqttClient = mqttClient;
		this.defaultQos = options.getDefaultQos();
	}

	@Override
	public void subscribe(Collection<MqttTopic> topicList, ConsumerListener<MqttTopic, ?> listener) {
		AssertUtil.notEmpty(topicList, "Topic list");
		AssertUtil.notNull(listener, "ConsumerListener");
		List<MqttListenerConfig> configList = new ArrayList<>();
		for (var topic : topicList) {
			var config = new MqttListenerConfig();
			config.setTopic(topic.getTopic());
			config.setQos(topic.getQos() == null ? defaultQos.getType() : topic.getQos().getType());
			config.setMultiThread(options.isMultiThread());
			config.setListener(listener);
			config.setExceptionHandler(options.getExceptionHandler());
			config.setExecutor(options.getExecutor());
			config.init();
			configList.add(config);
		}
		this.subscribe(configList);
	}

	private void subscribe(List<MqttListenerConfig> configList) {
		if (configList == null || configList.isEmpty()) {
			return;
		}
		mqttClient.subscribe(configList);
	}

	@Override
	public void unsubscribe(Collection<String> topicList) {
		AssertUtil.notEmpty(topicList, "Topic list");
		mqttClient.unsubscribe(topicList);
	}
}
