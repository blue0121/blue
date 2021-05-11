package test.mqtt.spring;

import blue.base.core.util.WaitUtil;
import blue.mqtt.core.MqttProducer;
import blue.mqtt.core.MqttTopic;
import org.apache.activemq.broker.BrokerService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import test.mqtt.spring.consumer.MqttReceiver;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-10
 */
@SpringJUnitConfig(locations = {"classpath:spring/mqtt.xml"})
public class MqttSpringTest {

	private static BrokerService brokerService;

    private MqttProducer producer;
    private MqttReceiver receiver;

	public MqttSpringTest() {
	}

	@BeforeAll
	public static void beforeAll() {
		System.setProperty("org.apache.activemq.default.directory.prefix", "target/");
		brokerService = new BrokerService();
		brokerService.setBrokerName("ActiveMQ");
		brokerService.setUseJmx(false);
		try {
			brokerService.addConnector("mqtt://localhost:2883");
			brokerService.start();
			brokerService.waitUntilStarted();
		} catch (Exception e) {
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
	}

	@Test
	public void test() {
		int count = 10;
		for (int i = 0; i < count; i++) {
			producer.sendSync(new MqttTopic("test/" + i), "blue_" + i);
		}
		WaitUtil.sleep(500);
		var map = receiver.getMessageMap();
		Assertions.assertEquals(count, map.size());
		for (int i = 0; i < count; i++) {
			Assertions.assertEquals("blue_" + i, map.get("test/" + i));
		}
	}

	@Autowired
    public void setProducer(MqttProducer producer) {
        this.producer = producer;
    }

    @Autowired
    public void setReceiver(MqttReceiver receiver) {
        this.receiver = receiver;
    }
}
