package blue.internal.jdbc.sql;

import blue.internal.jdbc.parser.CacheColumn;
import blue.internal.jdbc.parser.CacheId;
import blue.internal.jdbc.parser.CacheVersion;
import blue.jdbc.exception.JdbcException;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2019-11-24
 */
public interface SqlHandler
{
	String SUCCESS = "success";

	String INSERT_TPL = "insert into %s (%s) values (%s)";
	String UPDATE_TPL = "update %s set %s where %s";
	String DELETE_TPL = "delete from %s where %s";
	String SELECT_TPL = "select * from %s where %s";
	String GET_TPL = "select %s from %s where %s";
	String COUNT_TPL = "select count(*) from %s where %s";

	String sql(SqlParam param);

	default void checkMap(Map<String, Object> map)
	{
		if (map == null || map.isEmpty())
			throw new JdbcException("参数个数错误");
	}

	default void checkEvenParam(Object[] args)
	{
		if (args.length == 0 || args.length % 2 != 0)
			throw new JdbcException("参数个数错误");
	}

	default void checkStringParam(Object[] args, int i)
	{
		if (!(args[i] instanceof String))
			throw new JdbcException("参数第 " + i + " 个不是字符串的字段名称");
	}

	default CacheColumn getColumn(Object name, Map<String, CacheColumn> columnMap)
	{
		CacheColumn column = columnMap.get(name);
		if (column == null)
			throw new JdbcException(name + " 不存在列");

		return column;
	}

	default String getColumnString(Object name, Map<String, CacheId> idMap, Map<String,
			CacheColumn> columnMap, CacheVersion version)
	{
		String whereColumn = null;
		if (columnMap != null && columnMap.get(name) != null)
		{
			whereColumn = columnMap.get(name).getEscapeColumn();
		}
		if (idMap != null && idMap.get(name) != null)
		{
			whereColumn = idMap.get(name).getEscapeColumn();
		}
		if (version != null && version.getName().equals(name))
		{
			whereColumn = version.getEscapeColumn();
		}
		if (whereColumn == null)
			throw new JdbcException(name + " 不存在列");

		return whereColumn;
	}

}
