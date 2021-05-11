package test.mqtt.core.consumer;

import blue.mqtt.core.MqttConsumerListener;
import blue.mqtt.core.MqttTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-10
 */
public class MqttReceiver implements MqttConsumerListener<String> {
    private static Logger logger = LoggerFactory.getLogger(MqttReceiver.class);

	private Map<String, String> messageMap = new HashMap<>();

	public MqttReceiver() {
	}

    @Override
    public void onReceive(MqttTopic topic, String message) {
	    messageMap.put(topic.getTopic(), message);
        logger.info("Receive, {}={}", topic, message);
    }

	public Map<String, String> getMessageMap() {
		return messageMap;
	}
}
