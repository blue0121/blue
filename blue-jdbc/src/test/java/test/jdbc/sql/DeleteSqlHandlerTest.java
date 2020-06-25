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
public class DeleteSqlHandlerTest extends SqlHandlerTest
{
	public DeleteSqlHandlerTest()
	{
	}

	@Test
	public void test1()
	{
		Map<String, Object> param = new HashMap<>();
		param.put("groupId", 1);
		String sql = factory.handleMap(SqlType.DELETE, User.class, param);
		System.out.println(sql);
		Assertions.assertEquals("delete from `usr_user` where `group_id`=:groupId", sql);
	}

	@Test
	public void test2()
	{
		Map<String, Object> param = new HashMap<>();
		param.put("groupId", 1);
		param.put("name", "blue");
		String sql = factory.handleMap(SqlType.DELETE, User.class, param);
		System.out.println(sql);
		Assertions.assertEquals("delete from `usr_user` where `group_id`=:groupId and `name`=:name", sql);
	}

}
