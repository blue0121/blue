package blue.internal.redis.cache;

import blue.core.util.AssertUtil;
import blue.core.util.StringUtil;
import blue.redis.cache.L2Cache;
import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import org.redisson.api.BatchOptions;
import org.redisson.api.RBatch;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.api.listener.StatusListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Jin Zheng
 * @since 1.0 2020-12-08
 */
public class DefaultL2Cache implements L2Cache,
		AsyncCacheLoader<String, LocalValueWrapper>, Expiry<String, LocalValueWrapper>,
		MessageListener<String>, StatusListener, InitializingBean, DisposableBean
{
	private static Logger logger = LoggerFactory.getLogger(DefaultL2Cache.class);
	private static final String SPLIT = ":";
	private static final String KEY_SPLIT = ";:;";
	private static final String SET_SUFFIX = "~set";
	private static final String TOPIC_PREFIX = "local_cache_remove:";

	private RedissonClient redisson;
	private String keyPrefix;
	private long ttl;
	private long localTtl;
	private long localMaxSize;
	private long timeout;
	private final Map<String, Long> localTtlMap = new HashMap<>();
	private AsyncLoadingCache<String, LocalValueWrapper> cache;

	public DefaultL2Cache()
	{
	}

	@Override
	public String keyPrefix()
	{
		return keyPrefix;
	}

	@Override
	public long ttl()
	{
		return ttl;
	}

	@Override
	public long localTtl()
	{
		return localTtl;
	}

	@Override
	public long localMaxSize()
	{
		return localMaxSize;
	}

	@Override
	public long timeout()
	{
		return timeout;
	}

	public void setRedisson(RedissonClient redisson)
	{
		this.redisson = redisson;
	}

	public void setKeyPrefix(String keyPrefix)
	{
		this.keyPrefix = keyPrefix;
	}

	public void setTtl(long ttl)
	{
		this.ttl = ttl;
	}

	public void setLocalTtl(long localTtl)
	{
		this.localTtl = localTtl;
	}

	public void setLocalMaxSize(long localMaxSize)
	{
		this.localMaxSize = localMaxSize;
	}

	public void setTimeout(long timeout)
	{
		this.timeout = timeout;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getSync(String name)
	{
		LocalValueWrapper wrapper = cache.synchronous().get(name);
		if (wrapper == null)
			return null;

		return (T) wrapper.value();
	}

	@Override
	public <T> Map<String, T> getSync(String...names)
	{
		Map<String, LocalValueWrapper> wrapperMap = cache.synchronous().getAll(Arrays.asList(names));
		return this.unwrap(wrapperMap);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> void getAsync(Consumer<Map.Entry<String, T>> f, String name)
	{
		cache.get(name)
				.thenApply(v -> new DefaultMapEntry<>(name, (T) v.value()))
				.thenAccept(f);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> void getAsync(Consumer<Map<String, T>> f, String... names)
	{
		cache.getAll(Arrays.asList(names))
				.thenApply(m -> (Map<String, T>)this.unwrap(m))
				.thenAccept(f);
	}

	@SuppressWarnings("unchecked")
	private <T> Map<String, T> unwrap(Map<String, LocalValueWrapper> wrapperMap)
	{
		Map<String, T> map = new HashMap<>();
		if (wrapperMap == null || wrapperMap.isEmpty())
			return map;

		for (Map.Entry<String, LocalValueWrapper> entry : wrapperMap.entrySet())
		{
			map.put(entry.getKey(), (T) entry.getValue().value());
		}
		return map;
	}

	@Override
	public void setAsync(List<String> nameList, List<?> valueList, long ttl, long localTtl)
	{
		RBatch batch = this.setOperation(nameList, valueList, ttl, localTtl);
		this.putLocalTtl(localTtl, nameList.toArray(new String[0]));
		batch.executeAsync().thenAccept(r -> this.logOperation("set", nameList));
	}

	@Override
	public void setSync(List<String> nameList, List<?> valueList, long ttl, long localTtl)
	{
		RBatch batch = this.setOperation(nameList, valueList, ttl, localTtl);
		this.putLocalTtl(localTtl, nameList.toArray(new String[0]));
		batch.execute();
		this.logOperation("set", nameList);
	}

	private RBatch setOperation(List<String> nameList, List<?> valueList, long ttl, long localTtl)
	{
		AssertUtil.notEmpty(nameList, "nameList");
		AssertUtil.notEmpty(valueList, "valueList");
		AssertUtil.positive(ttl, "TTL");
		AssertUtil.notEqual(nameList.size(), valueList.size(), "nameList.size() != valueList.size()");

		List<String> keyList = new ArrayList<>();
		RBatch batch = this.createBatch();
		for (var i = 0; i < nameList.size(); i++)
		{
			String key = this.buildKey(nameList.get(i));
			keyList.add(key);
			batch.getBucket(key).setAsync(valueList.get(i), ttl, TimeUnit.MILLISECONDS);
		}
		batch.getSet(this.buildSetKey()).addAllAsync(nameList);
		this.pubOperation(batch, nameList);
		return batch;
	}

	@Override
	public void removeAsync(String... names)
	{
		RBatch batch = this.removeOperation(names);
		batch.executeAsync().thenAccept(r ->
		{
			this.removeLocalTtl(names);
			this.logOperation("remove", names);
		});
	}

	@Override
	public void removeSync(String... names)
	{
		RBatch batch = this.removeOperation(names);
		batch.execute();
		this.removeLocalTtl(names);
		this.logOperation("remove", names);
	}

	private RBatch removeOperation(String...names)
	{
		AssertUtil.notEmpty(names, "Names");
		RBatch batch = this.createBatch();
		String[] keys = this.buildKeys(names);
		batch.getKeys().deleteAsync(keys);
		List<String> nameList = Arrays.asList(names);
		batch.getSet(this.buildSetKey()).removeAllAsync(nameList);
		this.pubOperation(batch, Arrays.asList(names));
		return batch;
	}

	@Override
	public void clearAsync()
	{
		String setKey = this.buildSetKey();
		RSet<String> rset = redisson.getSet(setKey);
		rset.readAllAsync().thenAccept(nameSet ->
		{
			RBatch batch = this.clearOperation(setKey, nameSet);
			batch.executeAsync().thenAccept(r ->
			{
				this.logOperation("clear", nameSet);
				cache.synchronous().invalidateAll();
			});
		});
	}

	@Override
	public void clearSync()
	{
		String setKey = this.buildSetKey();
		RSet<String> rset = redisson.getSet(setKey);
		Set<String> nameSet = rset.readAll();
		RBatch batch = this.clearOperation(setKey, nameSet);
		batch.execute();
		this.logOperation("clear", nameSet);
		cache.synchronous().invalidateAll();
	}

	private RBatch clearOperation(String setKey, Set<String> nameSet)
	{
		RBatch batch = this.createBatch();
		String[] names = nameSet.toArray(new String[0]);
		String[] keys = this.buildKeys(names);
		batch.getKeys().deleteAsync(keys);
		batch.getSet(setKey).deleteAsync();
		return batch;
	}

	private RBatch createBatch()
	{
		BatchOptions options = BatchOptions.defaults()
				.executionMode(BatchOptions.ExecutionMode.IN_MEMORY)
				.skipResult()
				//.syncSlaves(2, timeout, TimeUnit.MILLISECONDS)
				.responseTimeout(timeout, TimeUnit.MILLISECONDS)
				.retryInterval(timeout, TimeUnit.MILLISECONDS)
				.retryAttempts(2);
		return redisson.createBatch(options);
	}

	private String buildKey(String name)
	{
		AssertUtil.notEmpty(name, "Name");
		StringBuilder sb = new StringBuilder(keyPrefix.length() + SPLIT.length() + name.length());
		sb.append(keyPrefix).append(SPLIT).append(name);
		return sb.toString();
	}

	private String[] buildKeys(String... names)
	{
		String[] keys = new String[names.length];
		for (var i = 0; i < names.length; i++)
		{
			keys[i] = this.buildKey(names[i]);
		}
		return keys;
	}

	private String buildSetKey()
	{
		return keyPrefix + SET_SUFFIX;
	}

	private String buildTopic()
	{
		return TOPIC_PREFIX + keyPrefix;
	}

	private void logOperation(String operation, Collection<String> nameList)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Redis {}, prefix: {}, names: {}", operation, keyPrefix, nameList);
		}
	}

	private void logOperation(String operation, String...names)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Redis {}, prefix: {}, names: {}", operation, keyPrefix, Arrays.toString(names));
		}
	}

	private void pubOperation(RBatch batch, Collection<String> nameList)
	{
		var messages = StringUtil.join(nameList, KEY_SPLIT);
		var topic = this.buildTopic();
		batch.getTopic(topic).publishAsync(messages)
				.thenAccept(r -> logger.info("Publish, channel: {}, message: {}", topic, messages));
	}

	/**
	 * localTtl operator
	 *
	 * @param name
	 * @return
	 */
	private long getLocalTtl(String name)
	{
		AssertUtil.notEmpty(name, "Name");
		Long localTtl = localTtlMap.get(name);
		if (localTtl != null && localTtl.longValue() > 0)
			return localTtl.longValue();

		return this.localTtl;
	}
	private void removeLocalTtl(String...names)
	{
		for (var name : names)
		{
			localTtlMap.remove(name);
		}
	}
	private void putLocalTtl(long localTtl, String...names)
	{
		if (localTtl <= 0)
			return;

		for (var name : names)
		{
			localTtlMap.put(name, localTtl);
		}
	}

	/**
	 * Caffeine::AsyncCacheLoader interface
	 *
	 * @param name
	 * @return
	 * @throws Exception
	 */
	@Override
	public CompletableFuture<LocalValueWrapper> asyncLoad(String name, Executor executor)
	{
		var key = this.buildKey(name);
		var localTtl = this.getLocalTtl(name);
		CompletionStage<LocalValueWrapper> future = redisson.getBucket(key)
				.getAsync()
				.thenApply(v ->
				{
					this.logOperation("get", name);
					return new LocalValueWrapper(v, localTtl);
				});
		return (CompletableFuture<LocalValueWrapper>) future;
	}
	@Override
	public CompletableFuture<Map<String, LocalValueWrapper>> asyncLoadAll(Iterable<? extends String> keys, Executor executor)
	{
		List<String> keyList = new ArrayList<>();
		List<String> nameList = new ArrayList<>();
		for (var key : keys)
		{
			nameList.add(key);
			keyList.add(this.buildKey(key));
		}
		CompletionStage<Map<String, LocalValueWrapper>> future = redisson.getBuckets().getAsync(keyList.toArray(new String[0]))
				.thenApply(map ->
				{
					Map<String, LocalValueWrapper> wrapperMap = new HashMap<>();
					for (var i = 0; i < keyList.size(); i++)
					{
						var key = keyList.get(i);
						var name = nameList.get(i);
						var value = map.get(key);
						var localTtl = this.getLocalTtl(name);
						wrapperMap.put(name, new LocalValueWrapper(value, localTtl));
					}
					this.logOperation("get", nameList);
					return wrapperMap;
				});
		return (CompletableFuture<Map<String, LocalValueWrapper>>) future;
	}

	/**
	 * Caffeine::Expiry interface
	 *
	 * @param key
	 * @param value
	 * @param currentTime
	 * @return
	 */
	@Override
	public long expireAfterCreate(String key, LocalValueWrapper value, long currentTime)
	{
		return this.getLocalTtl(key) * 1_000_000;
	}
	@Override
	public long expireAfterUpdate(String key, LocalValueWrapper value, long currentTime, long currentDuration)
	{
		return this.getLocalTtl(key) * 1_000_000;
	}
	@Override
	public long expireAfterRead(String key, LocalValueWrapper value, long currentTime, long currentDuration)
	{
		return currentDuration;
	}

	/**
	 * Redisson::MessageListener interface
	 *
	 * @param channel
	 * @param names
	 */
	@Override
	public void onMessage(CharSequence channel, String names)
	{
		logger.debug("onMessage, channel: {}, names: {}", channel, names);
		if (names == null || names.isEmpty())
			return;

		cache.synchronous().invalidateAll(Arrays.asList(names.split(KEY_SPLIT)));
	}

	/**
	 * Redisson::StatusListener interface
	 *
	 * @param channel
	 */
	@Override
	public void onSubscribe(String channel)
	{
		logger.info("onSubscribe, channel: {}", channel);
		cache.synchronous().invalidateAll();
	}
	@Override
	public void onUnsubscribe(String channel)
	{
		logger.info("onUnsubscribe, channel: {}", channel);
	}

	/**
	 * Spring::init
	 */
	@Override
	public void afterPropertiesSet()
	{
		AssertUtil.notNull(redisson, "Redisson");
		AssertUtil.notEmpty(keyPrefix, "KeyPrefix");
		logger.info("config, keyPrefix: {}, ttl: {}ms, localTtl: {}ms, localMaxSize: {}ms, timeout: {}ms",
				keyPrefix, ttl, localTtl, localMaxSize, timeout);
		this.cache = Caffeine.newBuilder()
				.maximumSize(localMaxSize)
				.expireAfter(this)
				.buildAsync(this);
		var topic = this.buildTopic();
		var rtopic = redisson.getTopic(topic);
		rtopic.addListenerAsync(this).thenAccept(r -> logger.info("Subscribe status, channel: {}", topic));
		rtopic.addListenerAsync(String.class, this)
				.thenAccept(r -> logger.info("Subscribe message, channel: {}", topic));
	}

	/**
	 * Spring::destroy
	 */
	@Override
	public void destroy()
	{
		redisson.getTopic(this.buildTopic()).removeAllListeners();
	}
}
