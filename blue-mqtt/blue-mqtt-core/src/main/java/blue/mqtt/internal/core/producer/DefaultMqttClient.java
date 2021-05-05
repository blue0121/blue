package blue.mqtt.internal.core.producer;

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
import java.util.Arrays;
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
    private final ConcurrentMap<String, CallbackConnection> topicConnMap = new ConcurrentHashMap<>();
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
        CallbackConnection connection = this.getConnection();
        byte[] payload = JsonUtil.toBytes(message);
        Callback<Void> callback = new PublishCallback(topic, message, listener);
        connection.getDispatchQueue().execute(() ->
                connection.publish(topic.getTopic(), payload, topic.getQos().toQoS(), false, callback));
    }

    private CallbackConnection getConnection() {
        if (options.getCount() == 1) {
            return connectionList.get(0).getValue();
        }

        int idx = (int) (counter.getAndIncrement() % options.getCount());
        return connectionList.get(idx).getValue();
    }

    public void subscribe(Topic... topics) {
        CountDownLatch latch = new CountDownLatch(options.getCount());
        for (var entry : connectionList) {
            entry.getValue().subscribe(topics, new SyncCallback<>(latch));
            logger.info("MQTT '{}' subscribe: {}, clientId: {}", options.getId(), Arrays.toString(topics), entry.getKey());
        }
        WaitUtil.await(latch);
        logger.info("MQTT '{}' subscribe successful", options.getId());
    }

    public void unsubscribe(UTF8Buffer... topics) {
        CountDownLatch latch = new CountDownLatch(options.getCount());
        for (var entry : connectionList) {
            entry.getValue().unsubscribe(topics, new SyncCallback<>(latch));
            logger.info("MQTT '{}' unsubscribe: {}, clientId: {}", options.getId(), Arrays.toString(topics), entry.getKey());
        }
        WaitUtil.await(latch);
        logger.info("MQTT '{}' unsubscribe successful", options.getId());
    }

    @Override
    public MqttProducer createProducer(MqttProducerOptions options) {
        return null;
    }

    @Override
    public MqttConsumer createConsumer(MqttConsumerOptions options) {
        return null;
    }
}
