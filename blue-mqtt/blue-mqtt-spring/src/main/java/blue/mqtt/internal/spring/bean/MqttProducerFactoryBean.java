package blue.mqtt.internal.spring.bean;

import blue.base.core.message.ProducerListener;
import blue.mqtt.core.MqttClient;
import blue.mqtt.core.MqttProducer;
import blue.mqtt.core.MqttProducerOptions;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-10
 */
public class MqttProducerFactoryBean implements FactoryBean<MqttProducer>, InitializingBean {
    private String id;
    private int defaultQos;
    private ProducerListener producerListener;
    private MqttClient mqttClient;

    private MqttProducer mqttProducer;

	public MqttProducerFactoryBean() {
	}

    @Override
    public void afterPropertiesSet() throws Exception {
        MqttProducerOptions options = new MqttProducerOptions();
        options.setId(id).setProducerListener(producerListener);
        options.setDefaultQos(defaultQos);
        this.mqttProducer = mqttClient.createProducer(options);
    }

    @Override
    public MqttProducer getObject() throws Exception {
        return mqttProducer;
    }

    @Override
    public Class<?> getObjectType() {
        return MqttProducer.class;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDefaultQos(int defaultQos) {
        this.defaultQos = defaultQos;
    }

    public void setProducerListener(ProducerListener producerListener) {
        this.producerListener = producerListener;
    }

    public void setMqttClient(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }
}
