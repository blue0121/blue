package blue.internal.jdbc.sql;

import blue.core.util.StringUtil;
import blue.internal.jdbc.parser.CacheColumn;
import blue.internal.jdbc.parser.CacheEntity;
import blue.internal.jdbc.parser.CacheId;
import blue.internal.jdbc.parser.CacheVersion;
import blue.internal.jdbc.parser.ParserCache;
import blue.jdbc.annotation.GeneratorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2019-11-24
 */
public class DefaultSqlHandler implements SqlHandler
{
	private static Logger logger = LoggerFactory.getLogger(DefaultSqlHandler.class);
	
	public DefaultSqlHandler()
	{
	}

	@Override
	public String sql(SqlParam param)
	{
		CacheEntity cacheEntity = ParserCache.getInstance().get(param.getClazz());
		List<String> idKeyList = new ArrayList<>();
		List<String> idValueList = new ArrayList<>();
		List<String> insertKeyList = new ArrayList<>();
		List<String> insertValueList = new ArrayList<>();
		List<String> idList = new ArrayList<>();
		List<String> idVersionList = new ArrayList<>();

		for (Map.Entry<String, CacheId> entry : cacheEntity.getIdMap().entrySet())
		{
			CacheId id = entry.getValue();
			idKeyList.add(id.getEscapeColumn());
			idValueList.add(":" + id.getName());
			idList.add(id.getEscapeColumn() + "=:" + id.getName());
			idVersionList.add(id.getEscapeColumn() + "=:" + id.getName());

			if (id.getGeneratorType() != GeneratorType.INCREMENT)
			{
				insertKeyList.add(id.getEscapeColumn());
				insertValueList.add(":" + id.getName());
			}
		}

		List<String> columnKeyList = new ArrayList<>();
		List<String> columnValueList = new ArrayList<>();
		List<String> columnList = new ArrayList<>();
		List<String> columnVersionList = new ArrayList<>();

		for (Map.Entry<String, CacheColumn> entry : cacheEntity.getColumnMap().entrySet())
		{
			CacheColumn column = entry.getValue();
			columnKeyList.add(column.getEscapeColumn());
			columnValueList.add(":" + column.getName());
			columnList.add(column.getEscapeColumn() + "=:" + column.getName());
			columnVersionList.add(column.getEscapeColumn() + "=:" + column.getName());

			insertKeyList.add(column.getEscapeColumn());
			insertValueList.add(":" + column.getName());
		}

		CacheVersion version = cacheEntity.getVersion();
		if (version != null)
		{
			insertKeyList.add(version.getEscapeColumn());
			insertValueList.add(":" + version.getName());

			idVersionList.add(version.getEscapeColumn() + "=:" + version.getName());
			columnVersionList.add(version.getEscapeColumn() + "=" + version.getEscapeColumn() + "+1");
		}

		//产生插入SQL语句
		if (idKeyList.size() > 0)
		{
			String insertSQL = String.format(INSERT_TPL, cacheEntity.getEscapeTable(),
					StringUtil.join(insertKeyList, ","),
					StringUtil.join(insertValueList, ","));
			logger.debug(cacheEntity.getClazz().getName() + " generate insert-sql: " + insertSQL);
			cacheEntity.setInsertSQL(insertSQL);
		}

		//产生更新SQL语句
		if (columnList.size() > 0)
		{
			String updateSQL = String.format(UPDATE_TPL, cacheEntity.getEscapeTable(),
					StringUtil.join(columnList, ","), StringUtil.join(idList, " and "));
			logger.debug(cacheEntity.getClazz().getName() + " generate update-sql: " + updateSQL);
			cacheEntity.setUpdateSQL(updateSQL);

			if (version != null)
			{
				String updateVersionSQL = String.format(UPDATE_TPL, cacheEntity.getEscapeTable(),
						StringUtil.join(columnVersionList, ","), StringUtil.join(idVersionList, " and "));
				logger.debug(cacheEntity.getClazz().getName() + " generate update-version-sql: " + updateVersionSQL);
				cacheEntity.setUpdateVersionSQL(updateVersionSQL);
			}
		}

		//产生删除SQL语句
		String deleteSQL = String.format(DELETE_TPL, cacheEntity.getEscapeTable(),
				StringUtil.join(idList, " and "));
		logger.debug(cacheEntity.getClazz().getName() + " generate delete-sql: " + deleteSQL);
		cacheEntity.setDeleteSQL(deleteSQL);

		if (idKeyList.size() == 1)
		{
			String idKey = idKeyList.get(0);

			//产生根据主键列表删除SQL语句
			String deleteListSQL = String.format(DELETE_TPL, cacheEntity.getEscapeTable(), idKey + " in (%s)");
			logger.debug(cacheEntity.getClazz().getName() + " generate delete-list-sql: " + deleteListSQL);
			cacheEntity.setDeleteListSQL(deleteListSQL);

			//产生根据主键列表查询SQL语句
			String getListSQL = String.format(SELECT_TPL, cacheEntity.getEscapeTable(), idKey + " in (%s)");
			logger.debug(cacheEntity.getClazz().getName() + " generate get-list-sql: " + getListSQL);
			cacheEntity.setGetListSQL(getListSQL);
		}

		//产生查询SQL语句
		String selectSQL = String.format(SELECT_TPL, cacheEntity.getEscapeTable(),
				StringUtil.join(idList, "and "));
		logger.debug(cacheEntity.getClazz().getName() + " generate get-sql: " + selectSQL);
		cacheEntity.setGetSQL(selectSQL);

		return SUCCESS;
	}
}
