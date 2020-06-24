package blue.jdbc.core;

import blue.internal.jdbc.expression.DefaultExpression;
import blue.internal.jdbc.expression.DefaultOrderBy;
import blue.internal.jdbc.expression.ExpressionOperator;

/**
 * @author Jin Zheng
 * @since 2019-12-20
 */
public interface Expression
{

	Expression add(String sql);

	Expression add(Expression expression);

	static Expression and()
	{
		return new DefaultExpression(ExpressionOperator.AND);
	}

	static Expression or()
	{
		return new DefaultExpression(ExpressionOperator.OR);
	}

	static OrderBy orderBy()
	{
		return new DefaultOrderBy();
	}

}
