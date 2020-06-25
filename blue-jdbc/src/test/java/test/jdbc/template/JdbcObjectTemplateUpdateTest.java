package test.jdbc.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import test.jdbc.model.Group;
import test.jdbc.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2019-12-06
 */
public class JdbcObjectTemplateUpdateTest extends TemplateTest
{
	@Test
	public void testUpdate1()
	{
		Mockito.when(nJdbcTemplate.update(Mockito.anyString(), Mockito.anyMap())).thenReturn(1);

		User user = new User();
		user.setId(1);
		user.setName("blue");
		jdbcObjectTemplate.update(user);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).update(sqlCaptor.capture(), mapCaptor.capture());
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("update `usr_user` set `name`=:name,`version`=`version`+1 where `id`=:id and `version`=:version", sql);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals("blue", map.get("name"));
		Assertions.assertEquals(1, map.get("version"));
		Assertions.assertEquals(1, map.get("id"));
	}

	@Test
	public void testUpdate2()
	{
		Mockito.when(nJdbcTemplate.update(Mockito.anyString(), Mockito.anyMap())).thenReturn(1);

		User user = new User();
		user.setName("blue");
		user.setId(1);
		jdbcObjectTemplate.update(user, false);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).update(sqlCaptor.capture(), mapCaptor.capture());
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("update `usr_user` set `count`=:count,`group_id`=:groupId,`name`=:name,`password`=:password,`version`=`version`+1 where `id`=:id and `version`=:version", sql);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals("blue", map.get("name"));
		Assertions.assertEquals(1, map.get("version"));
		Assertions.assertEquals(1, map.get("id"));
	}

	@Test
	public void testUpdate3()
	{
		Mockito.when(nJdbcTemplate.update(Mockito.anyString(), Mockito.anyMap())).thenReturn(1);

		Group group = new Group();
		group.setId(1);
		group.setName("blue");
		jdbcObjectTemplate.update(group);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).update(sqlCaptor.capture(), mapCaptor.capture());
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("update `group` set `name`=:name where `id`=:id", sql);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals("blue", map.get("name"));
		Assertions.assertEquals(1, map.get("id"));
	}

	@Test
	public void testUpdateObject()
	{
		jdbcObjectTemplate.updateObject(User.class, 1, "name", "blue");

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).update(sqlCaptor.capture(), mapCaptor.capture());
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("update `usr_user` set `name`=:name where `id`=:id", sql);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals("blue", map.get("name"));
		Assertions.assertEquals(1, map.get("id"));
	}

	@Test
	public void testUpdateList()
	{
		Mockito.when(nJdbcTemplate.batchUpdate(Mockito.anyString(), Mockito.any(Map[].class))).thenReturn(new int[]{1,1});
		List<User> userList = new ArrayList<>();
		User user1 = new User();
		user1.setId(1);
		user1.setName("blue1");
		userList.add(user1);
		User user2 = new User();
		user2.setId(2);
		user2.setName("blue2");
		userList.add(user2);

		jdbcObjectTemplate.updateList(userList);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map[]> mapCaptor = ArgumentCaptor.forClass(Map[].class);

		Mockito.verify(nJdbcTemplate).batchUpdate(sqlCaptor.capture(), mapCaptor.capture());
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("update `usr_user` set `count`=:count,`group_id`=:groupId,`name`=:name,`password`=:password,`version`=`version`+1 where `id`=:id and `version`=:version", sql);
		Map<String, Object>[] maps = mapCaptor.getValue();
		System.out.println(Arrays.toString(maps));
		Assertions.assertEquals(2, maps.length);
		List<String> nameList = Arrays.asList(user1.getName(), user2.getName());
		for (Map<String, Object> map : maps)
		{
			String name = (String)map.get("name");
			Assertions.assertTrue(nameList.contains(name));
		}
	}

}
