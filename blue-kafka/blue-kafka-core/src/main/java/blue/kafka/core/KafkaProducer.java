package blue.kafka.core;

import blue.base.core.message.Producer;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-20
 */
public interface KafkaProducer extends Producer<KafkaTopic> {

	/**
	 * 断开连接
	 */
	void disconnect();

}
