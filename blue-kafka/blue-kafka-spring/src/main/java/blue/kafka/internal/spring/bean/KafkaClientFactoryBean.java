package blue.kafka.internal.spring.bean;

import blue.kafka.core.KafkaClient;
import blue.kafka.core.options.KafkaClientOptions;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-25
 */
public class KafkaClientFactoryBean implements FactoryBean<KafkaClient>, InitializingBean, DisposableBean {
    private String id;
    private String broker;

    private KafkaClient kafkaClient;

    public KafkaClientFactoryBean() {
    }

    @Override
    public KafkaClient getObject() throws Exception {
        return kafkaClient;
    }

    @Override
    public Class<?> getObjectType() {
        return KafkaClient.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        KafkaClientOptions options = new KafkaClientOptions();
        options.setId(id)
                .setBroker(broker);
        kafkaClient = KafkaClient.create(options);
    }

    @Override
    public void destroy() throws Exception {
        if (kafkaClient != null) {
            kafkaClient.disconnect();
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }
}
