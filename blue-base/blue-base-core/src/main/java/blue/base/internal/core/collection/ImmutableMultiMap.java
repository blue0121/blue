package blue.base.internal.core.collection;

import blue.base.core.collection.MultiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * @author Jin Zheng
 * @since 1.0 2021-02-08
 */
public class ImmutableMultiMap<K, V> extends AbstractMultiMap<K, V> {
	private static Logger logger = LoggerFactory.getLogger(ImmutableMultiMap.class);

	public ImmutableMultiMap(MultiMap<K, V> map) {
		this.map = new HashMap<>();
		if (map == null || map.isEmpty()) {
			return;
		}

		for (var entry : map.entrySet()) {
			this.map.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("clear");
	}

	@Override
	public V put(K key, V value) {
		throw new UnsupportedOperationException("put");
	}

	@Override
	public boolean remove(K key) {
		throw new UnsupportedOperationException("remove");
	}

}
