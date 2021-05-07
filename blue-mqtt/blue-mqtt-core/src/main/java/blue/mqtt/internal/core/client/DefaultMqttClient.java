package blue.mqtt.internal.core.client;

import blue.base.core.message.ProducerListener;
import blue.base.core.util.JsonUtil;
import blue.base.core.util.WaitUtil;
import blue.mqtt.core.MqttClient;
import blue.mqtt.core.MqttClientOptions;
import blue.mqtt.core.MqttConsumer;
import blue.mqtt.core.MqttConsumerOptions;
import blue.mqtt.core.MqttProducer;
import blue.mqtt.core.MqttProducerOptions;
import blue.mqtt.core.MqttTopic;
import blue.mqtt.internal.core.consumer.MqttListenerConfig;
import blue.mqtt.internal.core.producer.DefaultMqttProducer;
import blue.mqtt.internal.core.producer.PublishCallback;
import blue.mqtt.internal.core.producer.SyncCallback;
import blue.mqtt.internal.core.producer.SyncDisconnectCallback;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.hawtdispatch.Dispatch;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Jin Zheng
 * @since 2021-05-03
 */
public class DefaultMqttClient implements MqttClient {
	private static Logger logger = LoggerFactory.getLogger(DefaultMqttClient.class);

	private List<Map.Entry<String, CallbackConnection>> connectionList;
	private Map<String, CallbackConnection> connectionMap;
	private final ConcurrentMap<String, String> topicClientIdMap = new ConcurrentHashMap<>();
	private final AtomicLong counter = new AtomicLong(1);
	private final DefaultMqttListener listener;
	private final MqttClientOptions options;

	public DefaultMqttClient(MqttClientOptions options) {
		this.options = options;
		this.listener = new DefaultMqttListener();
	}

	public void connect() {
		this.options.check();
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
		this.connect(mqtt);
	}

	private void connect(MQTT mqtt) {
		CountDownLatch latch = new CountDownLatch(options.getCount());
		List<Map.Entry<String, CallbackConnection>> connList = new ArrayList<>();
		Map<String, CallbackConnection> connMap = new HashMap<>();
		for (int i = 0; i < options.getCount(); i++) {
			String clientId = (options.getCount() == 1 ? options.getClientId() : options.getClientId() + i);
			mqtt.setClientId(clientId);
			CallbackConnection connection = mqtt.callbackConnection();
			connection.listener(listener);
			connection.connect(new SyncCallback<>(latch));
			connList.add(Map.entry(clientId, connection));
			connMap.put(clientId, connection);
			logger.info("MQTT '{}' connect: {}, clientId: {}", options.getId(), mqtt.getHost(), clientId);
		}
		WaitUtil.await(latch);
		this.connectionList = List.copyOf(connList);
		this.connectionMap = Map.copyOf(connMap);
		logger.info("MQTT '{}' connect successful, count: {}", options.getId(), options.getCount());
	}

	@Override
	public void disconnect() {
		CountDownLatch latch = new CountDownLatch(options.getCount());
		for (var entry : connectionList) {
			CallbackConnection connection = entry.getValue();
			connection.disconnect(new SyncDisconnectCallback(options.getId(), latch, connection));
			logger.info("MQTT '{}' disconnect: {}, clientId: {}", options.getId(), options.getBroker(), entry.getKey());
		}
		WaitUtil.await(latch);
		logger.info("MQTT '{}' disconnect successful", options.getId());
	}

	public void publish(MqttTopic topic, Object message, ProducerListener<MqttTopic, Object> listener) {
		CallbackConnection connection = this.getConnection().getValue();
		byte[] payload = JsonUtil.toBytes(message);
		Callback<Void> callback = new PublishCallback(topic, message, listener);
		connection.getDispatchQueue().execute(() ->
				connection.publish(topic.getTopic(), payload, topic.getQos().toQoS(), false, callback));
	}

	private Map.Entry<String, CallbackConnection> getConnection() {
		if (options.getCount() == 1) {
			return connectionList.get(0);
		}

		int idx = (int) (counter.getAndIncrement() % options.getCount());
		return connectionList.get(idx);
	}

	public void subscribe(List<MqttListenerConfig> configList) {
		CountDownLatch latch = new CountDownLatch(configList.size());
		for (var config : configList) {
			var connection = this.getConnection();
			var callback = new SubscribeCallback<byte[]>(latch, config.getTopic(), topicClientIdMap, connection.getKey());
			var ts = new Topic[]{new Topic(config.getTopic(), config.getQos().toQoS())};
			connection.getValue().subscribe(ts, callback);
		}
		WaitUtil.await(latch);
		logger.info("MQTT '{}' subscribe successful, count: {}", options.getId(), configList.size());
	}

	public void unsubscribe(Collection<String> topics) {
		CountDownLatch latch = new CountDownLatch(topics.size());
		for (var topic : topics) {
			String clientId = topicClientIdMap.get(topic);
			if (clientId == null || clientId.isEmpty()) {
				logger.warn("ClientId is empty, topic: {}", topic);
				continue;
			}
			var connection = connectionMap.get(clientId);
			if (connection == null) {
				logger.warn("Connection is null, clientId: {}", clientId);
				continue;
			}
			var callback = new UnsubscribeCallback<Void>(latch, topic, topicClientIdMap, clientId);
			var ts = new UTF8Buffer[] {new UTF8Buffer(topic)};
			connection.unsubscribe(ts, callback);
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
		return null;
	}
}
