package blue.mqtt.internal.spring.bean;

import blue.base.spring.common.ConsumerConfig;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-10
 */
public class MqttConsumerConfig extends ConsumerConfig {
    private Integer qos;

	public MqttConsumerConfig() {
	}

    public Integer getQos() {
        return qos;
    }

    public void setQos(Integer qos) {
        this.qos = qos;
    }
}
