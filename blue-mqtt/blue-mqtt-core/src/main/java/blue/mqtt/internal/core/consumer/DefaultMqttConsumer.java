package blue.mqtt.internal.core.consumer;

import blue.base.core.message.ConsumerListener;
import blue.base.core.message.ConsumerOptions;
import blue.base.core.util.AssertUtil;
import blue.base.internal.core.message.AbstractConsumer;
import blue.base.internal.core.message.ConsumerListenerConfig;
import blue.mqtt.core.MqttConsumer;
import blue.mqtt.core.MqttQos;
import blue.mqtt.core.MqttTopic;
import blue.mqtt.internal.core.producer.MqttClient;
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

	private MqttClient mqttClient;
	private MqttQos defaultQos;

	public DefaultMqttConsumer(String name, ConsumerOptions options) {
		super(name, options);
	}

	@Override
	public void subscribe(Collection<MqttTopic> topicList, ConsumerListener<MqttTopic, ?> listener) {
		AssertUtil.notEmpty(topicList, "Topic list");
		List<ConsumerListenerConfig> configList = new ArrayList<>();
		for (var topic : topicList) {
			var config = new MqttListenerConfig();
			config.setTopic(topic.getTopic());
			config.setQos(topic.getQos() == null ? defaultQos.getType() : topic.getQos().getType());
			config.setMultiThread(options.isMultiThread());
			config.setListener(listener);
			config.init();
			configList.add(config);
		}
		this.checkHandler(configList);
		this.subscribe(configList);
	}

	@Override
	public void unsubscribe(Collection<String> topicList) {
		AssertUtil.notEmpty(topicList, "Topic list");
		mqttClient.unsubscribe(topicList);
	}

	@Override
	protected void subscribe(List<ConsumerListenerConfig> configList) {
		List<MqttListenerConfig> list = new ArrayList<>();
		for (var config : configList) {
			list.add((MqttListenerConfig) config);
		}
		mqttClient.subscribe(list);
	}

	public void setMqttClient(MqttClient mqttClient) {
		this.mqttClient = mqttClient;
	}

	public void setDefaultQos(int defaultQos) {
		this.defaultQos = MqttQos.valueOf(defaultQos);
	}
}
