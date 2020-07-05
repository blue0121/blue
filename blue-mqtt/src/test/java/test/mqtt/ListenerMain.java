package test.mqtt;

import blue.mqtt.model.MqttQos;
import blue.mqtt.model.MqttTopic;
import blue.mqtt.producer.MqttProducer;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import test.mqtt.model.User;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-29
 */
public class ListenerMain
{
	private static String[] SPRING = {"spring/mqtt.xml"};

	public ListenerMain()
	{
	}

	public static void main(String[] args) throws Exception
	{
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(SPRING);

		MqttProducer producer = ctx.getBean(MqttProducer.class);
		for (int i = 0; i < 10; i++)
		{
			producer.sendAsync(new MqttTopic("test/" + i, MqttQos.AT_MOST_ONCE), new User(i, "blue" + i));
		}

		Thread.sleep(10000);
		ctx.close();
	}

}
