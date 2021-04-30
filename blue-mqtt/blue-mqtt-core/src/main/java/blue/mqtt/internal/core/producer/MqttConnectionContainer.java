package blue.mqtt.internal.core.producer;

import blue.base.core.message.ProducerListener;
import blue.base.core.util.JsonUtil;
import blue.base.core.util.WaitUtil;
import blue.mqtt.core.MqttException;
import blue.mqtt.core.MqttTopic;
import blue.mqtt.internal.core.util.Const;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.ExtendedListener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-13
 */
public class MqttConnectionContainer {
	private static Logger logger = LoggerFactory.getLogger(MqttConnectionContainer.class);

	private final List<Map.Entry<String, CallbackConnection>> connectionList = new ArrayList<>();
	private final String name;
	private final int count;
	private final AtomicLong counter = new AtomicLong(1);
	private MQTT mqtt;

	public MqttConnectionContainer(String name, int count) {
		if (count < 1 || count > Const.MAX_COUNT) {
			throw new MqttException("MQTT count must be between 1 and 200");
		}

		this.name = name;
		this.count = count;
	}

	public void connect(MQTT mqtt, String clientId, ExtendedListener listener) {
		this.mqtt = mqtt;
		CountDownLatch latch = new CountDownLatch(count);
		for (int i = 0; i < count; i++) {
			String id = (count == 1 ? clientId : clientId + i);
			mqtt.setClientId(id);
			CallbackConnection connection = mqtt.callbackConnection();
			connection.listener(listener);
			connectionList.add(Map.entry(id, connection));
			connection.connect(new SyncCallback<>(latch));
			logger.info("MQTT '{}' connect: {}, clientId: {}", name, mqtt.getHost(), id);
		}
		WaitUtil.await(latch);
		logger.info("MQTT '{}' connect successful, count: {}", name, count);
	}

	public void publish(MqttTopic topic, Object message, ProducerListener<MqttTopic, Object> listener) {
		CallbackConnection connection = this.getConnection();
		byte[] payload = JsonUtil.toBytes(message);
		connection.getDispatchQueue().execute(() ->
		{
			connection.publish(topic.getTopic(), payload, topic.getQos().toQoS(), false,
					new PublishCallback(topic, message, listener));
		});
	}

	private CallbackConnection getConnection() {
		if (count == 1) {
			return connectionList.get(0).getValue();
		}

		int idx = (int) (counter.getAndIncrement() % count);
		return connectionList.get(idx).getValue();
	}

	public void disconnect() {
		CountDownLatch latch = new CountDownLatch(count);
		for (var entry : connectionList) {
			CallbackConnection connection = entry.getValue();
			connection.disconnect(new SyncDisconnectCallback(name, latch, connection));
			logger.info("MQTT '{}' disconnect: {}, clientId: {}", name, mqtt.getHost(), entry.getKey());
		}
		WaitUtil.await(latch);
		logger.info("MQTT '{}' disconnect successful", name);
	}

	public void subscribe(Topic... topics) {
		CountDownLatch latch = new CountDownLatch(count);
		for (var entry : connectionList) {
			entry.getValue().subscribe(topics, new SyncCallback<>(latch));
			logger.info("MQTT '{}' subscribe: {}, clientId: {}", name, Arrays.toString(topics), entry.getKey());
		}
		WaitUtil.await(latch);
		logger.info("MQTT '{}' subscribe successful", name);
	}

	public void unsubscribe(UTF8Buffer... topics) {
		CountDownLatch latch = new CountDownLatch(count);
		for (var entry : connectionList) {
			entry.getValue().unsubscribe(topics, new SyncCallback<>(latch));
			logger.info("MQTT '{}' unsubscribe: {}, clientId: {}", name, Arrays.toString(topics), entry.getKey());
		}
		WaitUtil.await(latch);
		logger.info("MQTT '{}' unsubscribe successful", name);
	}

}
