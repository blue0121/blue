package blue.mqtt.internal.spring.bean;

import blue.mqtt.core.MqttClient;
import blue.mqtt.core.options.MqttClientOptions;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import javax.net.ssl.SSLContext;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-10
 */
public class MqttClientFactoryBean implements FactoryBean<MqttClient>, InitializingBean, DisposableBean {
    private String id;
    private String broker;
    private String username;
    private String password;
    private String clientId;
    private int count;
    private int timeout;
    private int keepAlive;
    private int reconnectDelay;
    private SSLContext sslContext;

    private MqttClient mqttClient;

	public MqttClientFactoryBean() {
	}

    @Override
    public MqttClient getObject() throws Exception {
        return mqttClient;
    }

    @Override
    public Class<?> getObjectType() {
        return MqttClient.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        MqttClientOptions options = new MqttClientOptions();
        options.setId(id)
                .setBroker(broker)
                .setUsername(username)
                .setPassword(password)
                .setClientId(clientId)
                .setCount(count)
                .setTimeout(timeout)
                .setKeepAlive(keepAlive)
                .setReconnectDelay(reconnectDelay)
                .setSslContext(sslContext);
        this.mqttClient = MqttClient.create(options);
    }

    @Override
    public void destroy() throws Exception {
        if (mqttClient != null) {
            mqttClient.disconnect();
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setKeepAlive(int keepAlive) {
        this.keepAlive = keepAlive;
    }

    public void setReconnectDelay(int reconnectDelay) {
        this.reconnectDelay = reconnectDelay;
    }

    public void setSslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
    }
}
