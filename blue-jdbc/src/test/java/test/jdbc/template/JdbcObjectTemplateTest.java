package test.jdbc.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import test.jdbc.model.User;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2019-12-06
 */
public class JdbcObjectTemplateTest extends TemplateTest
{
	@Test
	public void testInc()
	{
		jdbcObjectTemplate.inc(User.class, 1, "count", 1);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).update(sqlCaptor.capture(), mapCaptor.capture());
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("update `usr_user` set `count`=`count`+:count where `id`=:id", sql);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals(1, map.get("id"));
		Assertions.assertEquals(1, map.get("count"));
	}

	@Test
	public void testExist()
	{
		Mockito.when(nJdbcTemplate.queryForObject(Mockito.anyString(), Mockito.anyMap(), Mockito.eq(Integer.class)))
				.thenReturn(0);

		User user = new User();
		user.setName("blue");
		jdbcObjectTemplate.exist(user, "name");

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).queryForObject(sqlCaptor.capture(), mapCaptor.capture(), Mockito.eq(Integer.class));
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("select count(*) from `usr_user` where `name`=:name", sql);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals("blue", map.get("name"));
	}

	@Test
	public void testExist2()
	{
		Mockito.when(nJdbcTemplate.queryForObject(Mockito.anyString(), Mockito.anyMap(), Mockito.eq(Integer.class)))
				.thenReturn(0);

		User user = new User();
		user.setName("blue");
		user.setId(1);
		jdbcObjectTemplate.exist(user, "name");

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).queryForObject(sqlCaptor.capture(), mapCaptor.capture(), Mockito.eq(Integer.class));
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("select count(*) from `usr_user` where `name`=:name and `id`!=:id", sql);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals("blue", map.get("name"));
		Assertions.assertEquals(1, map.get("id"));
	}

	@Test
	public void testCount()
	{
		Mockito.when(nJdbcTemplate.queryForObject(Mockito.anyString(), Mockito.anyMap(), Mockito.eq(Integer.class)))
				.thenReturn(0);

		jdbcObjectTemplate.count(User.class, "groupId", 1);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).queryForObject(sqlCaptor.capture(), mapCaptor.capture(), Mockito.eq(Integer.class));
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("select count(*) from `usr_user` where `group_id`=:groupId", sql);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals(1, map.get("groupId"));
	}

}
