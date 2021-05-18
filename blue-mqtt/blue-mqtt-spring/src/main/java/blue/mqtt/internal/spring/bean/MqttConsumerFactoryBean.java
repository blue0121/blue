package blue.mqtt.internal.spring.bean;

import blue.base.spring.common.ConsumerFactoryBean;
import blue.mqtt.core.MqttClient;
import blue.mqtt.core.MqttConsumer;
import blue.mqtt.core.MqttException;
import blue.mqtt.core.MqttTopic;
import blue.mqtt.core.options.MqttConsumerOptions;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-10
 */
public class MqttConsumerFactoryBean extends ConsumerFactoryBean
        implements FactoryBean<MqttConsumer>, InitializingBean {

    private int defaultQos;

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

        MqttConsumerOptions options = new MqttConsumerOptions();
        this.setConsumerOptions(options);
        options.setDefaultQos(defaultQos);
        this.mqttConsumer = mqttClient.createConsumer(options);
        this.createMqttConsumers();
    }

    private void createMqttConsumers() {
        for (var config : configList) {
            MqttConsumerOptions options = new MqttConsumerOptions();
            this.setConsumerOptions(options);
            this.setConsumerOptions(options, config);
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

    public void setDefaultQos(int defaultQos) {
        this.defaultQos = defaultQos;
    }

    public void setMqttClient(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    public void setConfigList(List<MqttConsumerConfig> configList) {
        this.configList = configList;
    }
}
