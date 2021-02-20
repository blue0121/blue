package blue.internal.core.common;

import blue.core.common.Stack;

import java.util.LinkedList;

/**
 * 栈
 *
 * @author Jin Zheng
 * @since 1.0 2021-01-22
 */
public class DefaultStack<T> implements Stack<T>
{
	private final LinkedList<T> list;
	
	public DefaultStack()
	{
		list = new LinkedList<T>();
	}
	
	/**
	 * 出栈，空栈则返回 null
	 * 
	 * @return 出栈，空栈则返回 null
	 */
	@Override
	public T pop()
	{
		if (list.isEmpty())
			return null;
		else
			return list.pop();
	}
	
	/**
	 * 入栈
	 * 
	 * @param o 入栈元素
	 */
	@Override
	public void push(T o)
	{
		list.push(o);
	}
	
	/**
	 * 返回栈顶元素，空栈则返回 null
	 * 
	 * @return 栈顶元素，空栈则返回 null
	 */
	@Override
	public T peek()
	{
		return list.peek();
	}
	
	/**
	 * 返回栈元素的个数
	 * 
	 * @return 栈元素的个数
	 */
	@Override
	public int size()
	{
		return list.size();
	}
	
}
