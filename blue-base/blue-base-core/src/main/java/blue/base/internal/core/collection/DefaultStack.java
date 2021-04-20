package blue.base.internal.core.collection;

import blue.base.core.collection.Stack;

import java.util.ArrayList;
import java.util.List;

/**
 * 栈
 *
 * @author Jin Zheng
 * @since 1.0 2021-01-22
 */
public class DefaultStack<T> implements Stack<T> {
	private final List<T> array;

	public DefaultStack() {
		array = new ArrayList<>();
	}

	@Override
	public T pop() {
		if (array.isEmpty()) {
			return null;
		}
		return array.remove(array.size() - 1);
	}

	@Override
	public void push(T o) {
		array.add(o);
	}

	@Override
	public T peek() {
		if (array.isEmpty()) {
			return null;
		}
		return array.get(array.size() - 1);
	}

	@Override
	public int size() {
		return array.size();
	}

	@Override
	public boolean isEmpty() {
		return array.isEmpty();
	}
}
