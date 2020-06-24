package blue.internal.jdbc.expression;

import blue.jdbc.core.OrderBy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2019-12-28
 */
public class DefaultOrderBy implements OrderBy
{
	private List<String> sqlList = new ArrayList<>();

	public DefaultOrderBy()
	{
	}

	@Override
	public void add(String sql)
	{
		sqlList.add(sql);
	}

	@Override
	public String toString()
	{
		if (sqlList.isEmpty())
			return "";

		StringBuilder sql = new StringBuilder();
		for (String obj : sqlList)
		{
			if (sql.length() > 0)
			{
				sql.append(",");
			}
			sql.append(obj);
		}
		return sql.toString();
	}
}
