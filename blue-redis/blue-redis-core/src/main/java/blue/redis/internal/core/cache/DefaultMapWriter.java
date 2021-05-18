package blue.redis.internal.core.cache;

import blue.redis.core.RedisCacheWriter;
import org.redisson.api.map.MapWriter;

import java.util.Collection;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-18
 */
public class DefaultMapWriter<T> implements MapWriter<String, T> {
	private final RedisCacheWriter writer;

	public DefaultMapWriter(RedisCacheWriter writer) {
		this.writer = writer;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void write(Map<String, T> map) {
		writer.write(map);
	}

	@Override
	public void delete(Collection<String> keys) {
		writer.delete(keys);
	}
}
