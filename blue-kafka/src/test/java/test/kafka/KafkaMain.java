package test.kafka;

import blue.kafka.model.KafkaTopic;
import blue.kafka.producer.KafkaProducer;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import test.kafka.model.User;

/**
 * @author Jin Zheng
 * @since 2020-07-07
 */
public class KafkaMain
{
	private static String[] SPRING = {"spring/kafka.xml"};

	public KafkaMain()
	{
	}

	public static void main(String[] args) throws Exception
	{
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(SPRING);
		KafkaProducer producer = ctx.getBean(KafkaProducer.class);
		for (int i = 0; i < 10; i++)
		{
			User user = new User(i, "blue_" + i);
			KafkaTopic topic = new KafkaTopic("test");
			producer.sendAsync(topic, user);
		}

		Thread.sleep(5000);
		ctx.close();
	}
}
