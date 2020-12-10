package blue.internal.redis.cache;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2020-12-09
 */
public class DefaultMapEntry<K, V> implements Map.Entry<K, V>
{
	private K key;
	private V value;

	public DefaultMapEntry(K key)
	{
		this.key = key;
	}

	public DefaultMapEntry(K key, V value)
	{
		this.key = key;
		this.value = value;
	}

	@Override
	public K getKey()
	{
		return key;
	}

	@Override
	public V getValue()
	{
		return value;
	}

	@Override
	public V setValue(V value)
	{
		this.value = value;
		return value;
	}
}
