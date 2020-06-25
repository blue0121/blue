package test.jdbc.sql;

import blue.internal.jdbc.sql.SqlType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.jdbc.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2019-12-05
 */
public class PartialInsertSqlHandlerTest extends SqlHandlerTest
{
	public PartialInsertSqlHandlerTest()
	{
	}

	@Test
	public void test1()
	{
		Map<String, Object> param = new HashMap<>();
		param.put("name", "blue");
		String sql = factory.handleMap(SqlType.PARTIAL_INSERT, User.class, param);
		System.out.println(sql);
		Assertions.assertEquals("insert into `usr_user` (`name`) values (:name)", sql);
	}

	@Test
	public void test2()
	{
		Map<String, Object> param = new HashMap<>();
		param.put("name", "blue");
		param.put("password", "123");
		String sql = factory.handleMap(SqlType.PARTIAL_INSERT, User.class, param);
		System.out.println(sql);
		Assertions.assertEquals("insert into `usr_user` (`password`,`name`) values (:password,:name)", sql);
	}

}
