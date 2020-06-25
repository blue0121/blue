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
public class GetFieldSqlHandlerTest extends SqlHandlerTest
{
	public GetFieldSqlHandlerTest()
	{
	}

	@Test
	public void test1()
	{
		Map<String, Object> param = new HashMap<>();
		param.put("id", 1);
		String sql = factory.handleField(SqlType.GET_FIELD, User.class, "name", param);
		System.out.println(sql);
		Assertions.assertEquals("select `name` from `usr_user` where `id`=:id", sql);
	}

	@Test
	public void test2()
	{
		Map<String, Object> param = new HashMap<>();
		param.put("name", "blue");
		param.put("password", "123");
		String sql = factory.handleField(SqlType.GET_FIELD, User.class, "name", param);
		System.out.println(sql);
		Assertions.assertEquals("select `name` from `usr_user` where `password`=:password and `name`=:name", sql);
	}

}
