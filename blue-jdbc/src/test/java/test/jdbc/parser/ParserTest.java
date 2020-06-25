package test.jdbc.parser;

import blue.internal.jdbc.parser.CacheColumn;
import blue.internal.jdbc.parser.CacheEntity;
import blue.internal.jdbc.parser.CacheId;
import blue.internal.jdbc.parser.CacheMapper;
import blue.internal.jdbc.parser.CacheVersion;
import blue.internal.jdbc.parser.Parser;
import blue.internal.jdbc.parser.ParserCache;
import blue.jdbc.annotation.GeneratorType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.jdbc.model.Group;
import test.jdbc.model.User;
import test.jdbc.model.UserGroup;
import test.jdbc.model2.User2;
import test.jdbc.model2.User3;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-22
 */
public class ParserTest
{
	private ParserCache parserCache = ParserCache.getInstance();
	private Parser parser = Parser.getInstance();

	public ParserTest()
	{
	}

	@Test
	public void testParseGroup()
	{
		parser.parse(Group.class);
		CacheEntity cacheEntity = parserCache.get(Group.class);
		Assertions.assertNotNull(cacheEntity);
		Assertions.assertEquals(Group.class, cacheEntity.getClazz());
		Assertions.assertEquals("group", cacheEntity.getTable());

		Assertions.assertEquals(1, cacheEntity.getIdMap().size());
		CacheId cacheId = cacheEntity.getIdMap().get("id");
		Assertions.assertNotNull(cacheId);
		Assertions.assertEquals(GeneratorType.INCREMENT, cacheId.getGeneratorType());
		Assertions.assertEquals("id", cacheId.getColumn());
		Assertions.assertEquals("getId", cacheId.getGetterMethod().getName());
		Assertions.assertEquals("setId", cacheId.getSetterMethod().getName());

		Assertions.assertEquals(1, cacheEntity.getColumnMap().size());
		CacheColumn cacheColumn = cacheEntity.getColumnMap().get("name");
		Assertions.assertNotNull(cacheColumn);
		Assertions.assertEquals("name", cacheColumn.getColumn());
		Assertions.assertEquals("getName", cacheColumn.getGetterMethod().getName());
		Assertions.assertEquals("setName", cacheColumn.getSetterMethod().getName());

		Assertions.assertNull(cacheEntity.getVersion());
		Assertions.assertTrue(cacheEntity.getExtraMap().isEmpty());
	}

	@Test
	public void testParseUser()
	{
		parser.parse(User.class);
		CacheEntity cacheEntity = parserCache.get(User.class);
		Assertions.assertNotNull(cacheEntity);
		Assertions.assertEquals(User.class, cacheEntity.getClazz());
		Assertions.assertEquals("usr_user", cacheEntity.getTable());

		Assertions.assertEquals(1, cacheEntity.getIdMap().size());
		CacheId cacheId = cacheEntity.getIdMap().get("id");
		Assertions.assertNotNull(cacheId);
		Assertions.assertEquals(GeneratorType.INCREMENT, cacheId.getGeneratorType());
		Assertions.assertEquals("id", cacheId.getColumn());
		Assertions.assertEquals("getId", cacheId.getGetterMethod().getName());
		Assertions.assertEquals("setId", cacheId.getSetterMethod().getName());

		Assertions.assertEquals(4, cacheEntity.getColumnMap().size());
		CacheColumn cacheColumn = cacheEntity.getColumnMap().get("name");
		Assertions.assertNotNull(cacheColumn);
		Assertions.assertEquals("name", cacheColumn.getColumn());
		Assertions.assertEquals("getName", cacheColumn.getGetterMethod().getName());
		Assertions.assertEquals("setName", cacheColumn.getSetterMethod().getName());

		Assertions.assertNotNull(cacheEntity.getVersion());
		CacheVersion cacheVersion = cacheEntity.getVersion();
		Assertions.assertNotNull(cacheVersion);
		Assertions.assertEquals("version", cacheVersion.getColumn());
		Assertions.assertEquals("getVersion", cacheVersion.getGetterMethod().getName());
		Assertions.assertEquals("setVersion", cacheVersion.getSetterMethod().getName());

		Assertions.assertEquals(1, cacheEntity.getExtraMap().size());
		CacheColumn extraColumn = cacheEntity.getExtraMap().get("groupName");
		Assertions.assertNotNull(extraColumn);
		Assertions.assertEquals("group_name", extraColumn.getColumn());
		Assertions.assertEquals("getGroupName", extraColumn.getGetterMethod().getName());
		Assertions.assertEquals("setGroupName", extraColumn.getSetterMethod().getName());
	}

	@Test
	public void testParseUserGroup()
	{
		parser.parse(UserGroup.class);
		CacheMapper cacheMapper = parserCache.getMapper(UserGroup.class);
		Assertions.assertNotNull(cacheMapper);
		Assertions.assertEquals(4, cacheMapper.getColumnMap().size());

		CacheColumn cacheColumn = cacheMapper.getColumnMap().get("userName");
		Assertions.assertNotNull(cacheColumn);
		Assertions.assertEquals("user_name", cacheColumn.getColumn());
		Assertions.assertEquals("getUserName", cacheColumn.getGetterMethod().getName());
		Assertions.assertEquals("setUserName", cacheColumn.getSetterMethod().getName());
	}

	@Test
	public void testParseUser2()
	{
		Assertions.assertThrows(IllegalArgumentException.class, () -> parser.parse(User2.class));
	}

	@Test
	public void testParseUser3()
	{
		Assertions.assertThrows(IllegalArgumentException.class, () -> parser.parse(User3.class));
	}

}
