package blue.internal.jdbc.sql;

import blue.core.util.StringUtil;
import blue.internal.jdbc.parser.CacheColumn;
import blue.internal.jdbc.parser.CacheEntity;
import blue.internal.jdbc.parser.CacheId;
import blue.internal.jdbc.parser.CacheVersion;
import blue.internal.jdbc.parser.ParserCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-26
 */
public class PartialInsertSqlHandler implements SqlHandler
{
	public PartialInsertSqlHandler()
	{
	}

	@Override
	public String sql(SqlParam param)
	{
		Map<String, Object> map = param.getMap();
		this.checkMap(map);

		CacheEntity cacheEntity = ParserCache.getInstance().get(param.getClazz());
		List<String> columnList = new ArrayList<>();
		List<String> nameList = new ArrayList<>();

		Map<String, CacheColumn> columnMap = cacheEntity.getColumnMap();
		Map<String, CacheId> idMap = cacheEntity.getIdMap();
		CacheVersion cacheVersion = cacheEntity.getVersion();
		for (Map.Entry<String, Object> entry : map.entrySet())
		{
			String column = this.getColumnString(entry.getKey(), idMap, columnMap, cacheVersion);
			columnList.add(column);
			nameList.add(":" + entry.getKey());
		}

		String sql = String.format(INSERT_TPL, cacheEntity.getEscapeTable(),
				StringUtil.join(columnList, ","), StringUtil.join(nameList, ","));
		return sql;
	}
}
