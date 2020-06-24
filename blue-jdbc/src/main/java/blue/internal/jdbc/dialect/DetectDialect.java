package blue.internal.jdbc.dialect;

import blue.core.util.StringUtil;
import blue.jdbc.core.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2020-01-04
 */
public class DetectDialect
{
	private static Logger logger = LoggerFactory.getLogger(DetectDialect.class);

	private DetectDialect()
	{
	}

	public static Dialect dialect(DataSource dataSource) throws SQLException
	{
		try (Connection conn = dataSource.getConnection())
		{
			DatabaseMetaData meta = conn.getMetaData();
			String productName = meta.getDatabaseProductName();
			String productVersion = meta.getDatabaseProductVersion();
			String driverName = meta.getDriverName();
			String driverVersion = meta.getDriverVersion();
			String url = meta.getURL();
			String user = meta.getUserName();
			logger.info("database product: {}[{}], driver: {}[{}], url: {}, user: {}", productName, productVersion,
					driverName, driverVersion, url, user);

			return dialect(productName, productVersion, url);
		}
	}

	private static Dialect dialect(String productName, String productVersion, String url)
	{
		String jdbcType = StringUtil.getJdbcType(url);
		Dialect dialect = null;
		if ("mysql".equals(jdbcType))
		{
			dialect = new MySQLDialect();
		}
		else if ("postgresql".equals(jdbcType))
		{
			dialect = new PostgreSQLDialect();
		}
		else if ("oracle".equals(jdbcType))
		{
			dialect = new OracleDialect();
		}
		else if ("hsqldb".equals(jdbcType))
		{
			dialect = new HyperSQLDialect();
		}

		if (dialect != null)
		{
			logger.info("detect dialect: {}", dialect.getClass().getName());
		}

		return dialect;
	}

}
