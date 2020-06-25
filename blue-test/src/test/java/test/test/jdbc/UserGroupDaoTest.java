package test.test.jdbc;

import blue.test.jdbc.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import test.test.jdbc.dao.GroupDao;
import test.test.jdbc.dao.UserDao;
import test.test.jdbc.dao.UserGroupDao;
import test.test.jdbc.model.Group;
import test.test.jdbc.model.User;
import test.test.jdbc.model.UserGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2019-12-24
 */
@SpringJUnitConfig(locations = {"/spring/dao.xml"})
public class UserGroupDaoTest extends BaseTest
{
	private UserDao userDao;
	private GroupDao groupDao;
	private UserGroupDao userGroupDao;

	public UserGroupDaoTest()
	{
		this.classes = new Class[] {Group.class, User.class};
	}

	@Test
	public void testList()
	{
		Group group = new Group("group");
		groupDao.save(group);

		List<User> userList = new ArrayList<>();
		for (int i = 0; i < 5; i++)
		{
			User user = new User("blue" + i, "blue" + i);
			user.setState(0);
			user.setGroupId(group.getId());
			userList.add(user);
		}
		userDao.saveList(userList);

		UserGroup param = new UserGroup();
		param.setGroupId(group.getId());
		Assertions.assertEquals(5, userGroupDao.getTotalResult(param));
		Assertions.assertEquals(5, userGroupDao.list(param).size());

		param.setName("blue2");
		Assertions.assertEquals(1, userGroupDao.getTotalResult(param));
		Assertions.assertEquals(1, userGroupDao.list(param).size());

		param.setName("bluea");
		Assertions.assertEquals(0, userGroupDao.getTotalResult(param));
		Assertions.assertEquals(0, userGroupDao.list(param).size());
	}

	@Autowired
	public void setUserDao(UserDao userDao)
	{
		this.userDao = userDao;
	}

	@Autowired
	public void setGroupDao(GroupDao groupDao)
	{
		this.groupDao = groupDao;
	}

	@Autowired
	public void setUserGroupDao(UserGroupDao userGroupDao)
	{
		this.userGroupDao = userGroupDao;
	}
}
