package blue.kafka.core;

import blue.base.core.message.Consumer;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-07
 */
public interface KafkaConsumer extends Consumer<KafkaTopic> {

	/**
	 * 断开连接
	 */
	void disconnect();

}
