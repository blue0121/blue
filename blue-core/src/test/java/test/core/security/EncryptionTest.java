package test.core.security;

import blue.core.security.SpringBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import test.core.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2019-11-24
 */
@SpringJUnitConfig(locations = {"classpath:/spring/enc.xml"})
public class EncryptionTest
{
	private User user;

	public EncryptionTest()
	{
	}

	@Test
	public void testEncrypt()
	{
		Assertions.assertNotNull(user);
		Assertions.assertEquals("root", user.getName());
		Assertions.assertEquals("root", user.getPassword());

		User spy = Mockito.spy(user);
		Assertions.assertEquals("root", spy.getName());
	}

	@Test
	public void testSpringBean()
	{
		Assertions.assertNotNull(SpringBean.getBean(User.class));
		Assertions.assertNotNull(SpringBean.getBean("user", User.class));

		List<String> list = new ArrayList<>();
		list.add("a");
		Assertions.assertEquals("a", list.get(0));
		List<String> spyList = Mockito.spy(list);
		Assertions.assertEquals("a", spyList.get(0));
	}

	@Autowired
	public void setUser(User user)
	{
		this.user = user;
	}
}
