package blue.redis.internal.core.cache;

import blue.base.core.cache.Cache;
import blue.base.core.cache.CacheBuilder;
import blue.base.core.cache.CacheLoader;
import blue.base.core.util.AssertUtil;
import blue.redis.core.options.RedisCacheOptions;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.api.listener.StatusListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-17
 */
public class DefaultLocalRedisCache<T> extends AbstractRedisCache<T> implements CacheLoader<String, T>,
		MessageListener<String>, StatusListener {
	private static Logger logger = LoggerFactory.getLogger(DefaultLocalRedisCache.class);

	private final Cache<String, T> cache;

	public DefaultLocalRedisCache(RedisCacheOptions options, RedissonClient client) {
		super(options, client);
		this.cache = CacheBuilder.create()
				.expireAfterAccess(options.getLocalTtl(), TimeUnit.MILLISECONDS)
				.maximumSize(options.getLocalMaxSize())
				.build(this);
		var topic = this.buildTopic();
		var rtopic = client.getTopic(topic);
		rtopic.addListenerAsync(this)
				.thenAccept(r -> logger.info("Subscribe status, channel: {}", topic));
		rtopic.addListenerAsync(String.class, this)
				.thenAccept(r -> logger.info("Subscribe message, channel: {}", topic));
	}

	@Override
	public T get(String key) {
		AssertUtil.notEmpty(key, "Key");
		return cache.get(key);
	}

	@Override
	public Map<String, T> getMap(String... keys) {
		AssertUtil.notEmpty(keys, "Keys");
		return cache.getAll(Arrays.asList(keys));
	}

	@Override
	public void set(String key, T value) {
		AssertUtil.notEmpty(key, "Key");
		AssertUtil.notNull(value, "Value");
		this.getMapCache().fastPut(key, value, options.getTtl(), TimeUnit.MILLISECONDS);
		this.publishOperation(List.of(key));
		this.logOperation(RedisOperation.SET, key);
		cache.remove(key);
	}

	@Override
	public void setMap(Map<String, T> map) {
		AssertUtil.notEmpty(map, "Map");
		Set<String> keySet = map.keySet();
		this.getMapCache().putAll(map, options.getTtl(), TimeUnit.MILLISECONDS);
		this.publishOperation(keySet);
		this.logOperation(RedisOperation.SET, keySet);
		cache.removeAll(keySet);
	}

	@Override
	public void remove(String... keys) {
		AssertUtil.notEmpty(keys, "Keys");
		List<String> keyList = List.of(keys);
		this.getMapCache().fastRemove(keys);
		this.publishOperation(keyList);
		this.logOperation(RedisOperation.DELETE, keyList);
		cache.removeAll(keyList);
	}

	@Override
	public void clear() {
		this.getMapCache().clear();
		this.publishOperation(List.of(KEY_CLEAR));
		this.logOperation(RedisOperation.CLEAR);
		cache.clear();
	}

	@Override
	public T load(String key) {
		T value = this.getMapCache().get(key);
		this.logOperation(RedisOperation.GET, key);
		return value;
	}

	@Override
	public Map<? extends String, ? extends T> loadAll(Set<? extends String> keys) {
		Set<String> keySet = Set.copyOf(keys);
		Map<String, T> map = this.getMapCache().getAll(keySet);
		this.logOperation(RedisOperation.GET, keySet);
		return map;
	}

	@Override
	public void onMessage(CharSequence channel, String keys) {
		logger.debug("onMessage, channel: {}, keys: {}", channel, keys);
		if (keys == null || keys.isEmpty()) {
			return;
		}

		if (KEY_CLEAR.equals(keys)) {
			cache.clear();
		}
		else {
			cache.removeAll(Arrays.asList(keys.split(KEY_SPLIT)));
		}
	}

	@Override
	public void onSubscribe(String channel) {
		logger.info("onSubscribe, channel: {}", channel);
		cache.clear();
	}

	@Override
	public void onUnsubscribe(String channel) {
		logger.info("onUnsubscribe, channel: {}", channel);
	}

	@Override
	public void disconnect() {
		client.getTopic(this.buildTopic()).removeAllListeners();
		this.logOperation(RedisOperation.DISCONNECT);
	}
}
