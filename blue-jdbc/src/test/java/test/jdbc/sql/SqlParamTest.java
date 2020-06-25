package test.jdbc.sql;

import blue.internal.jdbc.sql.SqlParam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.jdbc.model.User;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-26
 */
public class SqlParamTest
{
	public SqlParamTest()
	{
	}

	@Test
	public void test()
	{
		Object[] args = {"abc"};
		SqlParam param1 = new SqlParam(User.class, args);
		Assertions.assertEquals(User.class, param1.getClazz());
		Assertions.assertArrayEquals(args, param1.getArgs());
		Assertions.assertNull(param1.getTarget());

		SqlParam param2 = new SqlParam(new User(), args);
		Assertions.assertEquals(User.class, param2.getClazz());
		Assertions.assertArrayEquals(args, param2.getArgs());
		Assertions.assertNotNull(param2.getTarget());
	}

}
