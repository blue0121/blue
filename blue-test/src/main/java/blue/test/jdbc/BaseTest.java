package blue.test.jdbc;

import blue.jdbc.core.JdbcOperation;
import org.junit.jupiter.api.AfterEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Jin Zheng
 * @since 1.0 2019-12-16
 */
public abstract class BaseTest
{
	private static Logger logger = LoggerFactory.getLogger(BaseTest.class);

	protected JdbcOperation jdbcOperation;
	protected Class<?>[] classes;

	public BaseTest()
	{
	}

	@AfterEach
	public void after()
	{
		this.truncateTable();
	}

	protected void truncateTable()
	{
		for (Class<?> clazz : classes)
		{
			String table = jdbcOperation.getTable(clazz, true);
			String sql = "truncate table " + table;
			logger.info("truncate table: {}", sql);
			jdbcOperation.getJdbcTemplate().execute(sql);
		}
	}

	@Autowired
	public void setJdbcOperation(JdbcOperation jdbcOperation)
	{
		this.jdbcOperation = jdbcOperation;
	}
}
