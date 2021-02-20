package blue.core.common;

import blue.internal.core.common.DefaultStack;

/**
 * 栈
 *
 * @author Jin Zheng
 * @since 1.0 2021-01-22
 */
public interface Stack<T>
{
	/**
	 * 创建栈
	 *
	 * @param <T>
	 * @return
	 */
	static <T> Stack<T> create()
	{
		return new DefaultStack<>();
	}

	/**
	 * 出栈，空栈则返回 null
	 * 
	 * @return 出栈，空栈则返回 null
	 */
	T pop();
	
	/**
	 * 入栈
	 * 
	 * @param o 入栈元素
	 */
	void push(T o);
	
	/**
	 * 返回栈顶元素，空栈则返回 null
	 * 
	 * @return 栈顶元素，空栈则返回 null
	 */
	T peek();
	
	/**
	 * 返回栈元素的个数
	 * 
	 * @return 栈元素的个数
	 */
	int size();
	
}
