package test.jdbc.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.jdbc.core.RowMapper;
import test.jdbc.model.User;
import test.jdbc.model.UserGroup;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2019-12-06
 */
public class JdbcObjectTemplateGetTest extends TemplateTest
{
	@Test
	public void testGet()
	{
		jdbcObjectTemplate.get(User.class, 1);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).query(sqlCaptor.capture(), mapCaptor.capture(), Mockito.any(RowMapper.class));
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals(dialect.page("select * from `usr_user` where `id`=:id", 0, 1), sql);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals(1, map.get("id"));
	}

	@Test
	public void testGet2()
	{
		jdbcObjectTemplate.get(User.class, Arrays.asList(1, 2));

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);

		Mockito.verify(nJdbcTemplate).query(sqlCaptor.capture(), Mockito.any(RowMapper.class));
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("select * from `usr_user` where `id` in (1,2)", sql);
	}

	@Test
	public void testGet3()
	{
		String sql = "select * from user_group where group_id=:groupId";
		UserGroup userGroup = new UserGroup();
		userGroup.setGroupId(1);
		jdbcObjectTemplate.get(sql, userGroup);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).query(sqlCaptor.capture(), mapCaptor.capture(), Mockito.any(RowMapper.class));
		String sql2 = sqlCaptor.getValue();
		System.out.println(sql2);
		Assertions.assertEquals(dialect.page(sql, 0, 1), sql2);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals(1, map.get("groupId"));
	}

	@Test
	public void testGetField()
	{
		jdbcObjectTemplate.getField(User.class, String.class, "name", "id", 1);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).queryForList(sqlCaptor.capture(), mapCaptor.capture(), Mockito.eq(String.class));
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals(dialect.page("select `name` from `usr_user` where `id`=:id", 0, 1), sql);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals(1, map.get("id"));
	}

	@Test
	public void testGetField2()
	{
		jdbcObjectTemplate.getField(User.class, Integer.class, "count", "groupId", 1);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).queryForList(sqlCaptor.capture(), mapCaptor.capture(), Mockito.eq(Integer.class));
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals(dialect.page("select `count` from `usr_user` where `group_id`=:groupId", 0, 1), sql);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals(1, map.get("groupId"));
	}

	@Test
	public void testGetObject()
	{
		jdbcObjectTemplate.getObject(User.class, "id", 1);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).query(sqlCaptor.capture(), mapCaptor.capture(), Mockito.any(RowMapper.class));
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals(dialect.page("select * from `usr_user` where `id`=:id", 0, 1), sql);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals(1, map.get("id"));
	}

	@Test
	public void testGetObject2()
	{
		jdbcObjectTemplate.getObject(User.class, "id", 2, "groupId", 1);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).query(sqlCaptor.capture(), mapCaptor.capture(), Mockito.any(RowMapper.class));
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals(dialect.page("select * from `usr_user` where `group_id`=:groupId and `id`=:id", 0, 1), sql);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals(2, map.get("id"));
		Assertions.assertEquals(1, map.get("groupId"));
	}

}
