package test.mqtt.core;

import blue.mqtt.core.MqttClient;
import blue.mqtt.core.MqttClientOptions;
import blue.mqtt.core.MqttConsumer;
import blue.mqtt.core.MqttConsumerOptions;
import blue.mqtt.core.MqttProducer;
import blue.mqtt.core.MqttProducerOptions;
import blue.mqtt.core.MqttTopic;
import test.mqtt.core.consumer.MqttReceiver;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-10
 */
public class MqttMain {
	public MqttMain() {
	}

    public static void main(String[] args) {
        MqttClientOptions clientOptions = new MqttClientOptions();
        clientOptions.setId("client")
                .setBroker("tcp://localhost:1883")
                .setUsername("test")
                .setPassword("test")
                .setClientId("client_$RANDOM")
                .setCount(2);
        MqttClient client = MqttClient.create(clientOptions);

        MqttConsumerOptions consumerOptions = new MqttConsumerOptions();
        consumerOptions.setId("consumer");
        MqttConsumer consumer = client.createConsumer(consumerOptions);
        consumer.subscribe(new MqttTopic("test/+"), new MqttReceiver());

        MqttProducerOptions producerOptions = new MqttProducerOptions();
        producerOptions.setId("producer");
        MqttProducer producer = client.createProducer(producerOptions);
        for (int i = 0; i < 10; i++) {
            producer.sendSync(new MqttTopic("test/" + i), "blue_" + i);
        }

        client.disconnect();
    }

}
