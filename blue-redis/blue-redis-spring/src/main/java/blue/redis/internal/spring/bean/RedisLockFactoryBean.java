package blue.redis.internal.spring.bean;

import blue.redis.core.RedisClient;
import blue.redis.core.RedisLock;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-19
 */
public class RedisLockFactoryBean implements FactoryBean<RedisLock>, InitializingBean {

	private RedisClient redisClient;
	private RedisLock lock;

	public RedisLockFactoryBean() {
	}

	@Override
	public RedisLock getObject() throws Exception {
		return lock;
	}

	@Override
	public Class<?> getObjectType() {
		return RedisLock.class;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.lock = redisClient.createLock();
	}

	public void setRedisClient(RedisClient redisClient) {
		this.redisClient = redisClient;
	}
}
