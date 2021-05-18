package blue.redis.internal.core.cache;

import blue.redis.core.RedisCacheLoader;
import org.redisson.api.map.MapLoader;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-18
 */
public class DefaultMapLoader<T> implements MapLoader<String, T> {
	private final RedisCacheLoader loader;

	public DefaultMapLoader(RedisCacheLoader loader) {
		this.loader = loader;
	}

	@SuppressWarnings("rawtypes")
	@Override
    public T load(String key) {
        return (T) loader.load(key);
    }

    @Override
    public Iterable<String> loadAllKeys() {
        return loader.loadAllKeys();
    }
}
