package blue.internal.jdbc.dialect;

import blue.jdbc.annotation.LockModeType;
import blue.jdbc.core.Dialect;

/**
 * HyperSQL/HsqlDB数据库方言
 *
 * @author zhengj
 * @since 1.0 2018-10-31
 */
public class HyperSQLDialect implements Dialect
{
	private static final String QUOT = "\"";

	public HyperSQLDialect()
	{
	}

	@Override
	public String escape()
	{
		return QUOT;
	}

	@Override
	public String escape(String key)
	{
		StringBuilder t = new StringBuilder(key.length() + 4);
		t.append(QUOT).append(key).append(QUOT);
		return t.toString();
	}

	@Override
	public String page(String sql, int start, int size)
	{
		StringBuilder t = new StringBuilder(sql.length() + 32);
		t.append(sql);
		t.append(" offset ");
		t.append(start);
		t.append(" limit ");
		t.append(size);
		return t.toString();
	}

	@Override
	public String lock(String sql, LockModeType type)
	{
		if (type == LockModeType.NONE)
			return sql;
		
		StringBuilder t = new StringBuilder(sql);
		if (type == LockModeType.WRITE)
			t.append(" for update");
		
		return t.toString();
	}
}
