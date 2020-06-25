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
public class IncSqlHandlerTest extends SqlHandlerTest
{
	public IncSqlHandlerTest()
	{
	}

	@Test
	public void test1()
	{
		Map<String, Object> param = new HashMap<>();
		param.put("count", 1);
		String sql = factory.handleMap(SqlType.INC, User.class, param);
		System.out.println(sql);
		Assertions.assertEquals("update `usr_user` set `count`=`count`+:count where `id`=:id", sql);
	}

	@Test
	public void test2()
	{
		Map<String, Object> param = new HashMap<>();
		param.put("count", 1);
		param.put("groupId", 1);
		String sql = factory.handleMap(SqlType.INC, User.class, param);
		System.out.println(sql);
		Assertions.assertEquals("update `usr_user` set `group_id`=`group_id`+:groupId,`count`=`count`+:count where `id`=:id", sql);
	}

}
