package blue.internal.jdbc.metadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2019-12-07
 */
public class TableParser
{
	private static Logger logger = LoggerFactory.getLogger(TableParser.class);

	public TableParser(DataSource dataSource)
	{
		try (Connection conn = dataSource.getConnection())
		{
			this.init(conn);
		}
		catch (SQLException e)
		{
			logger.error("数据库连接错误：", e);
		}
	}

	public TableParser(Connection connection)
	{
		try (Connection conn = connection)
		{
			this.init(conn);
		}
		catch (SQLException e)
		{
			logger.error("数据库连接错误：", e);
		}
	}

	public TableParser(String url, String username, String password)
	{
		try (Connection conn = DriverManager.getConnection(url, username, password))
		{
			this.init(conn);
		}
		catch (SQLException e)
		{
			logger.error("数据库连接错误：", e);
		}
	}

	private void init(Connection conn) throws SQLException
	{
		CacheTable cacheTable = CacheTable.getInstance();
		DatabaseMetaData meta = conn.getMetaData();
		List<Table> tableList = this.parseTable(meta);

		for (Table table : tableList)
		{
			this.parseColumn(meta, table);
			cacheTable.putTable(table);
		}
	}

	private void parseColumn(DatabaseMetaData meta, Table table) throws SQLException
	{
		try (ResultSet rs = meta.getPrimaryKeys(null, null, table.getTable()))
		{
			while (rs.next())
			{
				String name = rs.getString("COLUMN_NAME");

				TableId id = new TableId();
				id.setColumn(name);
				table.putId(id);
			}
		}
		try (ResultSet rs = meta.getColumns(null, null, table.getTable(), null))
		{
			while (rs.next())
			{
				String name = rs.getString("COLUMN_NAME");
				int type = rs.getInt("DATA_TYPE");
				String typeName = rs.getString("TYPE_NAME");
				int size = rs.getInt("COLUMN_SIZE");
				String nullable = rs.getString("IS_NULLABLE");
				String inc = rs.getString("IS_AUTOINCREMENT");
				String def = rs.getString("COLUMN_DEF");
				String comment = rs.getString("REMARKS");

				TableId id = table.getIdMap().get(name);
				if (id != null)
				{
					id.setInc(!"NO".equals(inc));
				}
				TableColumn column = id;
				if (column == null)
				{
					column = new TableColumn();
					column.setColumn(name);
					table.putColumn(column);
				}
				column.setType(type);
				column.setTypeName(typeName);
				column.setSize(size);
				column.setNullable(!"NO".equals(nullable));
				column.setDef("NULL".equals(def) ? null : def);
				column.setComment(comment);
			}
		}
	}

	private List<Table> parseTable(DatabaseMetaData meta) throws SQLException
	{
		List<Table> tableList = new ArrayList<>();
		try (ResultSet rs = meta.getTables(null, null, null, new String[] {"TABLE"}))
		{
			while (rs.next())
			{
				String name = rs.getString("TABLE_NAME");
				String comment = rs.getString("REMARKS");
				Table table = new Table(name);
				table.setComment(comment);
				tableList.add(table);
			}
		}
		logger.info("all tables: {}", tableList);
		return tableList;
	}

}
