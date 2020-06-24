package blue.internal.jdbc.sql;

import blue.core.util.StringUtil;
import blue.internal.jdbc.parser.CacheColumn;
import blue.internal.jdbc.parser.CacheEntity;
import blue.internal.jdbc.parser.CacheId;
import blue.internal.jdbc.parser.CacheVersion;
import blue.internal.jdbc.parser.ParserCache;
import blue.internal.jdbc.util.ObjectUtil;
import blue.jdbc.annotation.GeneratorType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-26
 */
public class InsertSqlHandler implements SqlHandler
{
	public InsertSqlHandler()
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
		List<String> fieldList = new ArrayList<>();
		List<String> columnList = new ArrayList<>();

		for (Map.Entry<String, CacheId> entry : cacheEntity.getIdMap().entrySet())
		{
			CacheId id = entry.getValue();
			if (id.getGeneratorType() == GeneratorType.UUID || id.getGeneratorType() == GeneratorType.ASSIGNED)
			{
				columnList.add(id.getEscapeColumn());
				fieldList.add(":" + id.getName());
			}
		}
		for (Map.Entry<String, CacheColumn> entry : cacheEntity.getColumnMap().entrySet())
		{
			CacheColumn column = entry.getValue();
			Object val = map.get(column.getName());
			if (column.isMustInsert() || val != null)
			{
				columnList.add(column.getEscapeColumn());
				fieldList.add(":" + column.getName());
			}
		}
		CacheVersion version = cacheEntity.getVersion();
		if (version != null)
		{
			columnList.add(version.getEscapeColumn());
			fieldList.add(":" + version.getName());
		}

		String sql = String.format(INSERT_TPL, cacheEntity.getEscapeTable(),
				StringUtil.join(columnList, ","), StringUtil.join(fieldList, ","));

		return sql;
	}
}
