package test.jdbc.expression;

import blue.jdbc.core.Expression;
import blue.jdbc.core.OrderBy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 2019-12-28
 */
public class OrderByTest
{
	public OrderByTest()
	{
	}

	@Test
	public void test1()
	{
		OrderBy order = Expression.orderBy();
		order.add("a.id desc");
		System.out.println(order);
		Assertions.assertEquals("a.id desc", order.toString());

		order.add("a.name asc");
		System.out.println(order);
		Assertions.assertEquals("a.id desc,a.name asc", order.toString());

		order.add("a.state asc");
		System.out.println(order);
		Assertions.assertEquals("a.id desc,a.name asc,a.state asc", order.toString());
	}

}
