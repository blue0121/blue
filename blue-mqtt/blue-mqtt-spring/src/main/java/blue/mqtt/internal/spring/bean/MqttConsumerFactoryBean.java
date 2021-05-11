package blue.mqtt.internal.spring.bean;

import blue.base.core.message.ExceptionHandler;
import blue.mqtt.core.MqttClient;
import blue.mqtt.core.MqttConsumer;
import blue.mqtt.core.MqttException;
import blue.mqtt.core.MqttTopic;
import blue.mqtt.core.options.MqttConsumerOptions;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-10
 */
public class MqttConsumerFactoryBean implements FactoryBean<MqttConsumer>, InitializingBean {

    private String id;
    private int defaultQos;
    private boolean multiThread;
    private Executor executor;
    private ExceptionHandler exceptionHandler;

    private MqttClient mqttClient;
    private List<MqttConsumerConfig> configList;
    private MqttConsumer mqttConsumer;
    private List<MqttConsumer> mqttConsumerList = new ArrayList<>();

	public MqttConsumerFactoryBean() {
	}

    @Override
    public void afterPropertiesSet() throws Exception {
        if (configList == null || configList.isEmpty()) {
            throw new MqttException("MQTT consumer listener config is empty");
        }

        MqttConsumerOptions options = this.createConsumerOptions();
        this.mqttConsumer = mqttClient.createConsumer(options);
        this.createMqttConsumers();
    }

    private MqttConsumerOptions createConsumerOptions() {
        MqttConsumerOptions options = new MqttConsumerOptions();
        options.setId(id)
                .setMultiThread(multiThread)
                .setExecutor(executor)
                .setExceptionHandler(exceptionHandler);
        options.setDefaultQos(defaultQos);
        return options;
    }

    private void createMqttConsumers() {
        for (var config : configList) {
            MqttConsumerOptions options = this.createConsumerOptions();
            if (config.getMultiThread() != null) {
                options.setMultiThread(config.getMultiThread());
            }
            if (config.getExecutor() != null) {
                options.setExecutor(config.getExecutor());
            }
            if (config.getExceptionHandler() != null) {
                options.setExceptionHandler(config.getExceptionHandler());
            }
            if (config.getQos() != null) {
                options.setDefaultQos(config.getQos());
            }
            MqttConsumer consumer = mqttClient.createConsumer(options);
            consumer.subscribe(new MqttTopic(config.getTopic(), options.getDefaultQos()), config.getListener());
            mqttConsumerList.add(consumer);
        }
    }

    @Override
    public MqttConsumer getObject() throws Exception {
        return mqttConsumer;
    }

    @Override
    public Class<?> getObjectType() {
        return MqttConsumer.class;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDefaultQos(int defaultQos) {
        this.defaultQos = defaultQos;
    }

    public void setMultiThread(boolean multiThread) {
        this.multiThread = multiThread;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public void setMqttClient(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    public void setConfigList(List<MqttConsumerConfig> configList) {
        this.configList = configList;
    }
}
