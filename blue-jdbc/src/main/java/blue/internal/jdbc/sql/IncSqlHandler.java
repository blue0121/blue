package blue.internal.jdbc.sql;

import blue.core.util.NumberUtil;
import blue.core.util.StringUtil;
import blue.internal.jdbc.parser.CacheColumn;
import blue.internal.jdbc.parser.CacheEntity;
import blue.internal.jdbc.parser.CacheId;
import blue.internal.jdbc.parser.ParserCache;
import blue.jdbc.exception.JdbcException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-26
 */
public class IncSqlHandler implements SqlHandler
{
	public IncSqlHandler()
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
			CacheColumn column = this.getColumn(entry.getKey(), columnMap);
			if (!NumberUtil.isNumber(entry.getValue().getClass()))
				throw new JdbcException("参数 " + entry.getKey() + " 不是数字");

			if (!NumberUtil.isNumber(column.getField().getType()))
				throw new JdbcException(entry.getKey() + " 不是数字列");

			columnList.add(column.getEscapeColumn()+ "=" + column.getEscapeColumn() + "+:" + column.getName());
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
