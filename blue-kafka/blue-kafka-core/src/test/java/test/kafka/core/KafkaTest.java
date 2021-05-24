package test.kafka.core;

import blue.base.core.util.WaitUtil;
import blue.kafka.core.KafkaClient;
import blue.kafka.core.KafkaConsumer;
import blue.kafka.core.KafkaProducer;
import blue.kafka.core.KafkaTopic;
import blue.kafka.core.options.KafkaClientOptions;
import blue.kafka.core.options.KafkaConsumerOptions;
import blue.kafka.core.options.KafkaProducerOptions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import test.kafka.core.consumer.KafkaReceiver;
import test.kafka.core.model.User;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-24
 */
@Disabled
public class KafkaTest {
	private static Logger logger = LoggerFactory.getLogger(KafkaTest.class);
	private static final String TOPIC = "test";

	private static EmbeddedKafkaBroker broker;

	public KafkaTest() {
	}

	@BeforeAll
	public static void beforeAll() {
		broker = new EmbeddedKafkaBroker(1, true);
		broker.afterPropertiesSet();
		logger.info("Embedded kafka broker: {}", broker.getBrokersAsString());
		logger.info("Embedded zookeeper: {}", broker.getZookeeperConnectionString());
	}

	@AfterAll
	public static void afterAll() {
		if (broker != null) {
			broker.destroy();
		}
	}

	@Test
	public void test() {
		KafkaClientOptions clientOptions = new KafkaClientOptions();
		clientOptions.setBroker(broker.getBrokersAsString());
		KafkaClient client = KafkaClient.create(clientOptions);

		KafkaConsumerOptions consumerOptions = new KafkaConsumerOptions();
		consumerOptions.setGroup("test");
		KafkaReceiver receiver = new KafkaReceiver();
		KafkaConsumer consumer = client.createConsumer(consumerOptions);
		consumer.subscribe(new KafkaTopic(TOPIC), receiver);

		KafkaProducerOptions producerOptions = new KafkaProducerOptions();
		KafkaProducer producer = client.createProducer(producerOptions);
		int count = 10;
		for (int i = 0; i < count; i++) {
			producer.sendSync(new KafkaTopic(TOPIC), new User(i, "blue_" + i));
		}
		WaitUtil.sleep(3000);
		for (int i = 0; i < count; i++) {
			Assertions.assertTrue(receiver.getMessage().contains(new User(i, "blue_" + i)));
		}

		client.disconnect();
	}

}
