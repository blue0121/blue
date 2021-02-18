package blue.core.common;

import blue.internal.core.common.ConcurrentHashSet;

import java.util.Collection;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2021-02-18
 */
public interface ConcurrentSet<E> extends Set<E>
{
	/**
	 * create ConcurrentSet
	 * @param c
	 * @param <E>
	 * @return
	 */
	static <E> ConcurrentSet<E> create(Collection<E> c)
	{
		if (c == null || c.isEmpty())
			return new ConcurrentHashSet<>();

		return new ConcurrentHashSet<>(c);
	}

}
