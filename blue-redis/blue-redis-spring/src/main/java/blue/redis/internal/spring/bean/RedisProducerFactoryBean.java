package blue.redis.internal.spring.bean;

import blue.base.core.message.ProducerListener;
import blue.redis.core.RedisClient;
import blue.redis.core.RedisProducer;
import blue.redis.core.options.RedisProducerOptions;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-13
 */
public class RedisProducerFactoryBean implements FactoryBean<RedisProducer>, InitializingBean {
	private String id;
	private ProducerListener producerListener;

	private RedisClient redisClient;
	private RedisProducer redisProducer;

	public RedisProducerFactoryBean() {
	}

	@Override
	public RedisProducer getObject() throws Exception {
		return redisProducer;
	}

	@Override
	public Class<?> getObjectType() {
		return RedisProducer.class;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		RedisProducerOptions options = new RedisProducerOptions();
		options.setId(id)
				.setProducerListener(producerListener);
		this.redisProducer = redisClient.createProducer(options);
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setProducerListener(ProducerListener producerListener) {
		this.producerListener = producerListener;
	}

	public void setRedisClient(RedisClient redisClient) {
		this.redisClient = redisClient;
	}
}
