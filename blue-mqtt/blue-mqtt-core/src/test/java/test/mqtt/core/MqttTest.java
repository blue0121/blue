package test.mqtt.core;

import blue.base.core.util.WaitUtil;
import blue.mqtt.core.MqttClient;
import blue.mqtt.core.MqttConsumer;
import blue.mqtt.core.MqttProducer;
import blue.mqtt.core.MqttTopic;
import blue.mqtt.core.options.MqttClientOptions;
import blue.mqtt.core.options.MqttConsumerOptions;
import blue.mqtt.core.options.MqttProducerOptions;
import org.apache.activemq.broker.BrokerService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.mqtt.core.consumer.MqttReceiver;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-11
 */
public class MqttTest {
    private static BrokerService brokerService;



	public MqttTest() {
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
        MqttClientOptions clientOptions = new MqttClientOptions();
        clientOptions.setId("client")
                .setBroker("tcp://localhost:2883")
                .setUsername("test")
                .setPassword("test")
                .setClientId("client_$RANDOM")
                .setCount(2);
        MqttClient client = MqttClient.create(clientOptions);

        var receiver = new MqttReceiver();
        MqttConsumerOptions consumerOptions = new MqttConsumerOptions();
        consumerOptions.setId("consumer");
        MqttConsumer consumer = client.createConsumer(consumerOptions);
        consumer.subscribe(new MqttTopic("test/+"), receiver);

        MqttProducerOptions producerOptions = new MqttProducerOptions();
        producerOptions.setId("producer");
        MqttProducer producer = client.createProducer(producerOptions);
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

        client.disconnect();
    }

}
