package blue.internal.jdbc.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-21
 */
public class CacheMapper
{
	private Class<?> clazz; // 类型
	private Map<String, CacheColumn> columnMap = new HashMap<>(); // 字段

	public CacheMapper()
	{
	}

	public Class<?> getClazz()
	{
		return clazz;
	}

	public void setClazz(Class<?> clazz)
	{
		this.clazz = clazz;
	}

	public Map<String, CacheColumn> getColumnMap()
	{
		return columnMap;
	}

	public void putColumn(CacheColumn column)
	{
		columnMap.put(column.getName(), column);
	}
}
