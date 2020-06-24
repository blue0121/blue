package blue.internal.jdbc.sql;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2019-11-24
 */
public class SqlParam
{
	private Class<?> clazz;
	private Object target;
	private Map<String, Object> map;
	private String field;
	private Object[] args;

	public SqlParam(Class<?> clazz)
	{
		this.clazz = clazz;
	}

	public SqlParam(Object target, Object...args)
	{
		if (target instanceof Class)
		{
			this.clazz = (Class<?>)target;
		}
		else
		{
			this.target = target;
			this.clazz = target.getClass();
		}
		this.args = args;
	}

	public SqlParam(Class<?> clazz, Map<String, Object> map, Object[] args)
	{
		this.clazz = clazz;
		this.map = map;
		this.args = args;
	}

	public SqlParam(Class<?> clazz, String field, Map<String, Object> map)
	{
		this.clazz = clazz;
		this.field = field;
		this.map = map;
	}

	public Class<?> getClazz()
	{
		return clazz;
	}

	public Object getTarget()
	{
		return target;
	}

	public Map<String, Object> getMap()
	{
		return map;
	}

	public String getField()
	{
		return field;
	}

	public Object[] getArgs()
	{
		return args;
	}
}
