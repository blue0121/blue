package blue.jms.core;

import blue.jms.core.options.JmsClientOptions;
import blue.jms.core.options.JmsConsumerOptions;
import blue.jms.core.options.JmsProducerOptions;
import blue.jms.internal.core.client.JmsClientFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-26
 */
public interface JmsClient {

	/**
	 * 创建JMS客户端
	 *
	 * @param options
	 * @return
	 */
	static JmsClient create(JmsClientOptions options) {
		return JmsClientFactory.create(options);
	}

	/**
	 * 断开连接
	 */
	void disconnect();

	/**
	 * 创建生产者
	 *
	 * @param options
	 * @return
	 */
	JmsProducer createProducer(JmsProducerOptions options);

	/**
	 * 创建消费者
	 *
	 * @param options
	 * @return
	 */
	JmsConsumer createConsumer(JmsConsumerOptions options);

}
