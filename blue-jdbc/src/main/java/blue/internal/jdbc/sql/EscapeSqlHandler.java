package blue.internal.jdbc.sql;

import blue.internal.jdbc.parser.CacheColumn;
import blue.internal.jdbc.parser.CacheEntity;
import blue.internal.jdbc.parser.CacheId;
import blue.internal.jdbc.parser.CacheVersion;
import blue.internal.jdbc.parser.ParserCache;
import blue.jdbc.core.Dialect;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2019-11-24
 */
public class EscapeSqlHandler implements SqlHandler
{
	private Dialect dialect;

	public EscapeSqlHandler(Dialect dialect)
	{
		this.dialect = dialect;
	}

	@Override
	public String sql(SqlParam param)
	{
		CacheEntity entity = ParserCache.getInstance().get(param.getClazz());
		entity.setEscapeTable(dialect.escape(entity.getTable()));
		this.escapeId(entity);
		this.escapeVersion(entity);
		this.escapeColumn(entity);
		return SUCCESS;
	}

	private void escapeId(CacheEntity entity)
	{
		Map<String, CacheId> idMap = entity.getIdMap();
		for (Map.Entry<String, CacheId> entry : idMap.entrySet())
		{
			CacheId id = entry.getValue();
			id.setEscapeColumn(dialect.escape(id.getColumn()));
		}
	}

	private void escapeVersion(CacheEntity entity)
	{
		CacheVersion version = entity.getVersion();
		if (version == null)
			return;

		version.setEscapeColumn(dialect.escape(version.getColumn()));
	}

	private void escapeColumn(CacheEntity entity)
	{
		Map<String, CacheColumn> columnMap = entity.getColumnMap();
		for (Map.Entry<String, CacheColumn> entry : columnMap.entrySet())
		{
			CacheColumn column = entry.getValue();
			column.setEscapeColumn(dialect.escape(column.getColumn()));
		}
	}

}
