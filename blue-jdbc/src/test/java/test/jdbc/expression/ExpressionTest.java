package test.jdbc.expression;

import blue.jdbc.core.Expression;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 2019-12-28
 */
public class ExpressionTest
{
	public ExpressionTest()
	{
	}

	@Test
	public void testAnd1()
	{
		Expression expression = Expression.and();
		expression.add("group_id=:groupId");
		System.out.println(expression);
		Assertions.assertEquals("group_id=:groupId", expression.toString());

		expression.add("name=:name");
		System.out.println(expression);
		Assertions.assertEquals("group_id=:groupId and name=:name", expression.toString());

		expression.add("state=:state");
		System.out.println(expression);
		Assertions.assertEquals("group_id=:groupId and name=:name and state=:state", expression.toString());
	}

	@Test
	public void testAnd2()
	{
		Expression expression = Expression.and();
		expression.add("group_id=:groupId");

		Expression exp = Expression.or();
		exp.add("name=:name");
		exp.add("state=:state");

		expression.add(exp);
		System.out.println(expression);
		Assertions.assertEquals("group_id=:groupId and (name=:name or state=:state)", expression.toString());
	}

}
