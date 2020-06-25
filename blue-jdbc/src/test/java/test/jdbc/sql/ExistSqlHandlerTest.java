package test.jdbc.sql;

import blue.internal.jdbc.sql.SqlType;
import blue.internal.jdbc.util.ObjectUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.jdbc.model.User;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2019-12-05
 */
public class ExistSqlHandlerTest extends SqlHandlerTest
{
	public ExistSqlHandlerTest()
	{
	}

	@Test
	public void test1()
	{
		User user = new User();
		user.setName("blue");
		Map<String, Object> map = ObjectUtil.toMap(user);
		String sql = factory.handleMap(SqlType.EXIST, User.class, map, "name");
		System.out.println(sql);
		Assertions.assertEquals("select count(*) from `usr_user` where `name`=:name", sql);
	}

	@Test
	public void test2()
	{
		User user = new User();
		user.setId(1);
		user.setName("blue");
		Map<String, Object> map = ObjectUtil.toMap(user);
		System.out.println(map);
		String sql = factory.handleMap(SqlType.EXIST, User.class, map, "name");
		System.out.println(sql);
		Assertions.assertEquals("select count(*) from `usr_user` where `name`=:name and `id`!=:id", sql);
	}

}
