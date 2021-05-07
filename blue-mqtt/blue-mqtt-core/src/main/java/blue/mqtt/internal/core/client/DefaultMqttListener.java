package blue.mqtt.internal.core.client;

import blue.base.core.util.JsonUtil;
import blue.mqtt.core.MqttTopic;
import blue.mqtt.internal.core.consumer.MqttListenerConfig;
import blue.mqtt.internal.core.consumer.MqttTopicUtil;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.ExtendedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Jin Zheng
 * @since 2019-06-30
 */
public class DefaultMqttListener implements ExtendedListener {
	private static Logger logger = LoggerFactory.getLogger(DefaultMqttListener.class);

	private static final String SHARE = "$share/";
	private static final String QUEUE = "$queue/";
	private static final String SPLIT = "/";

	private List<MqttListenerConfig> configList = new CopyOnWriteArrayList<>();

	public DefaultMqttListener() {
	}

	@Override
	public void onPublish(UTF8Buffer topic, Buffer body, Callback<Callback<Void>> ack) {
		this.invoke(topic.toString(), body.toByteArray(), "callback");
		ack.onSuccess(null);
	}

	@Override
	public void onPublish(UTF8Buffer topic, Buffer body, Runnable ack) {
		this.invoke(topic.toString(), body.toByteArray(), "runnable");
		ack.run();
	}

	private void invoke(String topic, byte[] payload, String type) {
		logger.debug("MQTT onReceive topic: {}", topic);
		for (MqttListenerConfig config : configList) {
			if (!this.isMatched(config.getTopic(), topic)) {
				continue;
			}

			if (config.isMultiThread()) {
				config.getExecutor().execute(() -> this.invoke(topic, payload, config, type));
			}
			else {
				this.invoke(topic, payload, config, type);
			}
		}
	}

	private boolean isMatched(String topicFilter, String topic) {
		if (topicFilter.startsWith(QUEUE)) {
			topicFilter = topicFilter.substring(topicFilter.indexOf(SPLIT) + 1);
		}
		else if (topicFilter.startsWith(SHARE)) {
			topicFilter = topicFilter.substring(topicFilter.indexOf(SPLIT, SHARE.length()) + 1);
		}
		return MqttTopicUtil.isMatched(topicFilter, topic);
	}

	@SuppressWarnings("unchecked")
	private void invoke(String topic, byte[] payload, MqttListenerConfig listener, String type) {
		Class<?> clazz = listener.getClazz();
		Object value = JsonUtil.fromBytes(payload, clazz);
		try {
			listener.getListener().onReceive(new MqttTopic(topic, null), value);
			if (logger.isDebugEnabled()) {
				logger.debug("MQTT onReceive {}, listen topic: {}, original topic: {}", type, listener.getTopic(), topic);
			}
		}
		catch (Exception e) {
			listener.getExceptionHandler().onError(new MqttTopic(topic, null), value, e);
		}
	}

	@Override
	public void onConnected() {
		logger.info("MQTT connected");
	}

	@Override
	public void onDisconnected() {
		logger.warn("MQTT disconnected");
	}

	@Override
	public void onFailure(Throwable cause) {
		logger.error("Error, ", cause);
	}

	public List<MqttListenerConfig> getConfigList() {
		return configList;
	}

	public void addMqttListenerConfig(MqttListenerConfig config) {
		configList.add(config);
	}

	public void addMqttListenerConfig(Collection<MqttListenerConfig> configList) {
		this.configList.addAll(configList);
	}

}
