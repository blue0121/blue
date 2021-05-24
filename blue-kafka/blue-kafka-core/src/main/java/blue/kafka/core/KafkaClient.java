package blue.kafka.core;

import blue.kafka.core.options.KafkaClientOptions;
import blue.kafka.core.options.KafkaConsumerOptions;
import blue.kafka.core.options.KafkaProducerOptions;
import blue.kafka.internal.core.client.DefaultKafkaClient;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-20
 */
public interface KafkaClient {

	/**
	 * 创建Kafka客户端
	 *
	 * @param options
	 * @return
	 */
	static KafkaClient create(KafkaClientOptions options) {
		KafkaClient client = new DefaultKafkaClient(options);
		return client;
	}

	/**
	 * 断开连接
	 */
	void disconnect();

	/**
	 * 创建Kafka生产者
	 *
	 * @param options
	 * @return
	 */
	KafkaProducer createProducer(KafkaProducerOptions options);

	/**
	 * 创建Kafka消费者
	 *
	 * @param options
	 * @return
	 */
	KafkaConsumer createConsumer(KafkaConsumerOptions options);

}
