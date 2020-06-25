package test.jdbc.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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
public class JdbcObjectTemplateSaveTest extends TemplateTest
{
	@Test
	public void testSave1()
	{
		User user = new User();
		user.setName("blue");
		jdbcObjectTemplate.save(user);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<MapSqlParameterSource> mapCaptor = ArgumentCaptor.forClass(MapSqlParameterSource.class);

		Mockito.verify(nJdbcTemplate).update(sqlCaptor.capture(), mapCaptor.capture(), Mockito.any());
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("insert into `usr_user` (`name`,`version`) values (:name,:version)", sql);
		Map<String, Object> map = mapCaptor.getValue().getValues();
		System.out.println(map);
		Assertions.assertEquals("blue", map.get("name"));
		Assertions.assertEquals(1, map.get("version"));
	}

	@Test
	public void testSave2()
	{
		User user = new User();
		user.setName("blue");
		jdbcObjectTemplate.save(user, false);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<MapSqlParameterSource> mapCaptor = ArgumentCaptor.forClass(MapSqlParameterSource.class);

		Mockito.verify(nJdbcTemplate).update(sqlCaptor.capture(), mapCaptor.capture(), Mockito.any());
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("insert into `usr_user` (`count`,`group_id`,`name`,`password`,`version`) values (:count,:groupId,:name,:password,:version)", sql);
		Map<String, Object> map = mapCaptor.getValue().getValues();
		System.out.println(map);
		Assertions.assertEquals("blue", map.get("name"));
		Assertions.assertEquals(1, map.get("version"));
	}

	@Test
	public void testSave3()
	{
		Group group = new Group();
		group.setName("blue");
		jdbcObjectTemplate.save(group);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<MapSqlParameterSource> mapCaptor = ArgumentCaptor.forClass(MapSqlParameterSource.class);

		Mockito.verify(nJdbcTemplate).update(sqlCaptor.capture(), mapCaptor.capture(), Mockito.any());
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("insert into `group` (`name`) values (:name)", sql);
		Map<String, Object> map = mapCaptor.getValue().getValues();
		System.out.println(map);
		Assertions.assertEquals("blue", map.get("name"));
	}

	@Test
	public void testSaveObject()
	{
		jdbcObjectTemplate.saveObject(User.class, "name", "blue");

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);

		Mockito.verify(nJdbcTemplate).update(sqlCaptor.capture(), mapCaptor.capture());
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("insert into `usr_user` (`name`) values (:name)", sql);
		Map<String, Object> map = mapCaptor.getValue();
		System.out.println(map);
		Assertions.assertEquals("blue", map.get("name"));
	}

	@Test
	public void testSaveList()
	{
		List<User> userList = new ArrayList<>();
		User user1 = new User();
		user1.setName("blue1");
		userList.add(user1);
		User user2 = new User();
		user2.setName("blue2");
		userList.add(user2);

		jdbcObjectTemplate.saveList(userList);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Map[]> mapCaptor = ArgumentCaptor.forClass(Map[].class);

		Mockito.verify(nJdbcTemplate).batchUpdate(sqlCaptor.capture(), mapCaptor.capture());
		String sql = sqlCaptor.getValue();
		System.out.println(sql);
		Assertions.assertEquals("insert into `usr_user` (`count`,`group_id`,`name`,`password`,`version`) values (:count,:groupId,:name,:password,:version)", sql);
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
