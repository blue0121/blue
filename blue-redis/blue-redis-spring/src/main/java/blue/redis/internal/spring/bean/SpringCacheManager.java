package blue.redis.internal.spring.bean;

import blue.base.core.util.AssertUtil;
import blue.redis.core.RedisCache;
import blue.redis.core.RedisClient;
import blue.redis.core.options.RedisCacheMode;
import blue.redis.core.options.RedisCacheOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Jin Zheng
 * @since 2020-12-05
 */
public class SpringCacheManager implements CacheManager, InitializingBean {
	private static Logger logger = LoggerFactory.getLogger(SpringCacheManager.class);

	private RedisClient redisClient;
	private RedisCacheOptions options = new RedisCacheOptions();
	private List<SpringCacheConfig> configList;
	private boolean allowNullValues = true;

	private final ConcurrentMap<String, RedisCacheOptions> optionsMap = new ConcurrentHashMap<>();
	private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

	public SpringCacheManager() {
	}

	@Override
	public Cache getCache(String name) {
		Cache cache = cacheMap.get(name);
		if (cache != null) {
			return cache;
		}

		var config = this.getCacheOptions(name);
		return this.getOrCreateCache(name, config);
	}

	private RedisCacheOptions getCacheOptions(String name) {
		return optionsMap.computeIfAbsent(name, k ->
		{
			SpringCacheConfig config = new SpringCacheConfig();
			config.setName(name);
			RedisCacheOptions opt = config.merge(options);
			return opt;
		});
	}

	private Cache getOrCreateCache(String name, RedisCacheOptions options) {
		return cacheMap.computeIfAbsent(name, k ->
		{
			RedisCache<Object> redisCache = redisClient.createCache(options);
			return new SpringCache(name, redisCache, allowNullValues);
		});
	}

	@Override
	public Collection<String> getCacheNames() {
		return optionsMap.keySet();
	}

	@Override
	public void afterPropertiesSet() {
		AssertUtil.notNull(redisClient, "Redis Client");
		options.check();
		if (configList != null && !configList.isEmpty()) {
			for (var config : configList) {
				var opt = config.merge(options);
				optionsMap.put(config.getName(), opt);
			}
		}
	}

	public void setRedisClient(RedisClient redisClient) {
		this.redisClient = redisClient;
	}

	public void setId(String id) {
		options.setId(id);
	}

	public void setPrefix(String prefix) {
		options.setPrefix(prefix);
	}

	public void setMode(RedisCacheMode mode) {
		options.setMode(mode);
	}

	public void setTtl(long ttl) {
		options.setTtl(ttl);
	}

	public void setLocalTtl(long localTtl) {
		options.setLocalTtl(localTtl);
	}

	public void setLocalMaxSize(long size) {
		options.setLocalMaxSize(size);
	}

	public void setTimeout(long timeout) {
		options.setTimeout(timeout);
	}

	public void setConfigList(List<SpringCacheConfig> configList) {
		this.configList = configList;
	}
}
