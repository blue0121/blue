package blue.internal.jdbc.expression;

/**
 * @author Jin Zheng
 * @since 2019-12-23
 */
public enum  ExpressionOperator
{

	AND(" and "),

	OR(" or ");


	private String name;

	ExpressionOperator(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
