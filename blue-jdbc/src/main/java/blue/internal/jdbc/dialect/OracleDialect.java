package blue.internal.jdbc.dialect;

import blue.jdbc.annotation.LockModeType;
import blue.jdbc.core.Dialect;

/**
 * Oracle 数据库方言
 * 
 * @author zhengj
 * @since 1.0 2018-10-31
 */
public class OracleDialect implements Dialect
{
	private static final String QUOT = "\"";

	public OracleDialect()
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
		StringBuilder t = new StringBuilder(sql.length() + 100);
		t.append("SELECT * FROM (SELECT TMP.*, ROWNUM RN FROM (");
		t.append(sql);
		t.append(") TMP WHERE ROWNUM <= ");
		t.append(start + size);
		t.append(") WHERE RN >= ");
		t.append(start + 1);
		return t.toString();
	}

	@Override
	public String lock(String sql, LockModeType type)
	{
		if (type == LockModeType.NONE)
			return sql;
		
		StringBuilder t = new StringBuilder(sql);
		if (type == LockModeType.WRITE)
			t.append(" FOR UPDATE");
		
		return t.toString();
	}
}
