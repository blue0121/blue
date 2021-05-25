package test.kafka.core;

import blue.base.core.util.WaitUtil;
import blue.kafka.core.KafkaClient;
import blue.kafka.core.KafkaConsumer;
import blue.kafka.core.KafkaProducer;
import blue.kafka.core.KafkaTopic;
import blue.kafka.core.options.KafkaClientOptions;
import blue.kafka.core.options.KafkaConsumerOptions;
import blue.kafka.core.options.KafkaProducerOptions;
import org.junit.jupiter.api.Assertions;
import test.kafka.core.consumer.KafkaReceiver;
import test.kafka.core.model.User;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-21
 */
public class KafkaMain {
	public KafkaMain() {
	}

	public static void main(String[] args) {
		KafkaClientOptions clientOptions = new KafkaClientOptions();
		clientOptions.setBroker("localhost:9092");
		KafkaClient client = KafkaClient.create(clientOptions);

		KafkaConsumerOptions consumerOptions = new KafkaConsumerOptions();
		consumerOptions.setGroup("test");
		KafkaReceiver receiver = new KafkaReceiver();
		KafkaConsumer consumer = client.createConsumer(consumerOptions);
		consumer.subscribe(new KafkaTopic("test"), receiver);

		KafkaProducerOptions producerOptions = new KafkaProducerOptions();
		KafkaProducer producer = client.createProducer(producerOptions);
		int count = 10;
		for (int i = 0; i < count; i++) {
			producer.sendSync(new KafkaTopic("test"), new User(i, "blue_" + i));
		}
		WaitUtil.sleep(1000);
		for (int i = 0; i < count; i++) {
			Assertions.assertTrue(receiver.getMessage().contains(new User(i, "blue_" + i)));
		}

		client.disconnect();
	}

}
