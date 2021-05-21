package blue.kafka.internal.core.client;

import blue.base.core.util.AssertUtil;
import blue.kafka.core.KafkaClient;
import blue.kafka.core.KafkaConsumer;
import blue.kafka.core.KafkaProducer;
import blue.kafka.core.options.KafkaClientOptions;
import blue.kafka.core.options.KafkaConsumerOptions;
import blue.kafka.core.options.KafkaProducerOptions;
import blue.kafka.internal.core.consumer.DefaultKafkaConsumer;
import blue.kafka.internal.core.producer.DefaultKafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-20
 */
public class DefaultKafkaClient implements KafkaClient {
    private final KafkaClientOptions clientOptions;

    private final List<KafkaProducer> producerList = new CopyOnWriteArrayList<>();
    private final List<KafkaConsumer> consumerList = new CopyOnWriteArrayList<>();

    public DefaultKafkaClient(KafkaClientOptions options) {
        this.clientOptions = options;
    }

    @Override
    public void disconnect() {
        for (var producer : producerList) {
            producer.disconnect();
        }
        for (var consumer : consumerList) {
            consumer.disconnect();
        }
    }

    @Override
    public KafkaProducer createProducer(KafkaProducerOptions options) {
        AssertUtil.notNull(options, "Producer Consumer Options");
        Properties prop = options.getProp();
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, clientOptions.getBroker());
        KafkaProducer producer = new DefaultKafkaProducer(options);
        producerList.add(producer);
        return producer;
    }

    @Override
    public KafkaConsumer createConsumer(KafkaConsumerOptions options) {
        AssertUtil.notNull(options, "Kafka Consumer Options");
        Properties prop = options.getProp();
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, clientOptions.getBroker());
        KafkaConsumer consumer = new DefaultKafkaConsumer(options);
        consumerList.add(consumer);
        return consumer;
    }
}
