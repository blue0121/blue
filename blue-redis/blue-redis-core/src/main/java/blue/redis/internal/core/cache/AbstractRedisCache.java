package blue.redis.internal.core.cache;

import blue.base.core.util.StringUtil;
import blue.redis.core.RedisCache;
import blue.redis.core.options.RedisCacheOptions;
import org.redisson.api.MapOptions;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-17
 */
public abstract class AbstractRedisCache<T> implements RedisCache<T> {
	private static Logger logger = LoggerFactory.getLogger(AbstractRedisCache.class);

	public static final String KEY_SPLIT = ";:;";
	public static final String KEY_CLEAR = ";!:!;~clear~;!:!;";
	public static final String TOPIC_PREFIX = "local_cache_remove:";

	protected final RedisCacheOptions options;
	protected final RedissonClient client;
	protected final RMapCache<String, T> mapCache;

	public AbstractRedisCache(RedisCacheOptions options, RedissonClient client) {
		this.options = options;
		this.options.check();
		this.client = client;
		this.mapCache = client.getMapCache(options.getPrefix(), this.getMapOption());
	}

	protected RMapCache<String, T> getMapCache() {
		return mapCache;
	}

	protected void publishOperation(Collection<String> keyList) {
		var messages = StringUtil.join(keyList, KEY_SPLIT);
		var topic = this.buildTopic();
		client.getTopic(topic).publishAsync(messages)
				.thenAccept(r -> logger.info("Publish, channel: {}, message: {}", topic, messages));
	}

	protected void logOperation(RedisOperation operation, Collection<String> keyList) {
		if (logger.isDebugEnabled()) {
			logger.debug("RedisCache '{}', operation: {}, prefix: {}, keys: {}", options.getId(),
					operation, options.getPrefix(), keyList);
		}
	}

	protected void logOperation(RedisOperation operation, String... keys) {
		if (logger.isDebugEnabled()) {
			logger.debug("RedisCache '{}', operation: {}, prefix: {}, keys: {}", options.getId(),
					operation, options.getPrefix(), Arrays.toString(keys));
		}
	}

	protected String buildTopic() {
		return TOPIC_PREFIX + options.getPrefix();
	}

	private MapOptions<String, T> getMapOption() {
		MapOptions<String, T> ops = MapOptions.defaults();
		ops.writeMode(MapOptions.WriteMode.WRITE_THROUGH);
		if (options.getLoader() != null) {
			ops.loader(new DefaultMapLoader<>(options.getLoader()));
		}
		if (options.getWriter() != null) {
			ops.writer(new DefaultMapWriter<>(options.getWriter()));
		}
		return ops;
	}

}
