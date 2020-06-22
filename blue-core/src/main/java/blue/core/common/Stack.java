package blue.core.common;

import java.util.LinkedList;

/**
 * 栈
 * 
 * @author zhengj
 * @since 2013-6-18 1.0
 */
public class Stack<T>
{
	private final LinkedList<T> list;
	
	public Stack()
	{
		list = new LinkedList<T>();
	}
	
	/**
	 * 出栈，空栈则返回 null
	 * 
	 * @return 出栈，空栈则返回 null
	 */
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
	public void push(T o)
	{
		list.push(o);
	}
	
	/**
	 * 返回栈顶元素，空栈则返回 null
	 * 
	 * @return 栈顶元素，空栈则返回 null
	 */
	public T peek()
	{
		return list.peek();
	}
	
	/**
	 * 返回栈元素的个数
	 * 
	 * @return 栈元素的个数
	 */
	public int size()
	{
		return list.size();
	}
	
}
