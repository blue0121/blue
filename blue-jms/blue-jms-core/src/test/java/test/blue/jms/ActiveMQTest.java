package test.blue.jms;

import blue.base.core.util.WaitUtil;
import blue.jms.core.JmsClient;
import blue.jms.core.JmsConsumer;
import blue.jms.core.JmsProducer;
import blue.jms.core.JmsTopic;
import blue.jms.core.JmsType;
import blue.jms.core.options.JmsClientOptions;
import blue.jms.core.options.JmsConnectionType;
import blue.jms.core.options.JmsConsumerOptions;
import blue.jms.core.options.JmsProducerOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import test.blue.jms.consumer.JmsReceiver;
import test.blue.jms.model.User;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-27
 */
@Disabled
public class ActiveMQTest {
	private static final String TEST = "test";
	//private static BrokerService brokerService;

	public ActiveMQTest() {
	}

	/*@BeforeAll
	public static void beforeAll() {
		System.setProperty("org.apache.activemq.default.directory.prefix", "target/");
		brokerService = new BrokerService();
		brokerService.setBrokerName("ActiveMQ");
		brokerService.setUseJmx(true);
		try {
			brokerService.addConnector("tcp://localhost:61616");
			brokerService.start();
			brokerService.waitUntilStarted();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterAll
	public static void afterAll() {
		try {
			brokerService.stop();
			brokerService.waitUntilStopped();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	@Test
	public void test() {
		JmsClientOptions clientOptions = new JmsClientOptions();
		clientOptions.setBroker("tcp://localhost:61616");
		clientOptions.setType(JmsConnectionType.ACTIVE_MQ)
				.setUsername(TEST)
				.setPassword(TEST);
		JmsClient client = JmsClient.create(clientOptions);

		JmsReceiver receiver = new JmsReceiver();
		JmsConsumerOptions consumerOptions = new JmsConsumerOptions();
		consumerOptions.setDefaultType(JmsType.QUEUE);
		JmsConsumer consumer = client.createConsumer(consumerOptions);
		consumer.subscribe(new JmsTopic(TEST, JmsType.QUEUE), receiver);

		JmsProducerOptions producerOptions = new JmsProducerOptions();
		JmsProducer producer = client.createProducer(producerOptions);
		int count = 10;
		for (int i = 0; i < count; i++) {
			producer.sendSync(new JmsTopic(TEST, JmsType.QUEUE), new User(i, "blue_" + i));
		}
		WaitUtil.sleep(500);
		Assertions.assertEquals(count, receiver.getMessage().size());
		for (int i = 0; i < count; i++) {
			Assertions.assertTrue(receiver.getMessage().contains(new User(i, "blue_" + i)));
		}

		client.disconnect();
	}

}
