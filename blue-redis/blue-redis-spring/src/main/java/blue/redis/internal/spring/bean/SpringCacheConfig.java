package blue.redis.internal.spring.bean;

import blue.redis.core.options.RedisCacheMode;
import blue.redis.core.options.RedisCacheOptions;

/**
 * @author Jin Zheng
 * @since 2020-12-05
 */
public class SpringCacheConfig {
	private String name;
	private RedisCacheMode mode;
	private long ttl;
	private long localTtl;
	private long localMaxSize;
	private long timeout;

	public SpringCacheConfig() {
		this.name = "default";
	}

	public SpringCacheConfig copy() {
		SpringCacheConfig config = new SpringCacheConfig();
		config.name = this.name;
		config.ttl = this.ttl;
		config.localTtl = this.localTtl;
		config.localMaxSize = this.localMaxSize;
		config.timeout = this.timeout;
		return config;
	}

	@Override
	public String toString() {
		return String.format("%s{name: %s, ttl: %dms, localTtl: %dms, localMaxSize: %dms, timeout: %dms}",
				this.getClass().getSimpleName(), name, ttl, localTtl, localMaxSize, timeout);
	}

	public RedisCacheOptions merge(RedisCacheOptions options) {
		RedisCacheOptions copy = new RedisCacheOptions();
		copy.setId(name);
		copy.setPrefix(options.getPrefix() + name);
		copy.setMode(mode != null ? mode : options.getMode());
		copy.setTtl(ttl > 0 ? ttl : options.getTtl());
		copy.setLocalTtl(localTtl > 0 ? localTtl : options.getLocalTtl());
		copy.setLocalMaxSize(localMaxSize > 0 ? localMaxSize : options.getLocalMaxSize());
		copy.setTimeout(timeout > 0 ? timeout : options.getTimeout());
		copy.setRetry(options.getRetry());
		copy.check();
		return copy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RedisCacheMode getMode() {
		return mode;
	}

	public void setMode(RedisCacheMode mode) {
		this.mode = mode;
	}

	public long getTtl() {
		return ttl;
	}

	public void setTtl(long ttl) {
		this.ttl = ttl;
	}

	public long getLocalTtl() {
		return localTtl;
	}

	public void setLocalTtl(long localTtl) {
		this.localTtl = localTtl;
	}

	public long getLocalMaxSize() {
		return localMaxSize;
	}

	public void setLocalMaxSize(long localMaxSize) {
		this.localMaxSize = localMaxSize;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
}
