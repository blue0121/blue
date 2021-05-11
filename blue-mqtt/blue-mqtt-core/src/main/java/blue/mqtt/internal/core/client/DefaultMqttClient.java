package blue.mqtt.internal.core.client;

import blue.base.core.message.ProducerListener;
import blue.base.core.util.JsonUtil;
import blue.base.core.util.WaitUtil;
import blue.mqtt.core.MqttClient;
import blue.mqtt.core.MqttClientOptions;
import blue.mqtt.core.MqttConsumer;
import blue.mqtt.core.MqttConsumerOptions;
import blue.mqtt.core.MqttException;
import blue.mqtt.core.MqttProducer;
import blue.mqtt.core.MqttProducerOptions;
import blue.mqtt.core.MqttTopic;
import blue.mqtt.internal.core.consumer.DefaultMqttConsumer;
import blue.mqtt.internal.core.consumer.MqttListenerConfig;
import blue.mqtt.internal.core.producer.DefaultMqttProducer;
import blue.mqtt.internal.core.producer.PublishCallback;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.hawtdispatch.Dispatch;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author Jin Zheng
 * @since 2021-05-03
 */
public class DefaultMqttClient implements MqttClient {
	private static Logger logger = LoggerFactory.getLogger(DefaultMqttClient.class);

	private final ConnectionSet connectionSet = new ConnectionSet();
	private final Map<String, MqttConnection> connectionMap = new ConcurrentHashMap<>();

	private final MqttClientOptions options;

	public DefaultMqttClient(MqttClientOptions options) {
		this.options = options;
	}

	public void connect() {
		this.options.check();
		MQTT mqtt = this.mqtt();
		CountDownLatch latch = new CountDownLatch(options.getCount());
		for (int i = 0; i < options.getCount(); i++) {
			String clientId = (options.getCount() == 1 ? options.getClientId() : options.getClientId() + i);
			MqttConnection connection = new MqttConnection(connectionSet, mqtt, clientId, latch);
			connectionMap.put(clientId, connection);
		}
		WaitUtil.await(latch);
		logger.info("MQTT '{}' connect successful, count: {}", options.getId(), options.getCount());
	}

	private MQTT mqtt() {
		MQTT mqtt = new MQTT();
		try {
			mqtt.setHost(options.getBroker());
		}
		catch (URISyntaxException e) {
			logger.error("Error, ", e);
		}
		if (options.getUsername() != null && !options.getUsername().isEmpty()) {
			mqtt.setUserName(options.getUsername());
		}
		if (options.getPassword() != null && !options.getPassword().isEmpty()) {
			mqtt.setPassword(options.getPassword());
		}
		if (options.getKeepAlive() > 0) {
			mqtt.setKeepAlive((short) options.getKeepAlive());
		}
		if (options.getReconnectDelay() > 0) {
			mqtt.setReconnectDelay(options.getReconnectDelay());
			mqtt.setReconnectDelayMax(options.getReconnectDelay());
		}
		if (options.getSslContext() != null) {
			mqtt.setSslContext(options.getSslContext());
		}
		mqtt.setDispatchQueue(Dispatch.createQueue());
		return mqtt;
	}

	@Override
	public void disconnect() {
		CountDownLatch latch = new CountDownLatch(options.getCount());
		for (var entry : connectionMap.entrySet()) {
			entry.getValue().disconnect(latch);
		}
		WaitUtil.await(latch);
		logger.info("MQTT '{}' disconnect successful", options.getId());
	}

	public void publish(MqttTopic topic, Object message, ProducerListener<MqttTopic, Object> listener) {
		var connection = connectionSet.getConnection();
		if (connection == null) {
			throw new MqttException("No MQTT Connection");
		}
		byte[] payload = JsonUtil.toBytes(message);
		Callback<Void> callback = new PublishCallback(topic, message, listener);
		var conn = connection.getValue();
		conn.getDispatchQueue().execute(() ->
				conn.publish(topic.getTopic(), payload, topic.getQos().toQoS(), false, callback));
	}

	public void subscribe(List<MqttListenerConfig> configList) {
		CountDownLatch latch = new CountDownLatch(configList.size());
		for (var config : configList) {
			var connection = connectionSet.getConnection();
			var mqttConn = connectionMap.get(connection.getKey());
			if (mqttConn == null) {
				logger.warn("MqttConnection is null, clientId: {}", connection.getKey());
				continue;
			}

			mqttConn.addMqttListenerConfig(config);
			var callback = new SubscribeCallback<byte[]>(latch, config.getTopic(), connection.getKey(), connectionSet);
			var ts = new Topic[]{new Topic(config.getTopic(), config.getQos().toQoS())};
			var conn = connection.getValue();
			conn.getDispatchQueue().execute(() -> conn.subscribe(ts, callback));
		}
		WaitUtil.await(latch);
		logger.info("MQTT '{}' subscribe successful, count: {}", options.getId(), configList.size());
	}

	public void unsubscribe(Collection<String> topics) {
		CountDownLatch latch = new CountDownLatch(topics.size());
		for (var topic : topics) {
			var connection = connectionSet.getConnectionByTopic(topic);
			if (connection == null) {
				logger.warn("Connection is null, topic: {}", topic);
				continue;
			}
			var callback = new UnsubscribeCallback<Void>(latch, topic, connectionSet);
			var ts = new UTF8Buffer[] {new UTF8Buffer(topic)};
			var conn = connection.getValue();
			conn.getDispatchQueue().execute(() -> conn.unsubscribe(ts, callback));
		}
		WaitUtil.await(latch);
		logger.info("MQTT '{}' unsubscribe successful, count: {}", options.getId(), topics.size());
	}

	@Override
	public MqttProducer createProducer(MqttProducerOptions options) {
		return new DefaultMqttProducer(options, this);
	}

	@Override
	public MqttConsumer createConsumer(MqttConsumerOptions options) {
		return new DefaultMqttConsumer(options, this);
	}
}
