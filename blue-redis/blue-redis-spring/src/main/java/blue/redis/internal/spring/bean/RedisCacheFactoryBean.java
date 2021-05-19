package blue.redis.internal.spring.bean;

import blue.redis.core.RedisCache;
import blue.redis.core.RedisCacheLoader;
import blue.redis.core.RedisCacheWriter;
import blue.redis.core.RedisClient;
import blue.redis.core.options.RedisCacheMode;
import blue.redis.core.options.RedisCacheOptions;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-18
 */
public class RedisCacheFactoryBean implements FactoryBean<RedisCache<?>>, InitializingBean {
	private String id;
	private String prefix;
	private RedisCacheMode mode;
	private long ttl;
	private long localTtl;
	private long localMaxSize;
	private long timeout;
	private int retry;

	private RedisCacheLoader loader;
	private RedisCacheWriter writer;

	private RedisClient redisClient;
	private RedisCache<?> cache;

	public RedisCacheFactoryBean() {
	}

	@Override
	public RedisCache<?> getObject() throws Exception {
		return cache;
	}

	@Override
	public Class<?> getObjectType() {
		return RedisCache.class;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		RedisCacheOptions options = new RedisCacheOptions();
		options.setId(id)
				.setPrefix(prefix)
				.setMode(mode)
				.setLoader(loader)
				.setWriter(writer);
		if (ttl > 0) {
			options.setTtl(ttl);
		}
		if (localTtl > 0) {
			options.setLocalTtl(localTtl);
		}
		if (localMaxSize > 0) {
			options.setLocalMaxSize(localMaxSize);
		}
		if (timeout > 0) {
			options.setTimeout(timeout);
		}
		if (retry > 0) {
			options.setRetry(retry);
		}
		this.cache = redisClient.createCache(options);
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setMode(RedisCacheMode mode) {
		this.mode = mode;
	}

	public void setTtl(long ttl) {
		this.ttl = ttl;
	}

	public void setLocalTtl(long localTtl) {
		this.localTtl = localTtl;
	}

	public void setLocalMaxSize(long localMaxSize) {
		this.localMaxSize = localMaxSize;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}

	public void setRedisClient(RedisClient redisClient) {
		this.redisClient = redisClient;
	}

	public void setLoader(RedisCacheLoader loader) {
		this.loader = loader;
	}

	public void setWriter(RedisCacheWriter writer) {
		this.writer = writer;
	}
}
