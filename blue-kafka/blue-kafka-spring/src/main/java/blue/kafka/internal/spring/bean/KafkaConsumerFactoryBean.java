package blue.kafka.internal.spring.bean;

import blue.base.spring.common.ConsumerFactoryBean;
import blue.kafka.core.KafkaClient;
import blue.kafka.core.KafkaConsumer;
import blue.kafka.core.KafkaException;
import blue.kafka.core.KafkaTopic;
import blue.kafka.core.OffsetManager;
import blue.kafka.core.options.KafkaConsumerOptions;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-25
 */
public class KafkaConsumerFactoryBean extends ConsumerFactoryBean
        implements FactoryBean<KafkaConsumer>, InitializingBean {

    private Class<?> keyDeserializer;
    private Class<?> valueDeserializer;
    private int count;
    private int duration;
    private String group;
    private OffsetManager offsetManager;
    private Properties prop;

    private KafkaClient kafkaClient;
    private List<KafkaConsumerConfig> configList;
    private KafkaConsumer kafkaConsumer;
    private List<KafkaConsumer> kafkaConsumerList = new ArrayList<>();

    public KafkaConsumerFactoryBean() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (configList == null || configList.isEmpty()) {
            throw new KafkaException("Kafka consumer listener config is empty");
        }

        KafkaConsumerOptions options = new KafkaConsumerOptions();
        this.setKafkaConsumerOptions(options);
        this.kafkaConsumer = kafkaClient.createConsumer(options);
        this.createKafkaConsumers();
    }

    private void createKafkaConsumers() {
        for (var config : configList) {
            KafkaConsumerOptions options = new KafkaConsumerOptions();
            this.setKafkaConsumerOptions(options);
            this.setKafkaConsumerOptions(options, config);

            KafkaConsumer consumer = kafkaClient.createConsumer(options);
            consumer.subscribe(new KafkaTopic(config.getTopic()), config.getListener());
            kafkaConsumerList.add(consumer);
        }
    }

    protected void setKafkaConsumerOptions(KafkaConsumerOptions options) {
        this.setConsumerOptions(options);
        options.setKeyDeserializer(keyDeserializer);
        options.setValueDeserializer(valueDeserializer);
        options.setCount(count);
        options.setDuration(Duration.ofMillis(duration));
        options.setGroup(group);
        options.setOffsetManager(offsetManager);
        options.setProp(prop);
    }

    protected void setKafkaConsumerOptions(KafkaConsumerOptions options, KafkaConsumerConfig config) {
        this.setConsumerOptions(options, config);
        if (config.getCount() != null) {
            options.setCount(config.getCount());
        }
        if (config.getDuration() != null) {
            options.setDuration(Duration.ofMillis(config.getDuration()));
        }
        if (config.getGroup() != null && !config.getGroup().isEmpty()) {
            options.setGroup(config.getGroup());
        }
        if (config.getOffsetManager() != null) {
            options.setOffsetManager(config.getOffsetManager());
        }
    }

    @Override
    public KafkaConsumer getObject() throws Exception {
        return kafkaConsumer;
    }

    @Override
    public Class<?> getObjectType() {
        return KafkaConsumer.class;
    }

    public void setKeyDeserializer(Class<?> keyDeserializer) {
        this.keyDeserializer = keyDeserializer;
    }

    public void setValueDeserializer(Class<?> valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setOffsetManager(OffsetManager offsetManager) {
        this.offsetManager = offsetManager;
    }

    public void setProp(Properties prop) {
        this.prop = prop;
    }

    public void setKafkaClient(KafkaClient kafkaClient) {
        this.kafkaClient = kafkaClient;
    }

    public void setConfigList(List<KafkaConsumerConfig> configList) {
        this.configList = configList;
    }
}
