package blue.redis.internal.spring.bean;

import blue.redis.core.RedisClient;
import blue.redis.core.options.RedisClientOptions;
import blue.redis.core.options.RedisConnectionMode;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-13
 */
public class RedisClientFactoryBean implements FactoryBean<RedisClient>, InitializingBean, DisposableBean {
	private String id;
	private Object codec;
	private RedisConnectionMode mode;
	private String broker;
	private String masterName;
	private int database;
	private String password;
	private int timeoutMillis;
	private int subscriptionConnectionPoolSize;
	private int connectionPoolSize;
	private int retry;

	private RedisClient redisClient;

	public RedisClientFactoryBean() {
	}

	@Override
	public RedisClient getObject() throws Exception {
		return redisClient;
	}

	@Override
	public Class<?> getObjectType() {
		return RedisClient.class;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		RedisClientOptions options = new RedisClientOptions();
		options.setId(id)
				.setCodec(codec)
				.setMode(mode)
				.setBroker(broker)
				.setMasterName(masterName)
				.setDatabase(database)
				.setPassword(password)
				.setTimeoutMillis(timeoutMillis)
				.setSubscriptionConnectionPoolSize(subscriptionConnectionPoolSize)
				.setConnectionPoolSize(connectionPoolSize)
				.setRetry(retry);
		this.redisClient = RedisClient.create(options);
	}

	@Override
	public void destroy() throws Exception {
		if (redisClient != null) {
			redisClient.disconnect();
		}
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCodec(Object codec) {
		this.codec = codec;
	}

	public void setMode(RedisConnectionMode mode) {
		this.mode = mode;
	}

	public void setBroker(String broker) {
		this.broker = broker;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setTimeoutMillis(int timeoutMillis) {
		this.timeoutMillis = timeoutMillis;
	}

	public void setSubscriptionConnectionPoolSize(int subscriptionConnectionPoolSize) {
		this.subscriptionConnectionPoolSize = subscriptionConnectionPoolSize;
	}

	public void setConnectionPoolSize(int connectionPoolSize) {
		this.connectionPoolSize = connectionPoolSize;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}
}
