package blue.base.internal.core.collection;

import blue.base.core.collection.Stack;

import java.util.LinkedList;

/**
 * 栈
 *
 * @author Jin Zheng
 * @since 1.0 2021-01-22
 */
public class DefaultStack<T> implements Stack<T> {
	private final LinkedList<T> list;

	public DefaultStack() {
		list = new LinkedList<T>();
	}

	@Override
	public T pop() {
		if (list.isEmpty()) {
			return null;
		}
		else {
			return list.pop();
		}
	}

	@Override
	public void push(T o) {
		list.push(o);
	}

	@Override
	public T peek() {
		return list.peek();
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}
}
