package test.mqtt.spring;

import blue.mqtt.core.MqttProducer;
import blue.mqtt.core.MqttTopic;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-10
 */
public class MqttSpringMain {
	private static String[] SPRING = {"spring/mqtt.xml"};

	public MqttSpringMain() {
	}

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(SPRING);

		MqttProducer producer = ctx.getBean(MqttProducer.class);
		for (int i = 0; i < 10; i++) {
			producer.sendSync(new MqttTopic("test/" + i), "blue_" + i);
		}

		ctx.close();
	}

}
