package blue.redis.internal.spring.bean;

import blue.base.core.message.Topic;
import blue.base.spring.common.ConsumerFactoryBean;
import blue.redis.core.RedisClient;
import blue.redis.core.RedisConsumer;
import blue.redis.core.RedisException;
import blue.redis.core.options.RedisConsumerOptions;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-10
 */
public class RedisConsumerFactoryBean extends ConsumerFactoryBean
		implements FactoryBean<RedisConsumer>, InitializingBean {

	private RedisClient redisClient;
	private List<RedisConsumerConfig> configList;
	private RedisConsumer redisConsumer;
	private List<RedisConsumer> consumerList = new ArrayList<>();

	public RedisConsumerFactoryBean() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (configList == null || configList.isEmpty()) {
			throw new RedisException("Redis consumer listener config is empty");
		}

		RedisConsumerOptions options = new RedisConsumerOptions();
		this.setConsumerOptions(options);
		this.redisConsumer = redisClient.createConsumer(options);
		this.createMqttConsumers();
	}

	private void createMqttConsumers() {
		for (var config : configList) {
			RedisConsumerOptions options = new RedisConsumerOptions();
			this.setConsumerOptions(options);
			this.setConsumerOptions(options, config);

			RedisConsumer consumer = redisClient.createConsumer(options);
			consumer.subscribe(new Topic(config.getTopic()), config.getListener());
			consumerList.add(consumer);
		}
	}

	@Override
	public RedisConsumer getObject() throws Exception {
		return redisConsumer;
	}

	@Override
	public Class<?> getObjectType() {
		return RedisConsumer.class;
	}

	public void setRedisClient(RedisClient redisClient) {
		this.redisClient = redisClient;
	}

	public void setConfigList(List<RedisConsumerConfig> configList) {
		this.configList = configList;
	}
}
