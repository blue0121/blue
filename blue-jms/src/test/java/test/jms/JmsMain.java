package test.jms;

import blue.jms.JmsProducer;
import blue.jms.JmsTopic;
import blue.jms.JmsType;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import test.jms.model.User;

/**
 * @author Jin Zheng
 * @since 2019-08-04
 */
public class JmsMain
{
	private static String[] SPRING = {"spring/jms.xml"};

	public JmsMain()
	{
	}

	public static void main(String[] args) throws Exception
	{
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(SPRING);
		JmsProducer producer = ctx.getBean(JmsProducer.class);
		for (int i = 0; i < 10; i++)
		{
			User user = new User(i, "blue_" + i);
			JmsTopic topic = new JmsTopic("test", JmsType.QUEUE);
			producer.sendAsync(topic, user);
		}

		Thread.sleep(5000);
		ctx.close();
	}

}
