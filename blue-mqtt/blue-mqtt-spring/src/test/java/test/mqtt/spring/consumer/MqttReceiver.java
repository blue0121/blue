package test.mqtt.spring.consumer;

import blue.mqtt.core.MqttConsumerListener;
import blue.mqtt.core.MqttTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-10
 */
public class MqttReceiver implements MqttConsumerListener<String> {
    private static Logger logger = LoggerFactory.getLogger(MqttReceiver.class);

	public MqttReceiver() {
	}

    @Override
    public void onReceive(MqttTopic topic, String message) {
        logger.info("Receive, {}={}", topic, message);
    }
}
