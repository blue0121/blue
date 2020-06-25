package test.jdbc.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.jdbc.core.RowMapper;
import test.jdbc.model.User;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2019-12-06
 */
public class JdbcObjectTemplateListTest extends TemplateTest
{
	@Test
	public void testListField()
	{
		jdbcObjectTemplate.listField(User.class, String.class, "name", "groupId", 1);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).queryForList(sqlCaptor.capture(), mapCaptor.capture(), Mockito.eq(String.class));
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("select `name` from `usr_user` where `group_id`=:groupId", sql);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals(1, map.get("groupId"));
	}

	@Test
	public void testListField2()
	{
		jdbcObjectTemplate.listField(User.class, Integer.class, "count", "id", 2, "groupId", 1);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).queryForList(sqlCaptor.capture(), mapCaptor.capture(), Mockito.eq(Integer.class));
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("select `count` from `usr_user` where `group_id`=:groupId and `id`=:id", sql);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals(2, map.get("id"));
		Assertions.assertEquals(1, map.get("groupId"));
	}

	@Test
	public void testListObject()
	{
		jdbcObjectTemplate.listObject(User.class, "groupId", 1);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).query(sqlCaptor.capture(), mapCaptor.capture(), Mockito.any(RowMapper.class));
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("select * from `usr_user` where `group_id`=:groupId", sql);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals(1, map.get("groupId"));
	}

	@Test
	public void testListObject2()
	{
		jdbcObjectTemplate.listObject(User.class, "id", 2, "groupId", 1);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).query(sqlCaptor.capture(), mapCaptor.capture(), Mockito.any(RowMapper.class));
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("select * from `usr_user` where `group_id`=:groupId and `id`=:id", sql);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals(2, map.get("id"));
		Assertions.assertEquals(1, map.get("groupId"));
	}

}
