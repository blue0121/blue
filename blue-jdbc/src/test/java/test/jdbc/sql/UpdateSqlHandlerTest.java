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
public class UpdateSqlHandlerTest extends SqlHandlerTest
{
	public UpdateSqlHandlerTest()
	{
	}

	@Test
	public void test1()
	{
		User user = new User();
		user.setId(1);
		user.setVersion(1);
		user.setName("blue");
		user.setGroupId(1);
		user.setPassword("123");
		Map<String, Object> map = ObjectUtil.toMap(user);
		String sql = factory.handleMap(SqlType.UPDATE, User.class, map);
		System.out.println(sql);
		Assertions.assertEquals("update `usr_user` set `group_id`=:groupId,`name`=:name,`password`=:password,`version`=`version`+1 where `id`=:id and `version`=:version", sql);
	}

	@Test
	public void test2()
	{
		User user = new User();
		user.setId(1);
		user.setName("blue");
		String sql = factory.handleTarget(SqlType.UPDATE, user);
		System.out.println(sql);
		Assertions.assertEquals("update `usr_user` set `name`=:name,`version`=`version`+1 where `id`=:id and `version`=:version", sql);
	}

}
