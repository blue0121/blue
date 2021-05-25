package test.kafka.spring;

import blue.base.core.util.WaitUtil;
import blue.kafka.core.KafkaProducer;
import blue.kafka.core.KafkaTopic;
import org.junit.jupiter.api.Assertions;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import test.kafka.spring.consumer.KafkaReceiver;
import test.kafka.spring.model.User;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-25
 */
public class KafkaSpringMain {
	private static String[] SPRING = {"spring/kafka.xml"};

	public KafkaSpringMain() {
	}

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(SPRING);
		KafkaProducer producer = ctx.getBean(KafkaProducer.class);
		KafkaReceiver receiver = ctx.getBean(KafkaReceiver.class);

		int count = 10;
		for (int i = 0; i < count; i++) {
			producer.sendSync(new KafkaTopic("test"), new User(i, "blue_" + i));
		}
		WaitUtil.sleep(1000);
		for (int i = 0; i < count; i++) {
			Assertions.assertTrue(receiver.getMessage().contains(new User(i, "blue_" + i)));
		}
		ctx.close();
	}

}
