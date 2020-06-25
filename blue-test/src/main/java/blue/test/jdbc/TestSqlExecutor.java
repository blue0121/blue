package blue.test.jdbc;

import blue.core.common.CoreConst;
import blue.jdbc.core.JdbcOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * 数据库测试
 *
 * @author zhengjin
 * @since 1.0 2018年12月02日
 */
public class TestSqlExecutor implements InitializingBean
{
	private static Logger logger = LoggerFactory.getLogger(TestSqlExecutor.class);

	private JdbcOperation jdbcOperation;

	private JdbcTemplate jdbcTemplate;
	
	public TestSqlExecutor()
	{
	}

	public void executeSqlFile(String path)
	{
		try (InputStream is = TestSqlExecutor.class.getResourceAsStream(path);
		     Scanner scanner = new Scanner(is, CoreConst.UTF_8))
		{
			StringBuilder sql = new StringBuilder(4096);
			while (scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				if (line == null || line.isEmpty())
					continue;

				line = line.trim();
				sql.append(line);
				// find one SQL
				if (line.endsWith(";"))
				{
					sql.delete(sql.length() - 1, sql.length());
					this.executeSql(sql.toString());
					// clear
					sql.delete(0, sql.length());
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void executeSql(String sql)
	{
		logger.info("execute sql: {}", sql);
		jdbcTemplate.execute(sql);
	}

	public void dropTable(Class<?> clazz)
	{
		String table = jdbcOperation.getTable(clazz, true);
		String sql = "drop table " + table;
		logger.info("drop table: {}", sql);
		jdbcTemplate.execute(sql);
	}

	public void truncatTable(Class<?> clazz)
	{
		String table = jdbcOperation.getTable(clazz, true);
		String sql = "truncate table " + table;
		logger.info("truncate table: {}", sql);
		jdbcTemplate.execute(sql);
	}

	public void deleteTable(Class<?> clazz)
	{
		String table = jdbcOperation.getTable(clazz, true);
		String sql = "delete from " + table;
		logger.info("delete: {}", sql);
		jdbcTemplate.update(sql);
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		this.jdbcTemplate = jdbcOperation.getJdbcTemplate();
		logger.info("initialize JdbcTemplate");
	}

	@Autowired
	public void setJdbcOperation(JdbcOperation jdbcOperation)
	{
		this.jdbcOperation = jdbcOperation;
	}
}
