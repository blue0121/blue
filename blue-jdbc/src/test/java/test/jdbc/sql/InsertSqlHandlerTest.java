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
public class InsertSqlHandlerTest extends SqlHandlerTest
{
	public InsertSqlHandlerTest()
	{
	}

	@Test
	public void test1()
	{
		User user = new User();
		user.setName("blue");
		user.setGroupId(1);
		user.setPassword("123");
		Map<String, Object> map = ObjectUtil.toMap(user);
		String sql = factory.handleMap(SqlType.INSERT, User.class, map);
		System.out.println(sql);
		Assertions.assertEquals("insert into `usr_user` (`group_id`,`name`,`password`,`version`) values (:groupId,:name,:password,:version)", sql);
	}

	@Test
	public void test2()
	{
		User user = new User();
		user.setName("blue");
		String sql = factory.handleTarget(SqlType.INSERT, user);
		System.out.println(sql);
		Assertions.assertEquals("insert into `usr_user` (`name`,`version`) values (:name,:version)", sql);
	}

}
