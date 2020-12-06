package blue.internal.redis.cache;

import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Jin Zheng
 * @since 2020-12-05
 */
public class RedisCacheManager implements CacheManager
{
	private static Logger logger = LoggerFactory.getLogger(RedisCacheManager.class);

	private RedissonClient redisson;
	private String prefix;
	private boolean allowNullValues = true;

	private Map<String, CacheConfig> configMap = new HashMap<>();
	private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

	public RedisCacheManager()
	{
	}

	@Override
	public Cache getCache(String name)
	{
		Cache cache = cacheMap.get(name);
		if (cache != null)
			return cache;

		var config = this.getCacheConfig(name);
		return this.createCache(name, config);
	}

	private CacheConfig getCacheConfig(String name)
	{
		return configMap.computeIfAbsent(name, k -> new CacheConfig());
	}

	private Cache createCache(String name, CacheConfig config)
	{
		return cacheMap.computeIfAbsent(name, k ->
		{
			var options = LocalCachedMapOptions.defaults()
					.cacheSize(config.getMaxSize())
					.evictionPolicy(LocalCachedMapOptions.EvictionPolicy.LFU)
					.reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.CLEAR)
					.maxIdle(config.getMaxIdleTime())
					.timeToLive(config.getTtl());
			var map = redisson.getLocalCachedMap(name, options);
			map.expire(config.getTtl(), TimeUnit.MILLISECONDS);
			return new RedisLocalCache(map, allowNullValues);
		});
	}

	@Override
	public Collection<String> getCacheNames()
	{
		return configMap.keySet();
	}

	public RedissonClient getRedisson()
	{
		return redisson;
	}

	public void setRedisson(RedissonClient redisson)
	{
		this.redisson = redisson;
	}

	public String getPrefix()
	{
		return prefix;
	}

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	public boolean isAllowNullValues()
	{
		return allowNullValues;
	}

	public void setAllowNullValues(boolean allowNullValues)
	{
		this.allowNullValues = allowNullValues;
	}

	public Map<String, CacheConfig> getConfigMap()
	{
		return Map.copyOf(configMap);
	}

	public void setConfigMap(Map<String, CacheConfig> configMap)
	{
		if (configMap != null && !configMap.isEmpty())
		{
			this.configMap.putAll(configMap);
		}
	}

	public Map<String, Cache> getCacheMap()
	{
		return Map.copyOf(cacheMap);
	}
}
