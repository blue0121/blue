package blue.internal.redis.cache;

import blue.core.util.AssertUtil;
import blue.redis.L2Cache;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Jin Zheng
 * @since 2020-12-05
 */
public class RedisCacheManager implements CacheManager, InitializingBean
{
	private static Logger logger = LoggerFactory.getLogger(RedisCacheManager.class);

	private RedissonClient redisson;
	private String prefix;
	private DefaultCacheConfig config = new DefaultCacheConfig();
	private boolean allowNullValues = true;

	private final ConcurrentMap<String, DefaultCacheConfig> configMap = new ConcurrentHashMap<>();
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

	private DefaultCacheConfig getCacheConfig(String name)
	{
		return configMap.computeIfAbsent(name, k ->
		{
			DefaultCacheConfig c = config.copy();
			c.setName(name);
			return c;
		});
	}

	private Cache createCache(String name, DefaultCacheConfig config)
	{
		return cacheMap.computeIfAbsent(name, k ->
		{
			String keyPrefix = prefix + name;
			L2Cache cache = L2Cache.create(redisson, keyPrefix, config);
			return new RedisLocalCache(cache, keyPrefix, allowNullValues);
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

	public DefaultCacheConfig getConfig()
	{
		return config.copy();
	}

	public void setDefaultTtl(long ttl)
	{
		if (ttl > 0)
		{
			config.setTtl(ttl);
		}
	}

	public void setDefaultLocalTtl(long localTtl)
	{
		if (localTtl > 0)
		{
			config.setLocalTtl(localTtl);
		}
	}

	public void setDefaultLocalMaxSize(long size)
	{
		if (size > 0)
		{
			config.setLocalMaxSize(size);
		}
	}

	public void setDefaultTimeout(long timeout)
	{
		if (timeout > 0)
		{
			config.setTimeout(timeout);
		}
	}

	public Map<String, DefaultCacheConfig> getConfigMap()
	{
		return Map.copyOf(configMap);
	}

	public void setConfigList(List<DefaultCacheConfig> configList)
	{
		if (configList != null && !configList.isEmpty())
		{
			for (var config : configList)
			{
				this.configMap.put(config.name(), config);
			}
		}
	}

	public Map<String, Cache> getCacheMap()
	{
		return Map.copyOf(cacheMap);
	}

	@Override
	public void afterPropertiesSet()
	{
		AssertUtil.notNull(redisson, "Redisson");
		AssertUtil.notNull(prefix, "Prefix");
		logger.info("config: {}", config);
		for (var entry : configMap.entrySet())
		{
			logger.info("config: {}", entry.getValue());
		}
	}
}
