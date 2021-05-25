package blue.kafka.internal.spring.bean;

import blue.base.core.message.ProducerListener;
import blue.kafka.core.KafkaClient;
import blue.kafka.core.KafkaProducer;
import blue.kafka.core.options.KafkaProducerOptions;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Properties;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-25
 */
public class KafkaProducerFactoryBean implements FactoryBean<KafkaProducer>, InitializingBean {
    private String id;
    private ProducerListener producerListener;
    private Class<?> keySerializer;
    private Class<?> valueSerializer;
    private Properties prop;

    private KafkaClient kafkaClient;
    private KafkaProducer producer;

    public KafkaProducerFactoryBean() {
    }

    @Override
    public KafkaProducer getObject() throws Exception {
        return producer;
    }

    @Override
    public Class<?> getObjectType() {
        return KafkaProducer.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        KafkaProducerOptions options = new KafkaProducerOptions();
        options.setId(id)
                .setProducerListener(producerListener);
        options.setKeySerializer(keySerializer)
                .setValueSerializer(valueSerializer)
                .setProp(prop);
        this.producer = kafkaClient.createProducer(options);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProducerListener(ProducerListener producerListener) {
        this.producerListener = producerListener;
    }

    public void setKeySerializer(Class<?> keySerializer) {
        this.keySerializer = keySerializer;
    }

    public void setValueSerializer(Class<?> valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public void setProp(Properties prop) {
        this.prop = prop;
    }

    public void setKafkaClient(KafkaClient kafkaClient) {
        this.kafkaClient = kafkaClient;
    }
}
