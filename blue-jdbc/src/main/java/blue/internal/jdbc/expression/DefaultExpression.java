package blue.internal.jdbc.expression;

import blue.jdbc.core.Expression;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2019-12-23
 */
public class DefaultExpression implements Expression
{
	private ExpressionOperator operator;
	private List<Object> sqlList = new ArrayList<>();

	public DefaultExpression(ExpressionOperator operator)
	{
		this.operator = operator;
	}

	@Override
	public Expression add(String sql)
	{
		sqlList.add(sql);
		return this;
	}

	@Override
	public Expression add(Expression expression)
	{
		sqlList.add(expression);
		return this;
	}

	@Override
	public String toString()
	{
		if (sqlList.isEmpty())
			return "";

		StringBuilder sql = new StringBuilder();
		for (Object obj : sqlList)
		{
			if (obj instanceof String)
			{
				this.appendString(sql, (String)obj);
			}
			else if (obj instanceof Expression)
			{
				this.appendExpression(sql, (Expression)obj);
			}
		}
		return sql.toString();
	}

	private void appendString(StringBuilder sql, String obj)
	{
		if (sql.length() > 0)
		{
			sql.append(operator);
		}
		sql.append(obj);
	}

	private void appendExpression(StringBuilder sql, Expression obj)
	{
		String str = obj.toString();
		if (str.isEmpty())
			return;

		if (sql.length() > 0)
		{
			sql.append(operator);
		}
		sql.append("(").append(obj).append(")");
	}

}
