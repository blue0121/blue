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
public class PartialUpdateSqlHandler implements SqlHandler
{
	public PartialUpdateSqlHandler()
	{
	}

	@Override
	public String sql(SqlParam param)
	{
		Map<String, Object> map = param.getMap();
		this.checkMap(map);

		CacheEntity cacheEntity = ParserCache.getInstance().get(param.getClazz());
		List<String> columnList = new ArrayList<>();
		List<String> idList = new ArrayList<>();

		Map<String, CacheColumn> columnMap = cacheEntity.getColumnMap();
		for (Map.Entry<String, Object> entry : map.entrySet())
		{
			String column = this.getColumnString(entry.getKey(), null, columnMap, null);
			columnList.add(column + "=:" + entry.getKey());
		}

		for (Map.Entry<String, CacheId> entry : cacheEntity.getIdMap().entrySet())
		{
			CacheId id = entry.getValue();
			idList.add(id.getEscapeColumn() + "=:" + id.getName());
		}

		String sql = String.format(UPDATE_TPL, cacheEntity.getEscapeTable(),
				StringUtil.join(columnList, ","), StringUtil.join(idList, " and "));
		return sql;
	}
}
