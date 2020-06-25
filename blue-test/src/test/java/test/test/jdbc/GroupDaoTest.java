package test.test.jdbc;

import blue.test.jdbc.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import test.test.jdbc.dao.GroupDao;
import test.test.jdbc.model.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Jin Zheng
 * @since 1.0 2019-12-17
 */
@SpringJUnitConfig(locations = {"/spring/dao.xml"})
public class GroupDaoTest extends BaseTest
{
	private GroupDao groupDao;

	public GroupDaoTest()
	{
		this.classes = new Class[] {Group.class};
	}

	@Test
	public void testSaveObject()
	{
		groupDao.saveObject("name", "blue");
		List<Group> groupList = groupDao.list(null);
		Assertions.assertEquals(1, groupList.size());
		Group newGroup = groupList.get(0);
		System.out.println(newGroup.getId());
		Assertions.assertNotNull(newGroup.getId());
		Assertions.assertEquals("blue", newGroup.getName());
		newGroup = groupDao.get(newGroup.getId());
		System.out.println(newGroup.getId());
		Assertions.assertNotNull(newGroup);
		Assertions.assertEquals("blue", newGroup.getName());
	}

	@Test
	public void testSaveList()
	{
		int size = 10;
		List<Group> groupList = new ArrayList<>();
		for (int i = 0; i < size; i++)
		{
			groupList.add(new Group("blue_" + i));
		}
		groupDao.saveList(groupList);
		groupList = groupDao.list(null);
		Assertions.assertEquals(size, groupList.size());
		List<Integer> idList = groupList.stream().map(Group::getId).collect(Collectors.toList());
		Map<Integer, Group> groupMap = groupDao.getList(idList);
		Assertions.assertEquals(size, groupMap.size());
		for (int i = 0; i < size; i++)
		{
			int index = size - 1 - i;
			Integer id = idList.get(index);
			Group group = groupMap.get(id);
			Assertions.assertNotNull(group);
			Assertions.assertEquals("blue_" + i, group.getName());
		}

		groupDao.delete(idList);
		Assertions.assertEquals(0, groupDao.getTotalResult(null));
	}

	@Test
	public void testUpdate()
	{
		Group group = new Group("blue");
		groupDao.save(group);
		Group newGroup = groupDao.get(group.getId());
		Assertions.assertNotNull(newGroup);
		Assertions.assertEquals(group.getName(), newGroup.getName());

		group.setName("blue10");
		groupDao.update(group);
		newGroup = groupDao.get(group.getId());
		Assertions.assertNotNull(newGroup);
		Assertions.assertEquals(group.getName(), newGroup.getName());

		groupDao.updateObject(group.getId(), "name", "blue20");
		newGroup = groupDao.get(group.getId());
		Assertions.assertNotNull(newGroup);
		Assertions.assertEquals("blue20", newGroup.getName());
	}

	@Test
	public void testDelete()
	{
		Group group = new Group("blue");
		groupDao.save(group);
		Assertions.assertEquals(1, groupDao.getTotalResult(null));

		groupDao.delete(group);
		Assertions.assertEquals(0, groupDao.getTotalResult(null));

		groupDao.save(group);
		Assertions.assertEquals(1, groupDao.getTotalResult(null));

		groupDao.deleteId(group.getId());
		Assertions.assertEquals(0, groupDao.getTotalResult(null));

		groupDao.save(group);
		Assertions.assertEquals(1, groupDao.getTotalResult(null));

		groupDao.deleteBy("id", group.getId());
		Assertions.assertEquals(0, groupDao.getTotalResult(null));

		groupDao.save(group);
		Assertions.assertEquals(1, groupDao.getTotalResult(null));

		groupDao.deleteBy("name", group.getName());
		Assertions.assertEquals(0, groupDao.getTotalResult(null));
	}

	@Test
	public void testInc()
	{
		Group group = new Group("blue");
		groupDao.save(group);

		Group newGroup = groupDao.get(group.getId());
		Assertions.assertNotNull(newGroup);
		Assertions.assertEquals(0, newGroup.getCount().intValue());

		groupDao.inc(group.getId(), "count", 1);
		newGroup = groupDao.get(group.getId());
		Assertions.assertNotNull(newGroup);
		Assertions.assertEquals(1, newGroup.getCount().intValue());

		groupDao.inc(group.getId(), "count", 10);
		newGroup = groupDao.get(group.getId());
		Assertions.assertNotNull(newGroup);
		Assertions.assertEquals(11, newGroup.getCount().intValue());
	}

	@Test
	public void testExist()
	{
		Group param = new Group("blue");
		Assertions.assertFalse(groupDao.exist(param, "name"));

		Group group = new Group(param.getName());
		groupDao.save(group);
		Assertions.assertTrue(groupDao.exist(param, "name"));

		param.setId(group.getId());
		Assertions.assertFalse(groupDao.exist(param, "name"));

		Assertions.assertEquals(1, groupDao.count("name", param.getName()));
	}

	@Autowired
	public void setGroupDao(GroupDao groupDao)
	{
		this.groupDao = groupDao;
	}
}
