package blue.internal.jdbc.sql;

import blue.core.util.StringUtil;
import blue.internal.jdbc.parser.CacheColumn;
import blue.internal.jdbc.parser.CacheEntity;
import blue.internal.jdbc.parser.CacheId;
import blue.internal.jdbc.parser.ParserCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-26
 */
public class ExistSqlHandler implements SqlHandler
{
	public ExistSqlHandler()
	{
	}

	@Override
	public String sql(SqlParam param)
	{
		Object[] args = param.getArgs();
		Map<String, Object> map = param.getMap();
		CacheEntity cacheEntity = ParserCache.getInstance().get(param.getClazz());
		List<String> columnList = new ArrayList<>();

		if (args.length > 0)
		{
			Map<String, CacheColumn> columnMap = cacheEntity.getColumnMap();
			for (Object arg : args)
			{
				CacheColumn column = this.getColumn(arg, columnMap);
				columnList.add(column.getEscapeColumn() + "=:" + column.getName());
			}
		}
		String op = args.length > 0 ? "!=:" : "=:";
		for (Map.Entry<String, CacheId> entry : cacheEntity.getIdMap().entrySet())
		{
			CacheId id = entry.getValue();
			Object val = map.get(id.getName());
			if (val != null)
			{
				columnList.add(id.getEscapeColumn() + op + id.getName());
			}
		}

		String sql = String.format(COUNT_TPL, cacheEntity.getEscapeTable(),
				StringUtil.join(columnList, " and "));
		return sql;
	}
}
