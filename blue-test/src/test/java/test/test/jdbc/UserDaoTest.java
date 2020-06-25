package test.test.jdbc;

import blue.test.jdbc.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import test.test.jdbc.dao.UserDao;
import test.test.jdbc.model.User;

/**
 * @author Jin Zheng
 * @since 1.0 2019-12-16
 */
@SpringJUnitConfig(locations = {"/spring/dao.xml"})
public class UserDaoTest extends BaseTest
{
	private UserDao userDao;

	public UserDaoTest()
	{
		this.classes = new Class[] {User.class};
	}

	@Test
	public void testSave1()
	{
		String name = "blue";
		User user = new User(name, name);
		int rs = userDao.save(user);
		Assertions.assertEquals(1, rs);

		User newUser = userDao.get(user.getId());
		Assertions.assertNotNull(newUser);
		Assertions.assertEquals(name, newUser.getName());
		Assertions.assertEquals(name, newUser.getPassword());
		Assertions.assertEquals(0, newUser.getState().intValue());
		System.out.println(newUser);
		Assertions.assertEquals(1, newUser.getVersion().intValue());

		newUser = userDao.getSelect(user.getId());
		Assertions.assertNotNull(newUser);
		Assertions.assertEquals(name, newUser.getName());
		Assertions.assertEquals(name, newUser.getPassword());
		Assertions.assertEquals(0, newUser.getState().intValue());
		Assertions.assertEquals(1, newUser.getVersion().intValue());

		newUser = userDao.getObject("id", user.getId());
		Assertions.assertNotNull(newUser);
		Assertions.assertEquals(name, newUser.getName());
		Assertions.assertEquals(name, newUser.getPassword());
		Assertions.assertEquals(0, newUser.getState().intValue());
		Assertions.assertEquals(1, newUser.getVersion().intValue());

		Assertions.assertEquals(name, userDao.getField(String.class, "name", "id", user.getId()));
		Assertions.assertEquals(name, userDao.getField(String.class, "password", "id", user.getId()));
		Assertions.assertEquals(1, userDao.getField(Integer.class, "version", "id", user.getId()).intValue());
	}

	@Autowired
	public void setUserDao(UserDao userDao)
	{
		this.userDao = userDao;
	}
}
