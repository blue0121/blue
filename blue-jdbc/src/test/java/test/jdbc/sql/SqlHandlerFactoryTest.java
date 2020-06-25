package test.jdbc.sql;

import blue.internal.jdbc.parser.CacheEntity;
import blue.internal.jdbc.parser.ParserCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.jdbc.model.Group;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-25
 */
public class SqlHandlerFactoryTest extends SqlHandlerTest
{
	@Test
	public void testInit()
	{
		CacheEntity cacheEntity = ParserCache.getInstance().get(Group.class);
		Assertions.assertTrue(cacheEntity.getInsertSQL().startsWith("insert"));
	}

}
