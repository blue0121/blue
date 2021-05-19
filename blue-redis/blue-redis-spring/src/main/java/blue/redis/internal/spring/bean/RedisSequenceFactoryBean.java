package blue.redis.internal.spring.bean;

import blue.redis.core.RedisClient;
import blue.redis.core.RedisSequence;
import blue.redis.core.options.RedisSequenceMode;
import blue.redis.core.options.RedisSequenceOptions;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-18
 */
public class RedisSequenceFactoryBean implements FactoryBean<RedisSequence>, InitializingBean {
	private String id;
	private String key;
	private RedisSequenceMode mode;
	private int length;
	private String prefix;
	private String suffix;

	private RedisClient redisClient;
	private RedisSequence sequence;

	public RedisSequenceFactoryBean() {
	}

	@Override
	public RedisSequence getObject() throws Exception {
		return sequence;
	}

	@Override
	public Class<?> getObjectType() {
		return RedisSequence.class;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		RedisSequenceOptions options = new RedisSequenceOptions();
		options.setId(id)
				.setKey(key)
				.setMode(mode)
				.setLength(length)
				.setPrefix(prefix)
				.setSuffix(suffix);
		this.sequence = redisClient.createSequence(options);
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setMode(RedisSequenceMode mode) {
		this.mode = mode;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public void setRedisClient(RedisClient redisClient) {
		this.redisClient = redisClient;
	}
}
