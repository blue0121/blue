package blue.redis.core;

import blue.redis.core.options.RedisClientOptions;
import blue.redis.core.options.RedisConsumerOptions;
import blue.redis.core.options.RedisProducerOptions;
import blue.redis.core.options.RedisSequenceOptions;
import blue.redis.internal.core.client.DefaultRedisClient;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-11
 */
public interface RedisClient {

	/**
	 * 创建Redis客户端
	 *
	 * @param options
	 * @return
	 */
	static RedisClient create(RedisClientOptions options) {
		options.check();
		DefaultRedisClient client = new DefaultRedisClient(options);
		return client;
	}

	/**
	 * 断开连接
	 */
	void disconnect();

	/**
	 * 创建Redis生产者
	 *
	 * @param options
	 * @return
	 */
	RedisProducer createProducer(RedisProducerOptions options);

	/**
	 * 创建Redis消费者
	 *
	 * @param options
	 * @return
	 */
	RedisConsumer createConsumer(RedisConsumerOptions options);

	/**
	 * 创建Redis分布式锁
	 *
	 * @return
	 */
	RedisLock createLock();

	/**
	 * 创建自增序列
	 *
	 * @param options
	 * @return
	 */
	Sequence createSequence(RedisSequenceOptions options);

}
