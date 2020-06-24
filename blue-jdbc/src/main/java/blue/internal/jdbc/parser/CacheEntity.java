package blue.internal.jdbc.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存持久对象
 * 
 * @author zhengj
 * @since 1.0 2011-4-1
 */
public class CacheEntity
{
	private String table; // 表名
	private String escapeTable; // 表名转义
	private Class<?> clazz; // 类型

	private CacheVersion version; // 版本
	private CacheId id; // 单个主键
	private Map<String, CacheId> idMap = new HashMap<>(); // 主键
	private Map<String, CacheColumn> columnMap = new HashMap<>(); // 字段
	private Map<String, CacheColumn> extraMap = new HashMap<>(); // 额外字段

	private String insertSQL; // 默认插入语句
	private String updateSQL; // 默认更新语句
	private String updateVersionSQL; // 带乐观锁（版本）更新语句
	private String deleteSQL; // 默认删除语句
	private String getSQL; // 默认根据主键取得完整对象语句
	private String getListSQL; // 默认根据主键取得完整对象列表语句
	private String deleteListSQL; // 默认删除主键语句

	public CacheEntity()
	{
	}

	public String getTable()
	{
		return table;
	}

	public void setTable(String table)
	{
		this.table = table;
	}

	public String getEscapeTable()
	{
		return escapeTable;
	}

	public void setEscapeTable(String escapeTable)
	{
		this.escapeTable = escapeTable;
	}

	public Class<?> getClazz()
	{
		return clazz;
	}

	public void setClazz(Class<?> clazz)
	{
		this.clazz = clazz;
	}

	public Map<String, CacheId> getIdMap()
	{
		return idMap;
	}

	public void putId(CacheId id)
	{
		this.idMap.put(id.getName(), id);
		this.id = id;
	}

	public CacheId getId()
	{
		if (idMap.size() != 1)
			return null;

		return id;
	}

	public CacheVersion getVersion()
	{
		return version;
	}

	public void setVersion(CacheVersion version)
	{
		this.version = version;
	}

	public Map<String, CacheColumn> getColumnMap()
	{
		return columnMap;
	}

	public void putColumn(CacheColumn column)
	{
		columnMap.put(column.getName(), column);
	}

	public Map<String, CacheColumn> getExtraMap()
	{
		return extraMap;
	}

	public void putExtra(CacheColumn extra)
	{
		extraMap.put(extra.getName(), extra);
	}

	public String getInsertSQL()
	{
		return insertSQL;
	}

	public void setInsertSQL(String insertSQL)
	{
		this.insertSQL = insertSQL;
	}

	public String getUpdateSQL()
	{
		return updateSQL;
	}

	public void setUpdateSQL(String updateSQL)
	{
		this.updateSQL = updateSQL;
	}

	public String getUpdateVersionSQL()
	{
		return updateVersionSQL;
	}

	public void setUpdateVersionSQL(String updateVersionSQL)
	{
		this.updateVersionSQL = updateVersionSQL;
	}

	public String getDeleteSQL()
	{
		return deleteSQL;
	}

	public void setDeleteSQL(String deleteSQL)
	{
		this.deleteSQL = deleteSQL;
	}

	public String getGetSQL()
	{
		return getSQL;
	}

	public void setGetSQL(String getSQL)
	{
		this.getSQL = getSQL;
	}

	public String getGetListSQL()
	{
		return getListSQL;
	}

	public void setGetListSQL(String getListSQL)
	{
		this.getListSQL = getListSQL;
	}

	public String getDeleteListSQL()
	{
		return deleteListSQL;
	}

	public void setDeleteListSQL(String deleteListSQL)
	{
		this.deleteListSQL = deleteListSQL;
	}

}
