package blue.redis.internal.spring.bean;

import blue.redis.core.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.cache.support.NullValue;

import java.util.concurrent.Callable;

/**
 * @author Jin Zheng
 * @since 2020-12-06
 */
public class SpringCache extends AbstractValueAdaptingCache {
	private static Logger logger = LoggerFactory.getLogger(SpringCache.class);

	private final String name;
	private final RedisCache<Object> cache;

	public SpringCache(String name, RedisCache<Object> cache, boolean allowNullValues) {
		super(allowNullValues);
		this.name = name;
		this.cache = cache;
	}

	@Override
	protected Object lookup(Object key) {
		return cache.get(key.toString());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getNativeCache() {
		return cache;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		Object value = cache.get(key.toString());
		if (value == null) {
			value = putValue(key, valueLoader);
		}
		return (T) fromStoreValue(value);
	}

	@Override
	public void put(Object key, Object value) {
		if (!this.isAllowNullValues() && value == null) {
			cache.remove(key.toString());
			return;
		}

		Object newValue = toStoreValue(value);
		cache.set(key.toString(), newValue);
	}

	private <T> Object putValue(Object key, Callable<T> valueLoader) {
		Object value = null;
		try {
			value = valueLoader.call();
		}
		catch (Exception ex) {
			return new Cache.ValueRetrievalException(key, valueLoader, ex);
		}
		this.put(key, value);
		return value;
	}

	@Override
	public void evict(Object key) {
		cache.remove(key.toString());
	}

	@Override
	public void clear() {
		cache.clear();
	}

	@Override
	protected Object fromStoreValue(Object storeValue) {
		if (storeValue == null) {
			return null;
		}

		if (this.isAllowNullValues() && storeValue instanceof NullValue) {
			return null;
		}
		return storeValue;
	}
}
