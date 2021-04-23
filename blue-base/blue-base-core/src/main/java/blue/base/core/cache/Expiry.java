package blue.base.core.cache;


/**
 * @author Jin Zheng
 * @since 1.0 2021-04-23
 */
public interface Expiry<K, V> {


	long expireAfterCreate(K key, V value, long currentTimeMillis);

	long expireAfterUpdate(K key, V value, long currentTimeMillis, long currentDurationMillis);

	default long expireAfterRead(K key, V value, long currentTimeMillis, long currentDurationMillis) {
		return currentDurationMillis;
	}

}
