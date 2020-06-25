package test.jdbc.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import test.jdbc.model.User;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2019-12-06
 */
public class JdbcObjectTemplateDeleteTest extends TemplateTest
{
	@Test
	public void testDelete()
	{
		User user = new User();
		user.setId(1);
		jdbcObjectTemplate.delete(user);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).update(sqlCaptor.capture(), mapCaptor.capture());
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("delete from `usr_user` where `id`=:id", sql);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals(1, map.get("id"));
	}

	@Test
	public void testDeleteId()
	{
		jdbcObjectTemplate.deleteId(User.class, 1);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).update(sqlCaptor.capture(), mapCaptor.capture());
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("delete from `usr_user` where `id`=:id", sql);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals(1, map.get("id"));
	}


	@Test
	public void testDeleteId2()
	{
		jdbcObjectTemplate.delete(User.class, Arrays.asList(1, 2));

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);

		Mockito.verify(jdbcTemplate).update(sqlCaptor.capture());
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("delete from `usr_user` where `id` in (1,2)", sql);
	}

	@Test
	public void testDeleteBy()
	{
		jdbcObjectTemplate.deleteBy(User.class, "groupId", 1);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).update(sqlCaptor.capture(), mapCaptor.capture());
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("delete from `usr_user` where `group_id`=:groupId", sql);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals(1, map.get("groupId"));
	}

	@Test
	public void testDeleteBy2()
	{
		jdbcObjectTemplate.deleteBy(User.class, "groupId", 1, "name", "blue");

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).update(sqlCaptor.capture(), mapCaptor.capture());
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("delete from `usr_user` where `group_id`=:groupId and `name`=:name", sql);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals(1, map.get("groupId"));
		Assertions.assertEquals("blue", map.get("name"));
	}

}
