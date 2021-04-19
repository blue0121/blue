package test.base.core.collection;

import blue.base.core.collection.Stack;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @date 2021-04-19
 */
public class StackTest {

	public StackTest() {
	}

	@Test
	public void testEmpty() {
		Stack<Integer> stack = Stack.create();
		Assertions.assertNull(stack.peek());
		Assertions.assertEquals(0, stack.size());
		Assertions.assertTrue(stack.isEmpty());
		Assertions.assertNull(stack.pop());
		Assertions.assertEquals(0, stack.size());
		Assertions.assertTrue(stack.isEmpty());
	}

	@Test
	public void testPush() {
		Stack<Integer> stack = Stack.create();
		stack.push(1);
		Assertions.assertFalse(stack.isEmpty());
		Assertions.assertEquals(1, stack.size());
		Assertions.assertEquals(1, stack.peek());
		Assertions.assertEquals(1, stack.pop());
		Assertions.assertNull(stack.peek());
		Assertions.assertNull(stack.pop());
		Assertions.assertTrue(stack.isEmpty());
		Assertions.assertEquals(0, stack.size());
	}

}
