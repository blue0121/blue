package blue.core.common;

import java.util.Map;
import java.util.Objects;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-14
 */
public class SimpleEntry<K, V> implements Map.Entry<K, V>
{
	private final K key;
	private V value;

	public SimpleEntry(K key)
	{
		this(key, null);
	}

	public SimpleEntry(K key, V value)
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

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		SimpleEntry<?, ?> that = (SimpleEntry<?, ?>) o;
		return key.equals(that.key);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(key);
	}
}
