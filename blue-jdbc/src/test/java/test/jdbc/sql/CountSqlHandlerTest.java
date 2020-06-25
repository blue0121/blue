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
public class CountSqlHandlerTest extends SqlHandlerTest
{

	@Test
	public void test1()
	{
		Map<String, Object> map = new HashMap<>();
		map.put("name", "blue");
		String sql = factory.handleMap(SqlType.COUNT, User.class, map);
		System.out.println(sql);
		Assertions.assertEquals("select count(*) from `usr_user` where `name`=:name", sql);
	}

	@Test
	public void test2()
	{
		Map<String, Object> map = new HashMap<>();
		map.put("name", "blue");
		map.put("groupId", 1);
		String sql = factory.handleMap(SqlType.COUNT, User.class, map);
		System.out.println(sql);
		Assertions.assertEquals("select count(*) from `usr_user` where `group_id`=:groupId and `name`=:name", sql);
	}

}
