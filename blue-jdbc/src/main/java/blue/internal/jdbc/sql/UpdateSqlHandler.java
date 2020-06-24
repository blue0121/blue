package blue.internal.jdbc.sql;

import blue.core.util.StringUtil;
import blue.internal.jdbc.parser.CacheColumn;
import blue.internal.jdbc.parser.CacheEntity;
import blue.internal.jdbc.parser.CacheId;
import blue.internal.jdbc.parser.CacheVersion;
import blue.internal.jdbc.parser.ParserCache;
import blue.internal.jdbc.util.ObjectUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-26
 */
public class UpdateSqlHandler implements SqlHandler
{
	public UpdateSqlHandler()
	{
	}

	@Override
	public String sql(SqlParam param)
	{
		Map<String, Object> map = param.getMap();
		if (map == null || map.isEmpty())
		{
			map = ObjectUtil.toMap(param.getTarget());
		}
		CacheEntity cacheEntity = ParserCache.getInstance().get(param.getClazz());
		List<String> idList = new ArrayList<>();
		List<String> columnList = new ArrayList<>();

		for (Map.Entry<String, CacheId> entry : cacheEntity.getIdMap().entrySet())
		{
			CacheId id = entry.getValue();
			idList.add(id.getEscapeColumn() + "=:" + id.getName());
		}
		for (Map.Entry<String, CacheColumn> entry : cacheEntity.getColumnMap().entrySet())
		{
			CacheColumn column = entry.getValue();
			Object val = map.get(column.getName());
			if (column.isMustInsert() || val != null)
			{
				columnList.add(column.getEscapeColumn() + "=:" + column.getName());
			}
		}
		CacheVersion version = cacheEntity.getVersion();
		if (version != null)
		{
			columnList.add(version.getEscapeColumn() + "=" + version.getEscapeColumn() + "+1");
			Object val = map.get(version.getName());
			if (version.isForce() || val != null)
			{
				idList.add(version.getEscapeColumn() + "=:" + version.getName());
			}
		}

		String sql = String.format(UPDATE_TPL, cacheEntity.getEscapeTable(),
				StringUtil.join(columnList, ","), StringUtil.join(idList, " and "));

		return sql;
	}
}
