package blue.internal.jdbc.metadata;

import blue.internal.jdbc.parser.CacheColumn;
import blue.internal.jdbc.parser.CacheEntity;
import blue.internal.jdbc.parser.CacheId;
import blue.internal.jdbc.parser.ParserCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2019-12-19
 */
public class CheckTable
{
	private static Logger logger = LoggerFactory.getLogger(CheckTable.class);

	private ParserCache parserCache = ParserCache.getInstance();
	private CacheTable cacheTable = CacheTable.getInstance();

	public CheckTable(DataSource dataSource)
	{
		new TableParser(dataSource);
	}

	public void check()
	{
		List<String> lackList = new ArrayList<>();
		for (Class<?> clazz : parserCache.allClazz())
		{
			CacheEntity cacheEntity = parserCache.get(clazz);
			Table table = cacheTable.getTable(cacheEntity.getTable());
			if (table == null)
			{
				lackList.add(cacheEntity.getTable());
				continue;
			}
			this.checkColumn(table, cacheEntity);
		}
		if (!lackList.isEmpty())
		{
			logger.warn("数据库缺少表：{}", lackList);
		}
	}

	private void checkColumn(Table table, CacheEntity cacheEntity)
	{
		Map<String, TableId> idMap = table.getIdMap();
		Map<String, TableColumn> columnMap = table.getColumnMap();
		List<String> lackList = new ArrayList<>();

		if (cacheEntity.getVersion() != null && !columnMap.containsKey(cacheEntity.getVersion().getColumn()))
		{
			lackList.add(cacheEntity.getVersion().getColumn());
		}
		for (Map.Entry<String, CacheId> entry : cacheEntity.getIdMap().entrySet())
		{
			if (!idMap.containsKey(entry.getValue().getColumn()))
			{
				lackList.add(entry.getValue().getColumn());
			}
		}
		for (Map.Entry<String, CacheColumn> entry : cacheEntity.getColumnMap().entrySet())
		{
			if (!columnMap.containsKey(entry.getValue().getColumn()))
			{
				lackList.add(entry.getValue().getColumn());
			}
		}
		if (!lackList.isEmpty())
		{
			logger.warn("表 {} 缺少列：{}", table.getTable(), lackList);
		}
	}

}
