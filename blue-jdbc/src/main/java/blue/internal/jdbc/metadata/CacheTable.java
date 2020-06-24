package blue.internal.jdbc.metadata;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2019-12-07
 */
public class CacheTable
{

	private static volatile CacheTable instance;

	private Map<String, Table> tableMap = new HashMap<>();

	private CacheTable()
	{
	}

	public static CacheTable getInstance()
	{
		if (instance == null)
		{
			synchronized (CacheTable.class)
			{
				instance = new CacheTable();
			}
		}
		return instance;
	}

	public Set<String> tableNames()
	{
		return tableMap.keySet();
	}

	public Table getTable(String table)
	{
		return tableMap.get(table);
	}

	public void putTable(Table table)
	{
		tableMap.put(table.getTable(), table);
	}

}
