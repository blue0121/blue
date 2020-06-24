package blue.internal.jdbc.metadata;

import java.util.HashMap;
import java.util.Map;

public class Table
{
	private String table;
	private String comment;
	private Map<String, TableId> idMap = new HashMap<>();
	private Map<String, TableColumn> columnMap = new HashMap<>();

	public Table()
	{
	}

	public Table(String table)
	{
		this.table = table;
	}

	public String getTable()
	{
		return table;
	}

	public void setTable(String table)
	{
		this.table = table;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public Map<String, TableId> getIdMap()
	{
		return idMap;
	}

	public void putId(TableId id)
	{
		idMap.put(id.getColumn(), id);
	}

	public Map<String, TableColumn> getColumnMap()
	{
		return columnMap;
	}

	public void putColumn(TableColumn column)
	{
		columnMap.put(column.getColumn(), column);
	}

	@Override
	public String toString()
	{
		return table;
	}
}
