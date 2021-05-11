package blue.mqtt.internal.core.client;

import blue.base.core.util.JsonUtil;
import blue.mqtt.core.MqttTopic;
import blue.mqtt.internal.core.consumer.MqttListenerConfig;
import blue.mqtt.internal.core.consumer.MqttTopicUtil;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.ExtendedListener;
import org.fusesource.mqtt.client.MQTT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-08
 */
public class MqttConnection implements ExtendedListener {
    private static Logger logger = LoggerFactory.getLogger(MqttConnection.class);
    private static final String SHARE = "$share/";
    private static final String QUEUE = "$queue/";
    private static final String SPLIT = "/";

    private final ConnectionSet connectionSet;
    private final MQTT mqtt;
    private final String clientId;
    private final CountDownLatch latch;
    private final List<MqttListenerConfig> configList = new CopyOnWriteArrayList<>();

    private CallbackConnection connection;

    public MqttConnection(ConnectionSet connectionSet, MQTT mqtt, String clientId, CountDownLatch latch) {
        this.connectionSet = connectionSet;
        this.mqtt = mqtt;
        this.clientId = clientId;
        this.latch = latch;
        this.connect();
    }

    private void connect() {
        this.mqtt.setClientId(clientId);
        this.connection = mqtt.callbackConnection();
        this.connection.listener(this);
        this.connection.getDispatchQueue().execute(() -> connection.connect(new SyncCallback<>(latch)));
    }

    public List<MqttListenerConfig> getConfigList() {
        return configList;
    }

    public void addMqttListenerConfig(MqttListenerConfig config) {
        configList.add(config);
    }

    public String getClientId() {
        return clientId;
    }

    public void disconnect(CountDownLatch latch) {
        connection.getDispatchQueue().execute(() ->
                connection.disconnect(new SyncDisconnectCallback(clientId, latch, connection)));
    }

    @Override
    public void onConnected() {
        logger.info("MQTT connected, clientId: {}", clientId);
        connectionSet.connect(clientId, connection);
    }

    @Override
    public void onDisconnected() {
        logger.warn("MQTT disconnected, clientId: {}", clientId);
        connectionSet.disconnect(clientId);
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
    public void onFailure(Throwable cause) {
        logger.error("MQTT ClientId '{}' Error, ", clientId, cause);
    }
}
